package com.example.todoapp.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Category
import com.example.todoapp.model.Task
import com.example.todoapp.repository.CategoryRepository
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.home.insert.InsertViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
    context: Context
): ViewModel() {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    private val _categoryColor = MutableLiveData<Map<Int, Int>>()
    val categoryColor: LiveData<Map<Int, Int>> get() = _categoryColor

    fun updateCategoryColors(categories: List<Category>) {
        if (categories.isNotEmpty()) {
            _categoryColor.value = categories.associate { it.id to it.color }
        }
    }

    // Lấy danh sách Task từ TaskRepository
    val taskList: LiveData<List<Task>> = taskRepository.getAllTasks()

    // Lấy danh sách Category từ CategoryRepository
    val categoryList: LiveData<List<Category>> = categoryRepository.getAllCategories()

    // Hàm kiểm tra và chèn các category mặc định chỉ một lần
    fun insertDefaultCategories() {
        val hasInsertedDefaultCategories = sharedPreferences.getBoolean("inserted_default_categories", false)

        if (!hasInsertedDefaultCategories) {
            viewModelScope.launch {
                val defaultCategories = categoryRepository.createDefaultCategories()
                categoryRepository.insertDefaultCategories(defaultCategories)
                sharedPreferences.edit().putBoolean("inserted_default_categories", true).apply()
            }
        }
    }

    class HomeViewModelFactory(
        private val taskRepository: TaskRepository,
        private val categoryRepository: CategoryRepository,
        private val context: Context
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(taskRepository, categoryRepository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}