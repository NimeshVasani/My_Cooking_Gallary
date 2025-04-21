package diamondcraft.devs.mycookinggallary.ui.guides

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ms.square.android.expandabletextview.BuildConfig
import diamondcraft.devs.mycookinggallary.CookingResultActivity
import diamondcraft.devs.mycookinggallary.adapters.RecyclerGuideAdapter
import diamondcraft.devs.mycookinggallary.databinding.FragmentGuidesBinding
import diamondcraft.devs.mycookinggallary.db.Cooking
import diamondcraft.devs.mycookinggallary.other.Resources
import diamondcraft.devs.mycookinggallary.viewmodels.CookingViewModel

class GuidesFragment : Fragment() {

    private var _binding: FragmentGuidesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var guideAdapter: RecyclerGuideAdapter
    private val viewModels by activityViewModels<CookingViewModel>()
    private val allRecipesList: MutableList<Cooking> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGuidesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpAdapter()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModels.allRecipes.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { cookingResponse ->
                        for (result in cookingResponse.results) {
                            if (result.recipes?.isNotEmpty() == true) {
                                allRecipesList.addAll(result.recipes)
                            } else {
                                allRecipesList.add(result)
                            }
                        }
                        guideAdapter.asyncDiffer.submitList(allRecipesList)
                    }
                }
                is Resources.Error -> {

                    response.message?.let { message ->
                        if (BuildConfig.DEBUG) Log.d("response", "error$message")
                    }
                }
                is Resources.Loading -> {
                }
            }
            guideAdapter.setOnItemSaveClickListener { cooking ->
                cooking.sections?.get(0)?.components?.get(0)?.ingredient?.name?.toString()?.let { Log.d("creditkeys", it) }
                viewModels.addCooking(cooking)
            }
            guideAdapter.setOnItemClickListener { cooking ->
                cooking.recipes?.let { viewModels.updateRelatedSharedData(it) }

                viewModels.updateSharedData(cooking)
                requireActivity().startActivity(
                    Intent(
                        requireActivity(),
                        CookingResultActivity::class.java
                    )
                )
            }
        }
    }

    private fun setUpAdapter() {
        guideAdapter = RecyclerGuideAdapter()
        binding.recyclerGuide.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerGuide.adapter = guideAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}