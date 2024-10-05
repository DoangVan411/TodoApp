package com.example.todoapp.ui.home.insert

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.databinding.FragmentInsertBinding
import com.example.todoapp.model.Status
import com.example.todoapp.repository.CategoryRepository
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.adapters.CategoryAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InsertFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentInsertBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: Calendar? = null

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    private val insertViewModel:InsertViewModel by viewModels {
        InsertViewModel.InsertViewModelFactory(TaskRepository(AppDatabase.getDatabase(requireContext()).taskDao()), CategoryRepository(AppDatabase.getDatabase(requireContext()).categoryDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInsertBinding.inflate(inflater, container, false)
        val view = binding.root

        //set up category recycler view
        categoryRecyclerView = binding.rvCategory
        categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(mutableListOf()) {}
        categoryRecyclerView.adapter = categoryAdapter

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = (resources.displayMetrics.heightPixels * 0.7).toInt()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            insertTask()
        }

        binding.etDueDate.setOnClickListener {
            showDateTimePicker()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // Set up checkboxes behavior
        binding.cbImportant.setOnClickListener {
            binding.cbNotImportant.isChecked = false
        }
        binding.cbNotImportant.setOnClickListener{
            binding.cbImportant.isChecked = false
        }

        //set up observer cho insertViewModel.insertResult
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

        //set up observer cho insertViewModel.categoryList
        setUpCategoryListObserver()
    }

    private fun insertTask() {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()
        val dueTime = selectedDate?.timeInMillis

        val importance = when {
            binding.cbImportant.isChecked -> true
            binding.cbNotImportant.isChecked -> false
            else -> null
        }

        val category = categoryAdapter.getSelectedCategory()
        insertViewModel.insertTask(1, title, description, importance, dueTime, category!!.id)
    }


    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                // Set the selected date
                calendar.set(year, month, day)

                // Now show time picker
                TimePickerDialog(
                    requireContext(),
                    { _, hour, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)
                        selectedDate = calendar

                        // Update the TextView with selected date and time
                        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        binding.etDueDate.text = format.format(calendar.time)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun setUpCategoryListObserver() {
        insertViewModel.categoryList.observe(viewLifecycleOwner) {categories ->
            categoryAdapter.updateCategories(categories)
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