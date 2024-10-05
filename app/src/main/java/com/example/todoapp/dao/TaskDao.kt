package com.example.todoapp.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.model.Task

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("DELETE FROM task WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Int)

    @Query("SELECT * FROM task")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTaskById(id: Int): LiveData<Task>
}