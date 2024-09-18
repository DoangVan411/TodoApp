package com.example.todoapp.repository

import com.example.todoapp.dao.UserDao
import com.example.todoapp.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}