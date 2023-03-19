package diamondcraft.devs.mycookinggallary.ui.bag

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import diamondcraft.devs.mycookinggallary.CookingResultActivity
import diamondcraft.devs.mycookinggallary.adapters.RecyclerMyBagAdapter
import diamondcraft.devs.mycookinggallary.databinding.FragmentMyBagBinding
import diamondcraft.devs.mycookinggallary.viewmodels.CookingViewModel

class MyBagFragment : Fragment() {

    private var _binding: FragmentMyBagBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<CookingViewModel>()
    private lateinit var myBagAdapter: RecyclerMyBagAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this)[MyBagViewModel::class.java]

        _binding = FragmentMyBagBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setUpMyBagRecycler()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSavedCooking().observe(viewLifecycleOwner) {
            myBagAdapter.asyncDiffer.submitList(it)
        }
        myBagAdapter.setOnItemClickListener { cooking ->
            cooking.recipes?.let { viewModel.updateRelatedSharedData(it) }

            viewModel.updateSharedData(cooking)
            requireActivity().startActivity(
                Intent(
                    requireActivity(),
                    CookingResultActivity::class.java
                )
            )
        }
        myBagAdapter.setOnItemDeleteClickListener { cooking ->
            viewModel.deleteCooking(cooking)
        }
    }

    private fun setUpMyBagRecycler() {
        myBagAdapter = RecyclerMyBagAdapter()
        binding.recyclerMyBag.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerMyBag.adapter = myBagAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}