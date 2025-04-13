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
        val categoryImageUrl: String? = null,
        val categoryName: String? = null,
        var isShowError: Boolean = false,
    )

    private val _recipeState = MutableLiveData(RecipesListState())
    val recipeState
        get() = _recipeState

    fun loadRecipesListState(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        var imageUrl: String? = null

        if (categoryImageUrl != null) {
            imageUrl = categoryImageUrl
        }
        if (categoryId != null) {
            RecipeRepository.INSTANSE.getRecipesByCategoryId(categoryId){ recipes ->
                if (!recipes.isNullOrEmpty()){
                    _recipeState.postValue(recipeState.value?.copy(
                        listOfRecipes = recipes,
                        categoryImageUrl = "${RecipeRepository.INSTANSE.loadImageUrl}$imageUrl",
                        categoryName = categoryName,
                    ))
                }
                else {
                    _recipeState.postValue(recipeState.value?.copy(isShowError = true))
                }

            }
        }
        else Log.i("!!!", "Category id must not be null")
    }
}