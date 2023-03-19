package diamondcraft.devs.mycookinggallary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import diamondcraft.devs.mycookinggallary.databinding.ItemIngradientLayoutBinding

class RecyclerNutritionAdapter :
    RecyclerView.Adapter<RecyclerNutritionAdapter.NutritionHolder>() {

    private lateinit var binding: ItemIngradientLayoutBinding
    private var nutritionList: MutableList<String> =
        mutableListOf("Sugar", "fat", "fiber", "carbohydrates", "Protein ", " Calories")
    private var diffCallbacks = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
    val asyncDiffer: AsyncListDiffer<String> = AsyncListDiffer(this, diffCallbacks)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionHolder {
        binding =
            ItemIngradientLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NutritionHolder(binding.root)
    }

    override fun onBindViewHolder(holder: NutritionHolder, position: Int) {
        val currentList = asyncDiffer.currentList[position]
        holder.tvIngradientName.text = nutritionList[position]
        holder.tvIngradientData.text = currentList
    }

    override fun getItemCount(): Int {
        return if (asyncDiffer.currentList.size >= nutritionList.size) nutritionList.size
        else asyncDiffer.currentList.size
    }

    inner class NutritionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvIngradientName = binding.tvIngradientName
        var tvIngradientData = binding.tvIngradientData
    }
}