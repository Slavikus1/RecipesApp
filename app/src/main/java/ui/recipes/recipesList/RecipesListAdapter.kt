package ui.recipes.recipesList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import data.RecipeRepository
import ru.aliohin.recipesapp.R
import model.Recipe
import ru.aliohin.recipesapp.databinding.ItemRecipeBinding

class RecipesListAdapter(private var dataset: List<Recipe>) :
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
        val root = binding.root
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
        Glide.with(holder.imageView.context)
            .load("${RecipeRepository.getInstance(holder.imageView.context).loadImageUrl}${recipe.imageUrl}")
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(holder.imageView)
        holder.root.setOnClickListener { itemClickListener?.onItemClick(recipe.id) }
    }

    fun updateData(newData: List<Recipe>) {
        dataset = newData
        notifyDataSetChanged()
    }
}