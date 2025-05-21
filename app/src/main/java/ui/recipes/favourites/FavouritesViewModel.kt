package ui.recipes.favourites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.RecipeRepository
import kotlinx.coroutines.launch
import model.Recipe
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(private val repository: RecipeRepository) :
    ViewModel() {
    data class FavouritesState(
        var favouritesList: List<Recipe>? = null,
        var isShowError: Boolean = false
    )

    private val _favouritesState = MutableLiveData(FavouritesState())
    val favouritesState
        get() = _favouritesState

    fun loadFavouritesState() {
        viewModelScope.launch {
            val favourites = repository.getFavouritesRecipes()
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