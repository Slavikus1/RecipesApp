package ui.recipes.favourites

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import data.STUB
import model.Recipe
import ui.recipes.recipe.RecipeFragment.Companion.KEY_FAVOURITES_RECIPE
import ui.recipes.recipe.RecipeFragment.Companion.SHARED_PREFERENCES

class FavouritesViewModel(private val application: Application) : AndroidViewModel(application) {
    data class FavouritesState(
        var favouritesList: List<Recipe>? = null,
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
        val favouritesIds = getFavourites(shredPreferences)
        val recipes: List<Recipe> = STUB.getRecipesByIds(favouritesIds)
        _favouritesState.value?.favouritesList = recipes
    }

    private fun getFavourites(sharedPref: SharedPreferences): MutableSet<String> {
        val newSet = sharedPref.getStringSet(KEY_FAVOURITES_RECIPE, setOf()) ?: setOf()
        return HashSet(newSet)
    }
}