package com.example.todoapp.ui.category.insert

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.databinding.FragmentInsertCategoryBinding
import com.example.todoapp.repository.CategoryRepository
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.adapters.ColorAdapter
import com.example.todoapp.ui.adapters.ImagePickerAdapter
import com.example.todoapp.ui.home.insert.InsertViewModel

class InsertCategoryFragment : DialogFragment() {

    private var _binding: FragmentInsertCategoryBinding? = null
    private val binding get() = _binding!!

    private var selectedColor: Int? = null
    private var selectedIcon: Int? = null


    private val insertViewModel: InsertCategoryViewModel by viewModels {
        InsertCategoryViewModel.InsertCategoryViewModelFactory(CategoryRepository(AppDatabase.getDatabase(requireContext()).categoryDao()))
    }

    override fun onStart() {
        super.onStart()

        // Thiết lập chiều cao cho DialogFragment
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            (resources.displayMetrics.heightPixels * 0.6).toInt()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentInsertCategoryBinding.inflate(inflater, container, false)

        val colors = listOf(
            R.color.work_color,
            R.color.fitness_color,
            R.color.personal_color,
            R.color.shopping_color,
            R.color.jade_green,
            R.color.pink,
            R.color.brown,
            R.color.violet
        )

        binding.rvColor.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val colorAdapter = ColorAdapter(colors) { color ->
            binding.cvIcon.setCardBackgroundColor(ContextCompat.getColor(requireContext(), color))
            selectedColor = color
        }
        binding.rvColor.adapter = colorAdapter

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnCreate.setOnClickListener {
            createCategory()
        }

        binding.btnIcon.setOnClickListener {
            findNavController().navigate(R.id.action_insertCategoryFragment_to_imagePickerFragment)
        }

        // Lắng nghe kết quả trả về từ ImagePickerFragment
        setInsertCategoryFragmentResultListener()

        //set up observer cho insertCategoryViewModel.insertResult
        setUpInsertResultObserver()
    }

    private fun createCategory() {
        val title = binding.etTitle.text.toString()
        val iconId = selectedIcon
        val color = selectedColor
        insertViewModel.insertCategory(title, iconId, color)
    }

    private fun setInsertCategoryFragmentResultListener() {
        setFragmentResultListener("iconRequestKey") { _, bundle ->
            val iconId = bundle.getInt("selectedIconId", -1)
            if (iconId != -1) {
                binding.ivIcon.setImageResource(iconId)
                selectedIcon = iconId
            }
        }
    }

    private fun setUpInsertResultObserver() {
        insertViewModel.insertResult.observe(viewLifecycleOwner) {result ->
            val (success, message) = result
            if(success) {
                showToast(message)
                dismiss()
            }
            else {
                showToast(message)
            }
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