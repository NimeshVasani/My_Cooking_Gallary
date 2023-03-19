package diamondcraft.devs.mycookinggallary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textview.MaterialTextView
import diamondcraft.devs.mycookinggallary.databinding.ItemLayout2InnerBinding
import diamondcraft.devs.mycookinggallary.db.Cooking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecyclerDiscoverAdapter : RecyclerView.Adapter<RecyclerDiscoverAdapter.DiscoverViewHolder>() {
    private lateinit var binding: ItemLayout2InnerBinding

    private var differCallback = object : DiffUtil.ItemCallback<Cooking>() {
        override fun areItemsTheSame(
            oldItem: Cooking, newItem: Cooking
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: Cooking, newItem: Cooking
        ): Boolean {
            return oldItem == newItem
        }
    }

    var asyncDiffer: AsyncListDiffer<Cooking> = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverViewHolder {
        binding =
            ItemLayout2InnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscoverViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
        val currentList = asyncDiffer.currentList[position]
        CoroutineScope(Dispatchers.Main).launch {
            Glide.with(holder.itemView.context).asBitmap().load(currentList.thumbnail_url)
                .thumbnail().diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.centerCropTransform()).into(holder.innerImageView)

            holder.tvInner.text = currentList.name
        }
        holder.btnDiscoverLike.setOnClickListener {
            onItemSaveClickListener?.let { it(currentList) }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(currentList) }
        }
    }

    private var onItemSaveClickListener: ((Cooking) -> Unit)? = null

    fun setOnItemSaveClickListener(listener: (Cooking) -> Unit) {
        onItemSaveClickListener = listener
    }

    override fun getItemCount(): Int {
        return asyncDiffer.currentList.size
    }


    private var onItemClickListener: ((Cooking) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cooking) -> Unit) {
        onItemClickListener = listener
    }

    inner class DiscoverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var innerImageView: ImageView = binding.imgInnerItems1
        var tvInner: MaterialTextView = binding.tvInnerItems
        var btnDiscoverLike = binding.btnDiscoverLike
    }


}