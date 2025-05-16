package di

import data.RecipeRepository
import ui.categories.CategoriesListViewModel

class CategoryListViewModelFactory(private val recipeRepository: RecipeRepository) :
    Factory<CategoriesListViewModel> {
    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipeRepository)
    }
}