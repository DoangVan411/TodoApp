package com.example.todoapp.repository

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.example.todoapp.R
import com.example.todoapp.dao.CategoryDao
import com.example.todoapp.model.Category

class CategoryRepository(private val categoryDao: CategoryDao) {
    suspend fun insertCategory(category: Category) {
        categoryDao.insert(category)
    }

    fun getAllCategories(): LiveData<List<Category>> {
        return categoryDao.getAllCategories()
    }

    fun getCategoryById(id: Int): Category?{
        return categoryDao.getCategoryById(id)
    }

    suspend fun insertDefaultCategories(categories: List<Category>) {
        categoryDao.insertCategories(categories)
    }

    fun createDefaultCategories(): List<Category> {
        return listOf(
            Category(0, "Work", R.drawable.work, R.color.work_color),
            Category(0, "Personal", R.drawable.person, R.color.personal_color),
            Category(0, "Shopping", R.drawable.shopping, R.color.shopping_color),
            Category(0, "Fitness", R.drawable.fitness, R.color.fitness_color)
        )
    }
}