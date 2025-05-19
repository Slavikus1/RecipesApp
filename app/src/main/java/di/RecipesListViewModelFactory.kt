package di

import data.RecipeRepository
import ui.recipes.recipesList.RecipesListViewModel

class RecipesListViewModelFactory(private val repository: RecipeRepository) :
    Factory<RecipesListViewModel> {
    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(repository)
    }
}