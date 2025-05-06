package ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.RecipeRepository
import kotlinx.coroutines.launch
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

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    fun loadRecipe(recipe: Recipe) {
        viewModelScope.launch {
            val isFavourite = RecipeRepository.getInstance(application).getFavouritesRecipes()
                .contains(recipe)
            try {
                _recipeState.postValue(
                    recipeState.value?.copy(
                        recipe = recipe,
                        isFavourite = isFavourite,
                        numberOfPortions = 1,
                        recipeImageUrl = "${RecipeRepository.getInstance(application).loadImageUrl}${recipe.imageUrl}"
                    )
                )
            } catch (e: Exception) {
                Log.i("Recipe VM exception", "${e.message}")
                _recipeState.postValue(recipeState.value?.copy(isShowError = true))
            }
        }
    }

    internal fun onFavoritesClicked(recipe: Recipe) {
        _recipeState.value.let { state ->
            viewModelScope.launch {
                val favouritesSet = RecipeRepository.getInstance(application).getFavouritesRecipes()
                if (favouritesSet.contains(recipe)) {
                    val updatedRecipe = recipe.copy(isFavourite = false)
                    updateCacheFavourites(updatedRecipe)
                    _recipeState.postValue(recipeState.value?.copy(isFavourite = false))
                } else {
                    val updatedRecipe = recipe.copy(isFavourite = true)
                    updateCacheFavourites(updatedRecipe)
                    _recipeState.postValue(recipeState.value?.copy(isFavourite = true))
                }
                _recipeState.value = state?.copy(isFavourite = favouritesSet.contains(recipe))
            }
        }
    }

    fun updatePortionsCount(currentCount: Int) {
        _recipeState.value = _recipeState.value?.copy(numberOfPortions = currentCount)
    }

    private fun updateCacheFavourites(recipe: Recipe) {
        viewModelScope.launch {
            RecipeRepository.getInstance(application).updateFavouritesStatus(recipe)
        }
    }
}