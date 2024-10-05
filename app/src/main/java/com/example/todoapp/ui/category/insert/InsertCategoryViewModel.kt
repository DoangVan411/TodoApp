package com.example.todoapp.ui.category.insert

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Category
import com.example.todoapp.repository.CategoryRepository
import com.example.todoapp.ui.category.CategoryViewModel
import com.example.todoapp.utils.Constant
import kotlinx.coroutines.launch

class InsertCategoryViewModel(private val categoryRepository: CategoryRepository): ViewModel() {

    private val _insertResult = MutableLiveData<Pair<Boolean, String>>()
    val insertResult get() = _insertResult

    fun insertCategory(
        title: String?,
        icon: Int?,
        color: Int?
    ) {
        if(title.isNullOrEmpty() || icon == null || color == null) {
            _insertResult.value = Pair(false, Constant.FILL_ALL_FIELDS)
            return
        }
        val category = Category(0, title, icon, color)
        viewModelScope.launch {
            categoryRepository.insertCategory(category)
        }
        _insertResult.value = Pair(true, Constant.INSERT_SUCCESSFUL)
    }

    class InsertCategoryViewModelFactory(
        private val categoryRepository: CategoryRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InsertCategoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InsertCategoryViewModel(categoryRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}