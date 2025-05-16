package di

import data.RecipeRepository
import ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(private val repository: RecipeRepository): Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(repository)
    }
}