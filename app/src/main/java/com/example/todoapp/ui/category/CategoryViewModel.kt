package com.example.todoapp.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.model.Category
import com.example.todoapp.repository.CategoryRepository

class CategoryViewModel(
    private val categoryRepository: CategoryRepository
): ViewModel() {

    // Lấy danh sách Category từ CategoryRepository
    val categoryList: LiveData<List<Category>> = categoryRepository.getAllCategories()

    class CategoryViewModelFactory(
        private val categoryRepository: CategoryRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CategoryViewModel(categoryRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}