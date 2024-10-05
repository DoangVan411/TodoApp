package com.example.todoapp.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.databinding.FragmentRegisterBinding
import com.example.todoapp.repository.UserRepository

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModel.RegistrationViewModelFactory(UserRepository(AppDatabase.getDatabase(requireContext()).userDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up observer cho registerViewModel.registrationResult
        setupRegistrationResultObserver()

        //set up listener cho cac view
        binding.apply {
            tvLogin.setOnClickListener {
                findNavController().popBackStack()
            }

            btnRegister.setOnClickListener {
                register()
            }
        }
    }

    private fun setupRegistrationResultObserver() {
        registerViewModel.registrationResult.observe(viewLifecycleOwner) { result ->
            val (success, message) = result
            if (success) {
                showToast(message)
                findNavController().popBackStack()
            } else {
                showToast(message)
            }
        }
    }

    private fun register() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirm = binding.etConfirm.text.toString()

        if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty() && checkPassword(password, confirm)) {
            registerViewModel.register(name, email, password)
        }
    }

    private fun checkPassword(password: String, confirm: String): Boolean {
        if(password != confirm) {
            binding.etConfirm.error = "Passwords do not match"
            binding.etConfirm.requestFocus()
            return false
        }
        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}