package com.example.todoapp.ui.category.insert

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentImagePickerBinding
import com.example.todoapp.ui.adapters.ImagePickerAdapter

class ImagePickerFragment: DialogFragment() {

    private var _binding: FragmentImagePickerBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagePickerBinding.inflate(inflater, container, false)
        val view = binding.root

        val imageList = listOf(
            R.drawable.work,
            R.drawable.person,
            R.drawable.shopping,
            R.drawable.fitness,
            R.drawable.book,
            R.drawable.music,
            R.drawable.sport,
            R.drawable.entertain
        )

        val adapter = ImagePickerAdapter(imageList) { iv ->
            val result = Bundle()
            result.putInt("selectedIconId", iv)
            setFragmentResult("iconRequestKey", result)
            dismiss()
        }

        binding.rvIcon.adapter = adapter
        binding.rvIcon.layoutManager = GridLayoutManager(requireContext(), 3)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}