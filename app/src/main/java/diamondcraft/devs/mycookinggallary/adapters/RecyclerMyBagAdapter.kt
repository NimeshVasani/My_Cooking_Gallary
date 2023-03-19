package diamondcraft.devs.mycookinggallary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import diamondcraft.devs.mycookinggallary.databinding.ItemSavedLayoutBinding
import diamondcraft.devs.mycookinggallary.db.Cooking

class RecyclerMyBagAdapter : RecyclerView.Adapter<RecyclerMyBagAdapter.MyBagHolder>() {
    private lateinit var binding: ItemSavedLayoutBinding


    private var diffCallback = object : DiffUtil.ItemCallback<Cooking>() {
        override fun areItemsTheSame(oldItem: Cooking, newItem: Cooking): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Cooking, newItem: Cooking): Boolean {
            return oldItem.name == newItem.name
        }
    }

    val asyncDiffer: AsyncListDiffer<Cooking> = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBagHolder {
        binding = ItemSavedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyBagHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyBagHolder, position: Int) {
        val currentList = asyncDiffer.currentList[position]
        holder.apply {
            Glide.with(holder.itemView.context).asBitmap().load(currentList.thumbnail_url)
                .thumbnail().diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.centerCropTransform()).into(imgMyBagMain)
            recipesTitle.text = currentList.name
            //recipesCreator.text = currentList.credits?.get(0)?.name ?: "Unknown - The Master Chef"
            recipesDelete.setOnClickListener {
                onItemDeleteClickListener?.let { it(currentList) }
            }
            itemView.setOnClickListener {
                onItemClickListener?.let { it(currentList) }
            }
        }
    }

    private var onItemClickListener: ((Cooking) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cooking) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemDeleteClickListener: ((Cooking) -> Unit)? = null

    fun setOnItemDeleteClickListener(listener: (Cooking) -> Unit) {
        onItemDeleteClickListener = listener
    }

    override fun getItemCount(): Int {
        return asyncDiffer.currentList.size
    }

    inner class MyBagHolder(itemView: View) : ViewHolder(itemView) {
        var imgMyBagMain = binding.imgMyBag
        var recipesTitle = binding.tvMyBagTitle
        var recipesCreator = binding.tvMyBagCreatorName
        var recipesDelete = binding.btnDeleteMyBag

    }
}