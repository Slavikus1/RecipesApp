package ui.recipes.recipe

import androidx.lifecycle.ViewModel
import model.Ingredient
import model.Recipe

class RecipeViewModel(): ViewModel() {

    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavourite: Boolean = false,
        val ingredients: List<Ingredient> = emptyList(),
        val method: List<String>,
    )
}