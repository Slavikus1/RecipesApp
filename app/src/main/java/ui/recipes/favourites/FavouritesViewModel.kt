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

    private val shredPreferences by lazy {
        application.getSharedPreferences(
            SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    private val _favouritesState = MutableLiveData(FavouritesState())
    val favouritesState
        get() = _favouritesState

    fun loadFavouritesState() {
        viewModelScope.launch {
            val favouritesIds = getFavourites(shredPreferences)
            Log.i("!!!", "favourites - $favouritesIds")
            val recipes = RecipeRepository.INSTANCE.getRecipesByIds(favouritesIds)
            if (!recipes.isNullOrEmpty()) {
                _favouritesState.postValue(favouritesState.value?.copy(favouritesList = recipes))
            } else _favouritesState.postValue(
                favouritesState.value?.copy(
                    favouritesList = emptyList(),
                    isShowError = true,
                )
            )

        }
    }

    private fun getFavourites(sharedPref: SharedPreferences): String {
        val newSet = sharedPref.getStringSet(KEY_FAVOURITES_RECIPE, setOf()) ?: setOf()
        return newSet.joinToString(",")
    }
}