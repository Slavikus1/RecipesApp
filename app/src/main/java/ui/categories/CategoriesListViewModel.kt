package ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.RecipeRepository
import model.Category

class CategoriesListViewModel : ViewModel() {
    data class CategoriesListState(
        var list: List<Category>? = null,
        var isShowError: Boolean = false,
    )

    private val _categoriesState = MutableLiveData(CategoriesListState())
    val categoriesState: LiveData<CategoriesListState>
        get() = _categoriesState

    fun loadCategories() {
        RecipeRepository.INSTANSE.getCategories { categories ->
            if (!categories.isNullOrEmpty()) {
                _categoriesState.postValue(CategoriesListState(list = categories))
            } else {
                _categoriesState.postValue(
                    CategoriesListState(
                        isShowError = true,
                        list = emptyList()
                    )
                )
            }
        }
    }
}