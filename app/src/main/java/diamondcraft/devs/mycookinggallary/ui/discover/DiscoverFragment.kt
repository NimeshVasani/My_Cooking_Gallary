package diamondcraft.devs.mycookinggallary.ui.discover

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ms.square.android.expandabletextview.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import diamondcraft.devs.mycookinggallary.CookingResultActivity
import diamondcraft.devs.mycookinggallary.R
import diamondcraft.devs.mycookinggallary.adapters.RecyclerDiscoverAdapter
import diamondcraft.devs.mycookinggallary.adapters.StaggeredAdapter
import diamondcraft.devs.mycookinggallary.databinding.FragmentDiscoverBinding
import diamondcraft.devs.mycookinggallary.db.Cooking
import diamondcraft.devs.mycookinggallary.other.Resources
import diamondcraft.devs.mycookinggallary.viewmodels.CookingViewModel
import java.text.Normalizer

@AndroidEntryPoint
class DiscoverFragment : Fragment(), java.io.Serializable {
    private lateinit var staggeredAdapter: StaggeredAdapter
    private lateinit var recommendRecycler: RecyclerDiscoverAdapter
    private lateinit var weeklyRecycler: RecyclerDiscoverAdapter
    private lateinit var seasonalRecycler: RecyclerDiscoverAdapter
    private var _binding: FragmentDiscoverBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<CookingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        val root: View = binding.root
        staggeredAdapter = StaggeredAdapter()
        setUpStaggerRecycler()

        recommendRecycler = RecyclerDiscoverAdapter()
        setUpRecommendRecyclerView()

        println(fromVulgarFraction("1¼"))

        weeklyRecycler = RecyclerDiscoverAdapter()
        setUpWeeklyRecycler()

