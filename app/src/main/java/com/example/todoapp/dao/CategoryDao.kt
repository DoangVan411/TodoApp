package com.example.todoapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todoapp.model.Category

@Dao
interface CategoryDao {
    @Insert
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM category")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM category WHERE id = :id")
    fun getCategoryById(id: Int): Category?

    @Insert
    suspend fun insertCategories(categories: List<Category>)
}