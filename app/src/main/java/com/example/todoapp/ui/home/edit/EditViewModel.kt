package com.example.todoapp.ui.home.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Status
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.utils.Constant
import kotlinx.coroutines.launch
import java.sql.Timestamp

class EditViewModel(private val repository: TaskRepository): ViewModel()  {

    private val _updateResult = MutableLiveData<Pair<Boolean, String>>()
    val updateResult get() = _updateResult

    fun getTaskById(taskId: Int): LiveData<Task>{
        return repository.getTaskById(taskId)
    }

    fun updateTask(
        id: Int,
        title: String?,
        description: String?,
        importance: Boolean?,
        dueTime: Long?,
        category: Int,
        status: Status
    ) {
        if (title.isNullOrEmpty() || description.isNullOrEmpty() || dueTime == null || importance == null) {
            _updateResult.value = Pair(false, Constant.FILL_ALL_FIELDS)
            return
        }

        val task = Task(
            id = id,
            title = title,
            description = description,
            importance = importance,
            dueDate = Timestamp(dueTime),
            category = category,
            status = status
        )

        viewModelScope.launch {
            repository.updateTask(task)
        }
        _updateResult.value = Pair(true, Constant.UPDATE_SUCCESSFUL)

    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            repository.deleteTaskById(id)
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