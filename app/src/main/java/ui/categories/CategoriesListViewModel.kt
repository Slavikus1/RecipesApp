package ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import data.RecipeRepository
import kotlinx.coroutines.launch
import model.Category

class CategoriesListViewModel(private val application: Application) :
    AndroidViewModel(application) {
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
                RecipeRepository.getInstance(application).getCategoriesFromCache()

            if (cachedCategories.isNotEmpty()) {
                _categoriesState.postValue(categoriesState.value?.copy(list = cachedCategories))
            } else {
                _categoriesState.postValue(categoriesState.value?.copy(isShowError = true))
            }

            val serverCategories = RecipeRepository.getInstance(application).getCategories()
            if (!serverCategories.isNullOrEmpty()) {
                _categoriesState.postValue(
                    categoriesState.value?.copy(
                        list = serverCategories,
                        isShowError = false
                    )
                )
                RecipeRepository.getInstance(application)
                    .insertCategoriesInDataBase(serverCategories)
            } else {
                _categoriesState.postValue(categoriesState.value?.copy(isShowError = true))
            }

        }
    }
}