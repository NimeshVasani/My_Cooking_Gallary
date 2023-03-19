package diamondcraft.devs.mycookinggallary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import diamondcraft.devs.mycookinggallary.databinding.ItemIngradientLayoutBinding
import diamondcraft.devs.mycookinggallary.other.IngradientData

class RecyclerIngradientAdapter() :
    RecyclerView.Adapter<RecyclerIngradientAdapter.IngradientHolder>() {
    private lateinit var binding: ItemIngradientLayoutBinding
    private val differCallbacks = object : DiffUtil.ItemCallback<IngradientData>() {
        override fun areItemsTheSame(oldItem: IngradientData, newItem: IngradientData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: IngradientData, newItem: IngradientData): Boolean {
            return oldItem.name == newItem.name
        }
    }

    val asyncDiffer: AsyncListDiffer<IngradientData> = AsyncListDiffer(this, differCallbacks)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngradientHolder {
        binding = ItemIngradientLayoutBinding.inflate(
            LayoutInflater.from(parent.rootView.context),
            parent,
            false
        )
        return IngradientHolder(binding.root)
    }

    override fun onBindViewHolder(holder: IngradientHolder, position: Int) {

        val currentList = asyncDiffer.currentList[position]


        holder.tvIngradientName.text = currentList.name
        holder.tvIngradientData.text = currentList.quantity2.plus( currentList.unit2!!.abbreviation)

    }

    override fun getItemCount(): Int {
        return asyncDiffer.currentList.size
    }

    inner class IngradientHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIngradientName = binding.tvIngradientName
        val tvIngradientData = binding.tvIngradientData
    }

}