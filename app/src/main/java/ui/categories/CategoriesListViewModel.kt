package ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.RecipeRepository
import kotlinx.coroutines.launch
import model.Category

class CategoriesListViewModel(
    private val recipeRepository: RecipeRepository,
) :
    ViewModel() {
    data class CategoriesListState(
        var list: List<Category>? = null,
        var isShowError: Boolean = false,
    )

    private val _categoriesState = MutableLiveData(CategoriesListState())
    val categoriesState: LiveData<CategoriesListState>
        get() = _categoriesState

    fun loadCategories() {
        viewModelScope.launch {
            val cachedCategories =
                recipeRepository.getCategoriesFromCache()

            if (cachedCategories.isNotEmpty()) {
                _categoriesState.postValue(categoriesState.value?.copy(list = cachedCategories))
            } else {
                _categoriesState.postValue(categoriesState.value?.copy(isShowError = true))
            }

            val serverCategories = recipeRepository.getCategories()
            if (!serverCategories.isNullOrEmpty()) {
                _categoriesState.postValue(
                    categoriesState.value?.copy(
                        list = serverCategories,
                        isShowError = false
                    )
                )
                recipeRepository.insertCategoriesInDataBase(serverCategories)
            } else {
                _categoriesState.postValue(categoriesState.value?.copy(isShowError = true))
            }

        }
    }
}