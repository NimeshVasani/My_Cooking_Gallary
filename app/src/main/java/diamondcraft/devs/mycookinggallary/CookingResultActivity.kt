package diamondcraft.devs.mycookinggallary

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import diamondcraft.devs.mycookinggallary.adapters.RecyclerDiscoverAdapter
import diamondcraft.devs.mycookinggallary.adapters.RecyclerIngradientAdapter
import diamondcraft.devs.mycookinggallary.adapters.RecyclerInstructionAdapter
import diamondcraft.devs.mycookinggallary.adapters.RecyclerNutritionAdapter
import diamondcraft.devs.mycookinggallary.databinding.ActivityCookingResultBinding
import diamondcraft.devs.mycookinggallary.db.Cooking
import diamondcraft.devs.mycookinggallary.other.GlobalFunctions
import diamondcraft.devs.mycookinggallary.other.IngradientData
import diamondcraft.devs.mycookinggallary.viewmodels.CookingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import androidx.core.net.toUri


@AndroidEntryPoint
class CookingResultActivity : AppCompatActivity() {
    private lateinit var cooking: Cooking
    private lateinit var relatedCookingData: MutableList<Cooking>
    private lateinit var binding: ActivityCookingResultBinding
    private val viewModel by viewModels<CookingViewModel>()
    private val recyclerDiscoverAdapter = RecyclerDiscoverAdapter()
    private val listIngradient: MutableList<IngradientData> = mutableListOf()

    private lateinit var nutritionRecyclerAdapter: RecyclerNutritionAdapter
    private lateinit var ingradientRecyclerAdapter: RecyclerIngradientAdapter
    private lateinit var instructionRecyclerAdapter: RecyclerInstructionAdapter
    var numOfServings: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCookingResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.liveSharedData.observe(this) { cooking ->
            this.cooking = cooking
            Log.d("cooking", cooking.toString())
        }

        setUpNutritionRecycler()
        setUpIngradientRecycler()
        setUpInstructionRecycler()
        viewModel.liveSharedRelatedData.observe(this) { cookingList ->
            relatedCookingData = cookingList
            recyclerDiscoverAdapter.asyncDiffer.submitList(relatedCookingData)
//            Toast.makeText(this, relatedCookingData[0].name, Toast.LENGTH_LONG).show()

        }
        binding.backCookingResult.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val relatedItemList: MutableList<Cooking> = mutableListOf()

