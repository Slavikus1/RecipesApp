package ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.STUB
import model.Category

class CategoriesListViewModel : ViewModel() {
    data class CategoriesListState(
        var list: List<Category>? = null,
    )

    fun loadCategories() {
        val categories = STUB.getCategories()
        _categoriesState.value?.list = categories
    }

    private val _categoriesState = MutableLiveData(CategoriesListState())
    val categoriesState: LiveData<CategoriesListState>
        get() = _categoriesState
}