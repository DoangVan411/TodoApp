package com.example.todoapp.ui.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.model.Category
import com.example.todoapp.model.Task
import com.example.todoapp.repository.CategoryRepository
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.home.HomeViewModel

class SearchViewModel(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
    context: Context
): ViewModel() {
    private val _originalTaskList = MutableLiveData<List<Task>>()
    private val _taskList = MediatorLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> get() = _taskList

    init {
        _taskList.addSource(taskRepository.getAllTasks()) { tasks ->
            _originalTaskList.value = tasks
            _taskList.value = tasks
        }
    }

    private val _categoryColor = MutableLiveData<Map<Int, Int>>()
    val categoryColor: LiveData<Map<Int, Int>> get() = _categoryColor

    // Lấy danh sách Category từ CategoryRepository
    val categoryList: LiveData<List<Category>> = categoryRepository.getAllCategories()

    fun updateCategoryColors(categories: List<Category>) {
        if (categories.isNotEmpty()) {
            _categoryColor.value = categories.associate { it.id to it.color }
        }
    }

    fun searchTasks(keyword: String) {
        val allTasks = _originalTaskList.value ?: listOf()
        val filteredTasks = allTasks.filter {task ->
            task.title.contains(keyword, ignoreCase = false) || task.description.contains(keyword, ignoreCase = false)
        }
        _taskList.value = filteredTasks
    }

    class SearchViewModelFactory(
        private val taskRepository: TaskRepository,
        private val categoryRepository: CategoryRepository,
        private val context: Context
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(taskRepository, categoryRepository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}