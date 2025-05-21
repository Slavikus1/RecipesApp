package ui.recipes.recipesList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.BASE_IMAGE_URL
import data.RecipeRepository
import kotlinx.coroutines.launch

import model.Recipe
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor (private val repository: RecipeRepository) : ViewModel() {

    data class RecipesListState(
        val listOfRecipes: List<Recipe>? = null,
        val categoryImageUrl: String? = null,
        val categoryName: String? = null,
        var isShowError: Boolean = false,
    )

    private val _recipeState = MutableLiveData(RecipesListState())
    val recipeState
        get() = _recipeState

    val myRepository = repository

    fun loadRecipesListState(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        viewModelScope.launch {
            var imageUrl: String? = null
            if (categoryImageUrl != null) {
                imageUrl = categoryImageUrl
            }
            if (categoryId != null) {
                val cachedRecipes = repository.getRecipesFromCacheByCategoryId(categoryId)
                if (cachedRecipes.isNotEmpty()) {
                    _recipeState.postValue(
                        recipeState.value?.copy(
                            listOfRecipes = cachedRecipes,
                            categoryImageUrl = "${BASE_IMAGE_URL}$imageUrl",
                            categoryName = categoryName,
                        )
                    )
                }
                val recipes =
                    repository.getRecipesByCategoryId(categoryId)
                if (!recipes.isNullOrEmpty()) {
                    repository.insertRecipesList(recipes)
                    _recipeState.postValue(
                        recipeState.value?.copy(
                            listOfRecipes = recipes,
                            categoryImageUrl = "${BASE_IMAGE_URL}$imageUrl",
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