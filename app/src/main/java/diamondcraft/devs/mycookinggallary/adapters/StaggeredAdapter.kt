package diamondcraft.devs.mycookinggallary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import diamondcraft.devs.mycookinggallary.R
import diamondcraft.devs.mycookinggallary.databinding.ItemLayout2Binding
import diamondcraft.devs.mycookinggallary.db.Cooking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StaggeredAdapter :
    RecyclerView.Adapter<StaggeredAdapter.StaggeredHolder>() {
    private lateinit var binding: ItemLayout2Binding

    private val differCallback = object : DiffUtil.ItemCallback<Cooking>() {
        override fun areItemsTheSame(oldItem: Cooking, newItem: Cooking): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Cooking, newItem: Cooking): Boolean {
            return oldItem == newItem
        }
    }

    val differ: AsyncListDiffer<Cooking> = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaggeredHolder {
        binding = ItemLayout2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaggeredHolder(binding.root)

    }

    override fun onBindViewHolder(holder: StaggeredHolder, position: Int) {
        val cooking = differ.currentList[position]
        CoroutineScope(Dispatchers.Main).launch {

            holder.tvItemName.text = cooking.name
            Glide.with(holder.root.context).asBitmap().load(cooking.thumbnail_url).thumbnail()
                .placeholder(
                    R.drawable.ic_dummy_pizza
                ).apply(RequestOptions.centerCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView)

            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(cooking) }
            }
            holder.btnStaggeredSave.setOnClickListener {
                onItemSaveClickListener?.let { it(cooking) }
            }
        }


    }

    private var onItemSaveClickListener: ((Cooking) -> Unit)? = null

    fun setOnItemSaveClickListener(listener: (Cooking) -> Unit) {
        onItemSaveClickListener = listener
    }


    private var onItemClickListener: ((Cooking) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cooking) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class StaggeredHolder(itemView: View) : ViewHolder(itemView) {

        val imageView: ImageView = binding.imgInnerItems
        val tvItemName: TextView = binding.tvItemName
        val root: RelativeLayout = binding.staggeredContext
        val btnStaggeredSave = binding.btnStaggeredSave

    }
}