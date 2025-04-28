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

    private val sharedPref: SharedPreferences by lazy {
        application.getSharedPreferences(
            SHARED_PREFERENCES, Context.MODE_PRIVATE
        )
    }

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val cachedRecipe =
                RecipeRepository.getInstance(application).getRecipeFromCacheById(recipeId)
            var isFavourite = getFavourites().contains(cachedRecipe.id.toString())
            try {
                _recipeState.postValue(
                    recipeState.value?.copy(
                        recipe = cachedRecipe,
                        isFavourite = isFavourite,
                        numberOfPortions = 1,
                        recipeImageUrl = "${RecipeRepository.getInstance(application).loadImageUrl}${cachedRecipe.imageUrl}"
                    )
                )
            } catch (e: Exception) {
                Log.i("Recipe VM exception", "${e.message}")
                _recipeState.postValue(recipeState.value?.copy(isShowError = true))
            }
            val loadedRecipe = RecipeRepository.getInstance(application).getRecipeById(recipeId)
            if (loadedRecipe != null) {
                RecipeRepository.getInstance(application).insertRecipe(loadedRecipe)
                isFavourite = getFavourites().contains(loadedRecipe.id.toString())
                _recipeState.postValue(
                    recipeState.value?.copy(
                        recipe = loadedRecipe,
                        isFavourite = isFavourite,
                        numberOfPortions = 1,
                        recipeImageUrl = "${RecipeRepository.getInstance(application).loadImageUrl}${loadedRecipe.imageUrl}",
                    )
                )
            } else _recipeState.postValue(recipeState.value?.copy(isShowError = true))

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