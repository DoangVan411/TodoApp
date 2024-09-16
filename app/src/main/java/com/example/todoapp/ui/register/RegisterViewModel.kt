package com.example.todoapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.User
import com.example.todoapp.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _registrationResult = MutableLiveData<Pair<Boolean, String>>()
    val registrationResult: LiveData<Pair<Boolean, String>> get() = _registrationResult

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            if (userRepository.isEmailExist(email)) {
                _registrationResult.value = Pair(false, "Email already exists")
            } else {
                userRepository.insertUser(User(0, name, email, password))
                _registrationResult.value = Pair(true, "Register sucessful")
            }
        }
    }

    class RegistrationViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}