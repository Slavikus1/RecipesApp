package ru.aliohin.recipesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.aliohin.recipesapp.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataset: List<Ingredient>) :
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

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    private fun calculateIngredients(ingredients: String): String {
        var calculate = 0.0
        calculate = (ingredients.toDouble() * quantity)
        if (calculate.toDouble() % 1.0 != 0.0) {
            return calculate.toString().format("%.1f")
        }
        else return calculate.toInt().toString()
    }

}