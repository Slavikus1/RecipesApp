package ru.aliohin.recipesapp

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.aliohin.recipesapp.databinding.ItemRecipeBinding

class RecipesListAdapter(private val dataset: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.RecipeHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    class RecipeHolder(binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        var titleTextView = binding.tvRecipeName
        var imageView = binding.ivRecipeLogo
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RecipeHolder {
        val binding =
            ItemRecipeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return RecipeHolder(binding)
    }

    override fun getItemCount() = dataset.size

    @SuppressLint("StringFormatInvalid")
    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        val recipe: Recipe = dataset[position]
        holder.titleTextView.text = recipe.title
        holder.imageView.contentDescription =
            holder.itemView.context.getString(R.string.iV_Recipes_list_description, recipe.title)
        val drawable =
            try {
                Drawable.createFromStream(
                    holder.itemView.context.assets.open(recipe.imageUrl),
                    null
                )
            } catch (e: Exception) {
                Log.d("!!!", "Image not found ${recipe.imageUrl}")
                null
            }
        holder.imageView.setImageDrawable(drawable)
        holder.imageView.setOnClickListener { itemClickListener?.onItemClick(recipe.id) }
    }
}