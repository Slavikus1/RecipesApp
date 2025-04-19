package ui.recipes.recipesList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.RecipeRepository
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            var imageUrl: String? = null
            if (categoryImageUrl != null) {
                imageUrl = categoryImageUrl
            }
            if (categoryId != null) {
                val recipes = RecipeRepository.INSTANCE.getRecipesByCategoryId(categoryId)
                if (!recipes.isNullOrEmpty()) {
                    _recipeState.postValue(
                        recipeState.value?.copy(
                            listOfRecipes = recipes,
                            categoryImageUrl = "${RecipeRepository.INSTANCE.loadImageUrl}$imageUrl",
                            categoryName = categoryName,
                        )
                    )
                } else {
                    _recipeState.postValue(recipeState.value?.copy(isShowError = true))
                }
            } else Log.i("!!!", "Category id must not be null")
        }
    }
}