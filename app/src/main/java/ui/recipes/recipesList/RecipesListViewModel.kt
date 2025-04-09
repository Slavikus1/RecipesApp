package ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import data.RecipeRepository

import model.Recipe

class RecipesListViewModel(private val application: Application) : AndroidViewModel(application) {

    data class RecipesListState(
        val listOfRecipes: List<Recipe>? = null,
        val categoryImage: Drawable? = null,
        val categoryName: String? = null,
        var isShowError: Boolean = false,
    )

    private val _recipeState = MutableLiveData(RecipesListState())
    val recipeState
        get() = _recipeState

    fun loadRecipesListState(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        var drawable: Drawable? = null
        if (categoryImageUrl != null) {
            drawable = loadImageFromAssets(categoryImageUrl)
        }
        if (categoryId != null) {
            RecipeRepository.INSTANSE.getRecipesByCategoryId(categoryId){ recipes ->
                if (!recipes.isNullOrEmpty()){
                    _recipeState.postValue(RecipesListState(
                        listOfRecipes = recipes,
                        categoryImage = drawable,
                        categoryName = categoryName,
                    ))
                }
                else {
                    _recipeState.postValue(RecipesListState(isShowError = true))
                }

            }
        }
        else Log.i("!!!", "Category id must not be null")
    }

    private fun loadImageFromAssets(categoryImageUrl: String): Drawable? {
        return try {
            application.assets.open(categoryImageUrl).use { stream ->
                Drawable.createFromStream(stream, null)
            }
        } catch (e: Exception) {
            Log.e("RecipeViewModel", "Error loading image for $categoryImageUrl")
            null
        }
    }
}