package com.example.todoapp.ui.home.insert

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Category
import com.example.todoapp.model.Status
import com.example.todoapp.model.Task
import com.example.todoapp.repository.CategoryRepository
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.login.LoginViewModel
import com.example.todoapp.utils.Constant
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.Calendar

class InsertViewModel(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
): ViewModel() {

    private val _insertResult = MutableLiveData<Pair<Boolean, String>>()
    val insertResult get() = _insertResult

    // Lấy danh sách Category từ CategoryRepository
    val categoryList: LiveData<List<Category>> = categoryRepository.getAllCategories()

    fun insertTask(
        userId: Int,
        title: String?,
        description: String?,
        importance: Boolean?,
        dueTime: Long?,
        category: Int
    ) {
        if (title.isNullOrEmpty() || description.isNullOrEmpty() || dueTime == null || importance == null || category == null) {
            _insertResult.value = Pair(false, Constant.FILL_ALL_FIELDS)
            return
        }

        val task = Task(
            userId = userId,
            title = title,
            description = description,
            importance = importance,
            dueDate = Timestamp(dueTime),
            category = category,
            status = Status.ON_GOING
        )

        viewModelScope.launch {
            taskRepository.insertTask(task)
        }
        _insertResult.value = Pair(true, Constant.INSERT_SUCCESSFUL)
    }



    class InsertViewModelFactory(
        private val taskRepository: TaskRepository,
        private val categoryRepository: CategoryRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InsertViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InsertViewModel(taskRepository, categoryRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}