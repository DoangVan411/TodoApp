package com.example.todoapp.ui.home.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepository
import kotlinx.coroutines.launch

class EditViewModel(private val repository: TaskRepository): ViewModel()  {

    fun getTaskById(taskId: Int, callback: (Task?) -> Unit) {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId)
            callback(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    class EditViewModelFactory(private val taskRepository: TaskRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
                return EditViewModel(taskRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}