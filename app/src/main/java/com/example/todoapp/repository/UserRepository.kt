package com.example.todoapp.repository

import com.example.todoapp.dao.UserDao
import com.example.todoapp.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
    suspend fun getUser(email: String, password: String) = userDao.getUser(email, password)
    suspend fun isEmailExist(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }
}