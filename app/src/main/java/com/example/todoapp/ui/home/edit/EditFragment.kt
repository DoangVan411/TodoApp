package com.example.todoapp.ui.home.edit

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.databinding.FragmentEditBinding
import com.example.todoapp.repository.TaskRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: Calendar? = null

    private val editViewModel: EditViewModel by viewModels {
        EditViewModel.EditViewModelFactory(TaskRepository(AppDatabase.getDatabase(requireContext()).taskDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val view = binding.root

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

        //lay ra ca task tu taskId da truyen vao
        val taskId = arguments?.getInt("taskId") ?: -1
        editViewModel.getTaskById(taskId){task ->
            task?.let {
                binding.etTitle.setText(it.title)
                binding.etDescription.setText(it.description)
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                binding.etDueDate.text = format.format(task.dueDate)
                if(task.importance) {
                    binding.cbImportant.isChecked = true
                }
                else {
                    binding.cbNotImportant.isChecked = true
                }
            }
        }


    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = android.app.DatePickerDialog(
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}