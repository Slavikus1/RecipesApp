package ru.aliohin.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryListAdapter(private val dataset: List<Category>) :
    RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.ivCategoryLogo)
        val titleTextView: TextView = view.findViewById(R.id.tvCategoryName)
        val descriptionTextView: TextView = view.findViewById(R.id.tvCategoryDescription)
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_category, viewGroup, false)
        return ViewHolder(view)
    }

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
    }

    override fun getItemCount() = dataset.size
}