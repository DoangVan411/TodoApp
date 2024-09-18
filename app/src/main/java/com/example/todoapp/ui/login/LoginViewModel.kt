package com.example.todoapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.User
import com.example.todoapp.repository.UserRepository
import com.example.todoapp.utils.Security
import kotlinx.coroutines.launch

class LoginViewModel (private val userRepository: UserRepository): ViewModel() {
    private val _loginResult = MutableLiveData<User?>()
    val loginResult: LiveData<User?> get() = _loginResult

    fun login(email:String, password: String) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            if(user != null) {
                val hashedPassword = Security.hashPassword(password, user.salt)
                if (user.password == hashedPassword) {
                    _loginResult.value = user
                } else {
                    _loginResult.value = null
                }
            }
            else {
                _loginResult.value = null
            }
        }
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }

    class LoginViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
         override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}