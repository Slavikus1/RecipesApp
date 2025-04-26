package ui.categories

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.RecipeRepository
import kotlinx.coroutines.launch
import model.Category

class CategoriesListViewModel(private val application: Application) : ViewModel() {
    data class CategoriesListState(
        var list: List<Category>? = null,
        var isShowError: Boolean = false,
    )

    private val _categoriesState = MutableLiveData(CategoriesListState())
    val categoriesState: LiveData<CategoriesListState>
        get() = _categoriesState

    fun loadCategories() {
        viewModelScope.launch {
            var categories: List<Category>? =
                RecipeRepository.getInstance(application).getCategoriesFromCache()
            if (categories != null && categories.isEmpty()) {
                categories = RecipeRepository.getInstance(application).getCategories()
            }
            if (!categories.isNullOrEmpty()) {
                RecipeRepository.getInstance(application).insertCategoriesInDataBase(categories)
                _categoriesState.postValue(categoriesState.value?.copy(list = categories))
            } else {
                _categoriesState.postValue(
                    categoriesState.value?.copy(
                        isShowError = true,
                        list = emptyList()
                    )
                )
            }

        }
    }
}