        seasonalRecycler = RecyclerDiscoverAdapter()
        setUpSeasonalRecycler()


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val staggeredList: MutableList<Cooking> = mutableListOf()
        binding.inputDiscover.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_discover_to_searchFragment)

        }
        viewModel.allRecipes.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { cookingResponse ->
                        for (result in cookingResponse.results) {
                            if (result.recipes.isNullOrEmpty()) {
                                staggeredList.add(result)
                            } else {
                                staggeredList.addAll(result.recipes)
                            }
                        }
                        staggeredAdapter.differ.submitList(staggeredList)

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
        }

        val recommendItemList: MutableList<Cooking> = mutableListOf()
        val weeklyItemList: MutableList<Cooking> = mutableListOf()
        val seasonalItemList: MutableList<Cooking> = mutableListOf()

        viewModel.allFeeds.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { cookingFeedResponse ->
                        for (result in cookingFeedResponse.results) {
                            Log.d("category", result.category.toString())
                            if (result.type == "featured") {
                                Log.d("type", "\n${result.type}")
                                Glide.with(requireContext()).asBitmap()
                                    .load(result.item!!.thumbnail_url).thumbnail().placeholder(
                                        R.drawable.ic_dummy_pizza
                                    ).apply(RequestOptions.fitCenterTransform())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.imageMainDiscover)
                                binding.imageMainDiscover.setOnClickListener {
                                    viewModel.updateSharedData(result.item!!)
                                    result.item!!.recipes?.let {
                                        viewModel.updateRelatedSharedData(
                                            it
                                        )
                                    }
                                    requireActivity().startActivity(
                                        Intent(
                                            requireActivity(), CookingResultActivity::class.java
                                        )
                                    )
                                }
                                binding.firstItemDiscover.text = "Let's Try \n${result.item!!.name}"
                            }

                            if (result.category == "Trending") {
                                Log.d("trending", "success")
                                result.items?.let { recommendItemList.addAll(it) }
                                result.item?.let { recommendItemList.add(it) }

                            }

                            if (result.name == "Popular Recipes This Week") {
                                if (result.items != null) for (item in result.items!!.iterator()) item.recipes?.let {
                                    weeklyItemList.addAll(
                                        it
                                    )
                                }

                                result.item?.recipes.let {
                                    if (it != null) {
                                        weeklyItemList.addAll(it)
                                    }
                                }

                            }
                            if (result.category == "Seasonal") {
                                result.items?.let { seasonalItemList.addAll(it) }
                                result.item?.let { seasonalItemList.add(it) }
                            }
                        }
                        recommendRecycler.asyncDiffer.submitList(null)
                        recommendRecycler.asyncDiffer.submitList(recommendItemList)

                        weeklyRecycler.asyncDiffer.submitList(null)
                        weeklyRecycler.asyncDiffer.submitList(weeklyItemList)

                        seasonalRecycler.asyncDiffer.submitList(null)
                        seasonalRecycler.asyncDiffer.submitList(seasonalItemList)

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
        }
        val relatedItemList: MutableList<Cooking> = mutableListOf()
        recommendRecycler.setOnItemClickListener { cooking ->
            relatedItemList.clear()
            for (i in 0 until recommendItemList.size) {
                val random = recommendItemList.random()
                if (!relatedItemList.contains(random)) {
                    relatedItemList.add(random)
                }
                if (relatedItemList.size >= 5) break
            }
            viewModel.updateSharedData(cooking)
            viewModel.updateRelatedSharedData(relatedItemList)
            requireActivity().startActivity(
                Intent(
                    requireActivity(), CookingResultActivity::class.java
                )
            )

        }
        seasonalRecycler.setOnItemClickListener { cooking ->
            cooking.recipes?.let { viewModel.updateRelatedSharedData(it) }

            viewModel.updateSharedData(cooking)
            requireActivity().startActivity(
                Intent(
                    requireActivity(), CookingResultActivity::class.java
                )
            )

        }
        weeklyRecycler.setOnItemClickListener { cooking ->
            cooking.recipes?.let { viewModel.updateRelatedSharedData(it) }

            viewModel.updateSharedData(cooking)
            requireActivity().startActivity(
                Intent(
                    requireActivity(), CookingResultActivity::class.java
                )
            )

        }
        recommendRecycler.setOnItemSaveClickListener { cooking ->
            cooking.credits?.toString()?.let { Log.d("creditkeys", it) }
            viewModel.addCooking(cooking)
        }
        seasonalRecycler.setOnItemSaveClickListener { cooking ->
            cooking.credits?.toString()?.let { Log.d("creditkeys", it) }
            viewModel.addCooking(cooking)
        }
        weeklyRecycler.setOnItemSaveClickListener { cooking ->
            cooking.credits?.toString()?.let { Log.d("creditkeys", it) }
            viewModel.addCooking(cooking)
        }
        staggeredAdapter.setOnItemSaveClickListener { cooking ->
            cooking.credits?.toString()?.let { Log.d("creditkeys", it) }
            viewModel.addCooking(cooking)
        }
        staggeredAdapter.setOnItemClickListener { cooking ->
            cooking.recipes?.let { viewModel.updateRelatedSharedData(it) }

            viewModel.updateSharedData(cooking)
            requireActivity().startActivity(
                Intent(
                    requireActivity(), CookingResultActivity::class.java
                )
            )
        }
    }

    private fun setUpRecommendRecyclerView() {

        binding.recyclerDiscoverItem1.adapter = recommendRecycler
        binding.recyclerDiscoverItem1.layoutManager = LinearLayoutManager(
            requireContext(), HORIZONTAL, false
        )
    }

    private fun setUpWeeklyRecycler() {

        binding.recyclerDiscoverItem2.layoutManager = LinearLayoutManager(
            requireContext(), HORIZONTAL, false
        )
        binding.recyclerDiscoverItem2.adapter = weeklyRecycler

    }

    private fun setUpSeasonalRecycler() {

        binding.recyclerDiscoverItem3.layoutManager = LinearLayoutManager(
            requireContext(), HORIZONTAL, false
        )
        binding.recyclerDiscoverItem3.adapter = seasonalRecycler

    }

    private fun setUpStaggerRecycler() {
        binding.recyclerDiscoverItem4.itemAnimator = DefaultItemAnimator()
        binding.recyclerDiscoverItem4.adapter = staggeredAdapter
        binding.recyclerDiscoverItem4.isNestedScrollingEnabled = false
        binding.recyclerDiscoverItem4.layoutManager = StaggeredGridLayoutManager(
            2, LinearLayoutManager.VERTICAL
        ).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        }
        binding.recyclerDiscoverItem4.setHasFixedSize(true)

    }

    fun fromVulgarFraction(number: String): Double {
        val items = number.split("""\d""".toRegex()).filterNot { it.isEmpty() }
        val mixed: String?
        val fraction = mutableListOf<String>()
        return if (items.isNotEmpty()) {
            mixed = items.first()
            fraction.addAll(Normalizer.normalize(mixed, Normalizer.Form.NFKC).split("\u2044"))
            val decimal = fraction[0].toInt().toDouble() / fraction[1].toDouble()
            val result = """\d+""".toRegex().find(number)
            if (result != null) {
                result.value.toDouble() + decimal
            } else {
                decimal
            }
        } else {
            number.toDouble()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}