package ru.aliohin.recipesapp

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.aliohin.recipesapp.databinding.ItemCategoryBinding

class CategoryListAdapter(private val dataset: List<Category>) :
    RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.ivCategoryLogo
        val titleTextView: TextView = binding.tvCategoryName
        val descriptionTextView: TextView = binding.tvCategoryDescription
        val root = binding.root
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category: Category = dataset[position]
        viewHolder.titleTextView.text = category.title
        viewHolder.descriptionTextView.text = category.description
        val drawable =
            try {
                Drawable.createFromStream(
                    viewHolder.itemView.context.assets.open(category.imageUrl),
                    null
                )
            } catch (e: Exception) {
                Log.d("!!!", "Image not found ${category.imageUrl}")
                null
            }
        viewHolder.imageView.setImageDrawable(drawable)
        viewHolder.root.setOnClickListener { itemClickListener?.onItemClick(category.id) }
        viewHolder.imageView.contentDescription =
            viewHolder.itemView.context.getString(R.string.iV_category_list_description, category.title)
    }

    override fun getItemCount() = dataset.size
}