package ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.RecipeRepository
import kotlinx.coroutines.launch
import model.Recipe


class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {

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
            val isFavourite = repository.getFavouritesRecipes()
                .contains(recipe)
            try {
                _recipeState.postValue(
                    recipeState.value?.copy(
                        recipe = recipe,
                        isFavourite = isFavourite,
                        numberOfPortions = 1,
                        recipeImageUrl = "${repository.loadImageUrl}${recipe.imageUrl}"
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
                val favouritesSet = repository.getFavouritesRecipes()
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
            repository.updateFavouritesStatus(recipe)
        }
    }
}