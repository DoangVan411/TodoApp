package com.example.todoapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.databinding.FragmentLoginBinding
import com.example.todoapp.repository.UserRepository

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModel.LoginViewModelFactory(UserRepository(AppDatabase.getDatabase(requireContext()).userDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up observer cho loginViewModel.loginResult
        setupLoginResultObserver()

        //set up listener cho cac view
        binding.apply {
            tvRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            btnLogin.setOnClickListener {
                login()
            }
        }

    }

    private fun setupLoginResultObserver() {
        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {user ->
            if(user != null) {
                showToast("Login successful")
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
            else {
                showToast("Login failed, please check your information again!")
            }
        })
    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            loginViewModel.login(email, password)
        } else {
            showToast("Fields cannot be blank.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}