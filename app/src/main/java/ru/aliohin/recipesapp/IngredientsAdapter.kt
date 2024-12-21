package ru.aliohin.recipesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.aliohin.recipesapp.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataset: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientHolder>() {
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
        val ingredient = dataset [position]
        holder.ingredientTextView.text = ingredient.description
        holder.quantityTextView.text = ingredient.quantity
        holder.unitOfMeasureTextView.text = ingredient.unitOfMeasure
    }

    override fun getItemCount(): Int = dataset.size


}