package ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import data.STUB
import model.Recipe
import ui.recipes.recipe.RecipeFragment.Companion.KEY_FAVOURITES_RECIPE

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {

    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavourite: Boolean = false,
        val numberOfPortions: Int = 1,
        val recipeImage: Drawable? = null,
    )

    private val sharedPref = getApplication<Application>().getSharedPreferences(
        KEY_FAVOURITES_RECIPE, Context.MODE_PRIVATE
    )

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    fun loadRecipe(recipeId: Int): Recipe {
        val recipe = STUB.getRecipeById(recipeId)
        if (recipe != null) {
            val isFavourite = getFavourites(sharedPref).contains(recipe.id.toString())
            val drawable = loadImageFromAssets(recipe.imageUrl)
            _recipeState.value = _recipeState.value?.copy(
                recipe = recipe,
                isFavourite = isFavourite,
                numberOfPortions = 1,
                recipeImage = drawable,
            )
        }
        TODO("load from network")
    }

    private fun loadImageFromAssets(recipeImageUrl: String): Drawable? {
        return try {
            application.assets.open(recipeImageUrl).use { stream ->
                Drawable.createFromStream(stream, null)
            }
        } catch (e: Exception) {
            Log.e("RecipeViewModel", "Error loading image for $recipeImageUrl")
            null
        }
    }

    fun getFavourites(sharedPref: SharedPreferences): MutableSet<String> {
        val newSet = sharedPref.getStringSet(KEY_FAVOURITES_RECIPE, setOf()) ?: setOf()
        return HashSet(newSet)
    }

    fun saveFavourites(sharedPref: SharedPreferences, favourites: MutableSet<String>) {
        sharedPref.edit()?.putStringSet(KEY_FAVOURITES_RECIPE, favourites)?.apply()
    }

    fun onFavoritesClicked() {
        _recipeState.value =
            _recipeState.value?.copy(isFavourite = !(_recipeState.value?.isFavourite ?: false))
    }
}