        CoroutineScope(Dispatchers.Main).launch {
            relatedItemList.add(cooking)
            Glide.with(applicationContext).asBitmap().load(cooking.thumbnail_url)
                .into(binding.imgThumbnailView)
            binding.videoRecipesMain.setVideoURI(cooking.video_url?.toUri())
            val mediaController = MediaController(this@CookingResultActivity)
            mediaController.setMediaPlayer(binding.videoRecipesMain)

            binding.videoRecipesMain.apply {
                setMediaController(mediaController)
                setOnCompletionListener {}
            }

            binding.videoRecipesMain.setOnPreparedListener {
                mediaController.setAnchorView(binding.videoRecipesMain)

            }
            binding.btnVideoStart.setOnClickListener {
                binding.imgThumbnailView.visibility = View.GONE
                binding.videoRecipesMain.start()
                it.visibility = View.GONE
            }
            binding.recyclerOthersCooking.layoutManager = LinearLayoutManager(
                this@CookingResultActivity, LinearLayoutManager.HORIZONTAL, false
            )

            binding.recyclerOthersCooking.adapter = recyclerDiscoverAdapter

            cooking.nutrition?.let {
                val listNutrition: MutableList<String> = mutableListOf()
                listNutrition.add(it.sugar.toString())
                listNutrition.add(it.fat.toString())
                listNutrition.add(it.fiber.toString())
                listNutrition.add(it.carbohydrates.toString())
                listNutrition.add(it.protein.toString())
                listNutrition.add(it.calories.toString())
                nutritionRecyclerAdapter.asyncDiffer.submitList(listNutrition)

            }

            cooking.instructions?.let { cookingInstructionsList ->
                Log.d("Instruction", cookingInstructionsList[0].display_text.toString())
                instructionRecyclerAdapter.asyncDiffer.submitList(cookingInstructionsList)
            }

            getAndSetIngredientData()
            numOfServings = cooking.num_servings ?: 0
            binding.tvNumServings.text = numOfServings.toString()
            binding.tvIngredientsData.text = "$numOfServings people"

            binding.tvAddServings.setOnClickListener {
                listIngradient.clear()

                getAndSetIngredientData(
                    (binding.tvNumServings.text.toString()
                        .toDouble() + 1.0) / numOfServings
                )

                binding.tvNumServings.text =
                    (binding.tvNumServings.text.toString().toInt() + 1).toString()

                Toast.makeText(
                    this@CookingResultActivity,
                    binding.tvNumServings.text.toString(),
                    Toast.LENGTH_LONG
                )
                    .show()
                ingradientRecyclerAdapter.notifyItemRangeChanged(0, listIngradient.size)
            }


            recyclerDiscoverAdapter.setOnItemClickListener { cooking ->

                relatedItemList.addAll(relatedCookingData)
                relatedItemList.remove(cooking)
                viewModel.updateSharedData(cooking)
                viewModel.updateRelatedSharedData(relatedItemList)
                onStart()
            }
            binding.tvPeopleLikes.apply {
                text = "${
                    (cooking.user_ratings?.score?.toDouble())?.times(100)?.toInt()
                }"
                append(resources.getString(R.string.people_likes))
            }
            val description = cooking.description
            if (description.isNullOrBlank()) binding.tvDescription.visibility = View.GONE
            binding.tvDescription.text = description



            binding.tvCookingResultTitle.text = cooking.name.toString()
        }
    }

    private fun getAndSetIngredientData(numOfServings: Double = 1.0) {
        var string: String = ""
        cooking.sections?.let { sectionsList ->
            for (section in sectionsList) {
                section.components?.let { componentList ->

                    for (component in componentList) {
                        string =
                            component.measurements?.get(component.measurements!!.size - 1)?.quantity
                                ?: "¼"
                        Log.d("numFormat",string.plus("n"))
                        listIngradient.add(
                            IngradientData(
                                name = component.ingredient?.name,
                                quantity1 = (GlobalFunctions.fromVulgarFraction(string) * numOfServings).toString(),
                                quantity2 = DecimalFormat("#.##").format(
                                    GlobalFunctions.fromVulgarFraction(
                                        component.measurements?.get(
                                            component.measurements!!.size - 1
                                        )?.quantity!!
                                    ) * numOfServings
                                ).toString(),
                                unit1 = component.measurements?.get(0)?.unit,
                                unit2 = component.measurements?.get(component.measurements!!.size - 1)?.unit,
                            )
                        )
                        Log.d("ingradient", component.ingredient?.name.toString())

                    }
                }
            }
            ingradientRecyclerAdapter.asyncDiffer.submitList(listIngradient)
        }

    }


    private fun setUpNutritionRecycler() {
        nutritionRecyclerAdapter = RecyclerNutritionAdapter()
        binding.recyclerNutrition.layoutManager = LinearLayoutManager(
            applicationContext, LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerNutrition.adapter = nutritionRecyclerAdapter

    }

    private fun setUpIngradientRecycler() {
        ingradientRecyclerAdapter = RecyclerIngradientAdapter()
        binding.recyclerIngradient.layoutManager = LinearLayoutManager(
            applicationContext, LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerIngradient.adapter = ingradientRecyclerAdapter

    }

    private fun setUpInstructionRecycler() {
        instructionRecyclerAdapter = RecyclerInstructionAdapter()
        binding.recyclerInstruction.layoutManager = LinearLayoutManager(
            applicationContext, LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerInstruction.adapter = instructionRecyclerAdapter

    }

}

