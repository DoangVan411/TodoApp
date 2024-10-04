package com.example.todoapp.ui.home.edit

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.databinding.FragmentEditBinding
import com.example.todoapp.model.Status
import com.example.todoapp.model.Task
import com.example.todoapp.repository.CategoryRepository
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.adapters.CategoryAdapter
import com.example.todoapp.utils.Constant
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: Calendar? = null
    private var taskDueDate: Long? = null

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    private val editViewModel: EditViewModel by viewModels {
        EditViewModel.EditViewModelFactory(TaskRepository(AppDatabase.getDatabase(requireContext()).taskDao()), CategoryRepository(AppDatabase.getDatabase(requireContext()).categoryDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val view = binding.root

        //set up category recycler view
        categoryRecyclerView = binding.rvCategory
        categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

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

        //lay ra ca task tu taskId da truyen vao, set up thong tin cua task cho cac edit text
        val taskId = arguments?.getInt("taskId") ?: -1
        setUpInformation(taskId)

        // Lấy task thông qua ViewModel
        editViewModel.getTaskById(taskId).observe(viewLifecycleOwner, Observer { task ->
            task?.let {
                categoryAdapter = CategoryAdapter(mutableListOf(), task.category)
                categoryRecyclerView.adapter = categoryAdapter
            }
            //set up observer cho insertViewModel.categoryList
            setUpCategoryListObserver()
        })

        //Lưu sau khi sửa
        binding.btnSave.setOnClickListener {
            updateTask(taskId, 1)
        }

        //Hiện ra calendar để chọn dueDate
        binding.etDueDate.setOnClickListener {
            showDateTimePicker()
        }

        //Màn hình biến mất khi ấn Cancel
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        //Chỉ 1 checkbox important được tick tại 1 thời điểm
        binding.cbImportant.setOnClickListener {
            binding.cbNotImportant.isChecked = false
        }
        binding.cbNotImportant.setOnClickListener{
            binding.cbImportant.isChecked = false
        }

        //Xóa task nếu ấn vào biểu tượng thùng rác
        binding.ivTrashBin.setOnClickListener {
            showConfirmationDialog("Delete Task", "Are you sure you want to delete this task?", taskId) { id ->
                deleteTask(id)
            }
        }

        //Đánh dấu task là đã hoàn thành
        binding.ivCompleted.setOnClickListener {
            showConfirmationDialog("Complete Task", "Are you sure you want to mark this task as completed?", taskId) { id ->
                updateTask(id, 2)
            }
        }

        //set up observer cho editViewModel.updateResult
        editViewModel.updateResult.observe(viewLifecycleOwner) { result ->
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

    private fun updateTask(taskId: Int, mode: Int) {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()
        val dueTime = selectedDate?.timeInMillis ?: taskDueDate

        val importance = when {
            binding.cbImportant.isChecked -> true
            binding.cbNotImportant.isChecked -> false
            else -> null
        }
        val category = categoryAdapter.getSelectedCategory()

        if(mode == 1) {
            editViewModel.updateTask(taskId, title, description, importance, dueTime, category!!.id, Status.ON_GOING)
        }
        else {
            editViewModel.updateTask(taskId, title, description, importance, dueTime, category!!.id, Status.COMPLETED)
        }
    }

    private fun setUpInformation(taskId: Int) {
        editViewModel.getTaskById(taskId).observe(viewLifecycleOwner, Observer { task ->
            task?.let {
                binding.etTitle.setText(it.title)
                binding.etDescription.setText(it.description)

                val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                binding.etDueDate.text = format.format(it.dueDate)
                taskDueDate = it.dueDate.time

                if(it.importance) {
                    binding.cbImportant.isChecked = true
                }
                else {
                    binding.cbNotImportant.isChecked = true
                }
            }
        })
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

    private fun showConfirmationDialog(title: String, message: String, taskId: Int, func: (Int) -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes") { _, _ ->
                func(taskId)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteTask(taskId: Int) {
        editViewModel.deleteTask(taskId)
        dismiss()
        showToast(Constant.DELETE_SUCCESSFUL)
    }

    private fun setUpCategoryListObserver() {
        editViewModel.categoryList.observe(viewLifecycleOwner) {categories ->
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