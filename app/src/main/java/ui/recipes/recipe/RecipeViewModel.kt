package ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import data.RecipeRepository
import model.Recipe
import ui.recipes.recipe.RecipeFragment.Companion.KEY_FAVOURITES_RECIPE
import ui.recipes.recipe.RecipeFragment.Companion.SHARED_PREFERENCES

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {

    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavourite: Boolean = false,
        val numberOfPortions: Int = 1,
        val recipeImageUrl: String? = null,
        var isShowError: Boolean = false,
    )

    private val sharedPref: SharedPreferences by lazy { application.getSharedPreferences(
        SHARED_PREFERENCES, Context.MODE_PRIVATE) }

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    fun loadRecipe(recipeId: Int) {
        RecipeRepository.INSTANSE.getRecipeById(recipeId){ recipe: Recipe? ->
            if (recipe != null){
                val isFavourite = getFavourites().contains(recipe.id.toString())
                _recipeState.postValue(recipeState.value?.copy(
                    recipe = recipe,
                    isFavourite = isFavourite,
                    numberOfPortions = 1,
                    recipeImageUrl = "${RecipeRepository.INSTANSE.loadImageUrl}${recipe.imageUrl}",
                ))
            }
            else _recipeState.postValue(recipeState.value?.copy(isShowError = true))
        }
    }

    private fun getFavourites(): MutableSet<String> {
        val newSet = sharedPref.getStringSet(KEY_FAVOURITES_RECIPE, setOf()) ?: setOf()
        return HashSet(newSet)
    }

    private fun saveFavourites(favourites: MutableSet<String>) {
        sharedPref.edit()?.putStringSet(KEY_FAVOURITES_RECIPE, favourites)?.apply()
    }

    internal fun onFavoritesClicked(recipeId: String) {
        _recipeState.value.let { state ->
            val favouritesSet = getFavourites()
            if (favouritesSet.contains(recipeId)) favouritesSet.remove(recipeId)
            else favouritesSet.add(recipeId)
            saveFavourites(favouritesSet)
            _recipeState.value = state?.copy(isFavourite = favouritesSet.contains(recipeId))
        }
    }

    fun updatePortionsCount(currentCount: Int) {
        _recipeState.value = _recipeState.value?.copy(numberOfPortions = currentCount)
    }
}