package ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import data.STUB
import model.Recipe
import ui.recipes.recipe.RecipeFragment.Companion.KEY_FAVOURITES_RECIPE

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipeState(
        var recipe: Recipe? = null,
        var isFavourite: Boolean = false,
        var numberOfPortions: Int = 1,
    )

    private val sharedPref = getApplication<Application>().getSharedPreferences(
        KEY_FAVOURITES_RECIPE, Context.MODE_PRIVATE
    )

    private val _recipeState = MutableLiveData<RecipeState>().apply {
        value = RecipeState()
    }
    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    fun loadRecipe(recipeId: Int): Recipe {
        val recipe = STUB.getRecipeById(recipeId)
        if (recipe != null) {
            _recipeState.value?.isFavourite =
                getFavourites(sharedPref).contains(recipe.id.toString())
            _recipeState.value?.recipe = recipe
            _recipeState.value?.numberOfPortions = 1
        }
        TODO("load from network")
    }

    fun getFavourites(sharedPref: SharedPreferences): MutableSet<String> {
        val newSet = sharedPref.getStringSet(KEY_FAVOURITES_RECIPE, setOf()) ?: setOf()
        return HashSet(newSet)
    }

    fun saveFavourites(sharedPref: SharedPreferences, favourites: MutableSet<String>) {
        sharedPref.edit()?.putStringSet(KEY_FAVOURITES_RECIPE, favourites)?.apply()
    }

    fun onFavoritesClicked() {
        _recipeState.value = _recipeState.value?.copy(isFavourite = !(_recipeState.value?.isFavourite ?: false))
    }
}