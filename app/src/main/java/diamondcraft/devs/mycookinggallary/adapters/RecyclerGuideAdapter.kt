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
import diamondcraft.devs.mycookinggallary.databinding.ItemGuideLayoutBinding
import diamondcraft.devs.mycookinggallary.db.Cooking

class RecyclerGuideAdapter : RecyclerView.Adapter<RecyclerGuideAdapter.GuideHolder>() {
    private lateinit var binding: ItemGuideLayoutBinding

    private var differCallback = object : DiffUtil.ItemCallback<Cooking>() {
        override fun areItemsTheSame(oldItem: Cooking, newItem: Cooking): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Cooking, newItem: Cooking): Boolean {
            return oldItem == newItem
        }

    }
    var asyncDiffer: AsyncListDiffer<Cooking> = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideHolder {
        binding = ItemGuideLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuideHolder(binding.root)
    }

    override fun onBindViewHolder(holder: GuideHolder, position: Int) {
        val currentList = asyncDiffer.currentList[position]
        Glide.with(holder.itemView.context).asBitmap().load(currentList.thumbnail_url)
            .thumbnail().diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).apply(RequestOptions.centerCropTransform()).into(holder.imageMain)

        holder.tvTitle.text = currentList.name
        holder.tvDescription.text = currentList.description
        holder.tvCreatorName.text = currentList.credits?.get(0)?.name ?: "Unknown -The Master Chef"

        if (!holder.tvDescription.text.isNullOrBlank()) {
            holder.tvDescription.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(currentList) }
        }
        holder.btnGuideSave.setOnClickListener {
            onItemSaveClickListener?.let { it(currentList) }
        }
    }

    override fun getItemCount(): Int {
        return asyncDiffer.currentList.size
    }

    private var onItemClickListener: ((Cooking) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cooking) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemSaveClickListener: ((Cooking) -> Unit)? = null

    fun setOnItemSaveClickListener(listener: (Cooking) -> Unit) {
        onItemSaveClickListener = listener
    }

    inner class GuideHolder(itemView: View) : ViewHolder(itemView) {
        var imageMain = binding.imgGuideMain
        var profilePic = binding.imgGuideProfile
        var tvCreatorName = binding.tvCreatorName
        var tvTitle = binding.tvRecipesTitle
        var tvDescription = binding.tvDescriptions
        var btnGuideSave = binding.btnGuideSave
    }
}