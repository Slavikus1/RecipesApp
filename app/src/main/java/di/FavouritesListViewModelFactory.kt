package di

import data.RecipeRepository
import ui.recipes.favourites.FavouritesViewModel

class FavouritesListViewModelFactory(private val repository: RecipeRepository) :
    Factory<FavouritesViewModel> {
    override fun create(): FavouritesViewModel {
        return FavouritesViewModel(repository)
    }
}