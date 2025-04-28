package ui.categories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import data.RecipeRepository
import model.Category
import ru.aliohin.recipesapp.R
import ru.aliohin.recipesapp.databinding.ItemCategoryBinding

class CategoryListAdapter(private var dataset: List<Category>) :
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
        Glide.with(viewHolder.imageView.context)
            .load("${RecipeRepository.getInstance(viewHolder.imageView.context).loadImageUrl}${category.imageUrl}")
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(viewHolder.imageView)
        viewHolder.root.setOnClickListener { itemClickListener?.onItemClick(category.id) }
        viewHolder.imageView.contentDescription =
            viewHolder.itemView.context.getString(
                R.string.iV_category_list_description,
                category.title
            )
    }

    override fun getItemCount() = dataset.size

    fun updateDataSet(newSet: List<Category>) {
        dataset = newSet
        notifyDataSetChanged()
    }
}