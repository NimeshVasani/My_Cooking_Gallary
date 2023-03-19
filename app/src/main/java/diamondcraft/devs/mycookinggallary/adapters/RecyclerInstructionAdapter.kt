package diamondcraft.devs.mycookinggallary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.textview.MaterialTextView
import diamondcraft.devs.mycookinggallary.databinding.ItemInstructionStepLayoutBinding
import diamondcraft.devs.mycookinggallary.models.CookingInstruction

class RecyclerInstructionAdapter :
    RecyclerView.Adapter<RecyclerInstructionAdapter.InstructionHolder>() {

    private lateinit var binding: ItemInstructionStepLayoutBinding
    private val differCallbacks = object : DiffUtil.ItemCallback<CookingInstruction>() {
        override fun areItemsTheSame(
            oldItem: CookingInstruction,
            newItem: CookingInstruction
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CookingInstruction,
            newItem: CookingInstruction
        ): Boolean {
            return oldItem.position == newItem.position
        }
    }

    val asyncDiffer: AsyncListDiffer<CookingInstruction> = AsyncListDiffer(this, differCallbacks)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionHolder {
        binding = ItemInstructionStepLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return InstructionHolder(binding.root)
    }

    override fun onBindViewHolder(holder: InstructionHolder, position: Int) {
        val currentList = asyncDiffer.currentList[position]
        holder.apply {
            tvPosition.text = currentList.position

            tvInstructionData.text =
                "${currentList.display_text}  \n \nappliance = ${currentList.appliance?:"Not Applicable"}"

        }
    }

    override fun getItemCount(): Int {
        return asyncDiffer.currentList.size
    }

    inner class InstructionHolder(itemView: View) : ViewHolder(itemView) {
        var tvPosition: MaterialTextView = binding.tvInstructionStepNumber
        var tvInstructionData = binding.tvDescription

    }


}