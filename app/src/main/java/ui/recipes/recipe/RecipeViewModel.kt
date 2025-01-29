package ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Recipe

class RecipeViewModel(): ViewModel() {

    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavourite: Boolean = false,
        val numberOfPortions: Int = 1,
    )

    private val _recipeState = MutableLiveData<RecipeState>().apply {
        value = RecipeState()
    }
    val recipeState: LiveData<RecipeState>
        get() = _recipeState
}