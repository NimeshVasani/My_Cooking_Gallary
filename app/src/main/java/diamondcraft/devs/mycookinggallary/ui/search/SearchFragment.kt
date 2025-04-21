package diamondcraft.devs.mycookinggallary.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import diamondcraft.devs.mycookinggallary.CookingResultActivity
import diamondcraft.devs.mycookinggallary.R
import diamondcraft.devs.mycookinggallary.adapters.RecyclerGuideAdapter
import diamondcraft.devs.mycookinggallary.databinding.FragmentDiscoverBinding
import diamondcraft.devs.mycookinggallary.databinding.FragmentSearchBinding
import diamondcraft.devs.mycookinggallary.db.Cooking
import diamondcraft.devs.mycookinggallary.models.CookingResponse
import diamondcraft.devs.mycookinggallary.other.Resources
import diamondcraft.devs.mycookinggallary.viewmodels.CookingViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlin.getValue

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<CookingViewModel>()
    private lateinit var adapter: RecyclerGuideAdapter // or your custom adapter

    private val searchQueryFlow = MutableStateFlow("")

    val searchResult = MutableStateFlow<List<Cooking>>(mutableListOf())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearch() // ✅ Safe here — fragment is fully attached

        binding.searchFood.addTextChangedListener { query ->
            if (!query.isNullOrBlank()) {
                searchQueryFlow.value = query.toString()
            }
        }
        binding.backCookingResult.setOnClickListener {
            findNavController().popBackStack()
        }
        // Collect result from API and update searchResult

        lifecycleScope.launchWhenStarted {
            searchQueryFlow
                .debounce(300) // 300ms wait after last input
                .filter { it.isNotBlank() }
                .collectLatest { query ->
                    binding.progressBar.visibility = View.VISIBLE

                    viewModel.searchRecipes(query)
                }
        }
        adapter.setOnItemClickListener { cooking ->
            cooking.recipes?.let { viewModel.updateRelatedSharedData(it) }

            viewModel.updateSharedData(cooking)
            requireActivity().startActivity(
                Intent(
                    requireActivity(),
                    CookingResultActivity::class.java
                )
            )
        }
        lifecycleScope.launchWhenStarted {
            viewModel.searchRecipes.collectLatest { result ->
                when (result) {
                    is Resources.Success -> {
                        binding.progressBar.visibility = View.GONE
                        searchResult.value = result.data?.results ?: emptyList()
                        if (searchResult.value.isEmpty()) {
                            binding.noResult.visibility = View.VISIBLE
                        } else {
                            binding.noResult.visibility = View.GONE
                        }
                    }

                    is Resources.Error -> {
                        // show error message
                        binding.progressBar.visibility = View.GONE
                        binding.noResult.visibility = View.VISIBLE
                        binding.noResult.text = result.message
                    }

                    is Resources.Loading -> {
                        // show loading indicator if needed
                        binding.noResult.visibility = View.GONE
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            searchResult.collectLatest {
                adapter.asyncDiffer.submitList(it)
            }
        }

    }


    private fun setupSearch() {
        adapter = RecyclerGuideAdapter()
        binding.recyclerSearch.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerSearch.adapter = adapter

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}