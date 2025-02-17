package ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import model.Ingredient
import ru.aliohin.recipesapp.databinding.ItemIngredientBinding
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(var dataset: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientHolder>() {
    private var quantity = 1

    class IngredientHolder(binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {
        var ingredientTextView = binding.tvIngredient
        var quantityTextView = binding.tvQuantity
        var unitOfMeasureTextView = binding.tvUnitOfMeasure
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientHolder {
        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientHolder, position: Int) {
        val ingredient = dataset[position]
        holder.ingredientTextView.text = ingredient.description
        holder.quantityTextView.text = calculateIngredients(ingredient.quantity)
        holder.unitOfMeasureTextView.text = ingredient.unitOfMeasure
    }

    override fun getItemCount(): Int = dataset.size

    fun updateDataset(newSet: List<Ingredient>){
        dataset = newSet
        notifyDataSetChanged()
    }

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    private fun calculateIngredients(ingredients: String): String {
        val totalQuantity = BigDecimal(ingredients) * BigDecimal(quantity)

        val displayQuantity = totalQuantity
            .setScale(1, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()

        return displayQuantity
    }

}