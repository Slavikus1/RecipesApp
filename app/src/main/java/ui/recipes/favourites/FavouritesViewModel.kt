package ui.recipes.favourites

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.RecipeRepository
import kotlinx.coroutines.launch
import model.Recipe
import ui.recipes.recipe.RecipeFragment.Companion.KEY_FAVOURITES_RECIPE
import ui.recipes.recipe.RecipeFragment.Companion.SHARED_PREFERENCES

class FavouritesViewModel(private val application: Application) : AndroidViewModel(application) {
    data class FavouritesState(
        var favouritesList: List<Recipe>? = null,
        var isShowError: Boolean = false
    )

    private val _favouritesState = MutableLiveData(FavouritesState())
    val favouritesState
        get() = _favouritesState

    fun loadFavouritesState() {
        viewModelScope.launch {
            val favourites = RecipeRepository.getInstance(application).getFavouritesRecipes()
            Log.i("!!!", "favourites - $favourites")
            if (favourites.isNotEmpty()) {
                _favouritesState.postValue(favouritesState.value?.copy(favouritesList = favourites))
            } else _favouritesState.postValue(
                favouritesState.value?.copy(
                    favouritesList = emptyList(),
                    isShowError = true,
                )
            )

        }
    }
}