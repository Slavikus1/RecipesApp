package ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.RecipeRepository
import kotlinx.coroutines.launch
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
        viewModelScope.launch {
            RecipeRepository.INSTANCE.getCategories { categories ->
                if (!categories.isNullOrEmpty()) {
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
}