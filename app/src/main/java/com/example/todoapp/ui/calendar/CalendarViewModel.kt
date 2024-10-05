package com.example.todoapp.ui.calendar

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.model.Category
import com.example.todoapp.model.Task
import com.example.todoapp.repository.CategoryRepository
import com.example.todoapp.repository.TaskRepository

class CalendarViewModel(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
    context: Context
):ViewModel() {

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

    // LiveData chứa danh sách task sau khi lọc theo ngày
    private val _dateTask = MutableLiveData<List<Task>>()
    val dateTask: LiveData<List<Task>> = _dateTask

    fun getTasksByDateRange(startOfDay: Long, endOfDay: Long) {
        // Lấy danh sách task hiện tại từ LiveData
        val tasks = taskList.value
        if (tasks != null) {
            val filteredTasks = tasks.filter { it.dueDate.time in startOfDay..endOfDay}
            _dateTask.value = filteredTasks
        }
    }

    class CalendarViewModelFactory(
        private val taskRepository: TaskRepository,
        private val categoryRepository: CategoryRepository,
        private val context: Context
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CalendarViewModel(taskRepository, categoryRepository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}