package com.example.todoapp.ui.calendar

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.databinding.FragmentCalendarBinding
import com.example.todoapp.repository.CategoryRepository
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.adapters.TaskAdapter
import com.example.todoapp.ui.home.HomeViewModel
import java.time.LocalDate
import java.time.ZoneId

class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarView: CalendarView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var rvTask: RecyclerView

    private val calendarViewModel: CalendarViewModel by viewModels {
        CalendarViewModel.CalendarViewModelFactory(
            TaskRepository(AppDatabase.getDatabase(requireContext()).taskDao()), CategoryRepository(
                AppDatabase.getDatabase(requireContext()).categoryDao()), requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val view = binding.root

        taskAdapter = TaskAdapter({}, {categoryId ->
            val categoryColor = calendarViewModel.categoryColor.value?.get(categoryId) ?: -1
            categoryColor
        })
        rvTask = binding.rvTasks
        rvTask.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvTask.adapter = taskAdapter
        calendarView = binding.calendarView

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarViewModel.dateTask.observe(viewLifecycleOwner) {task ->
            Log.d("CalendarFragment", "Tasks for selected date: $task")
            taskAdapter.submitList(task)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        //set up categoryList Observer
        calendarViewModel.categoryList.observe(viewLifecycleOwner) { categories ->
            calendarViewModel.updateCategoryColors(categories)
        }

        calendarViewModel.taskList.observe(viewLifecycleOwner) {task ->
            getTodayTask()
            setUpCalendarViewListener()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTodayTask() {
        val today = LocalDate.now()
        val startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endOfDay = today.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        calendarViewModel.getTasksByDateRange(startOfDay, endOfDay)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpCalendarViewListener () {
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val startOfDay = LocalDate.of(year, month + 1, dayOfMonth)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            val endOfDay = LocalDate.of(year, month + 1, dayOfMonth)
                .atTime(23, 59, 59)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            calendarViewModel.getTasksByDateRange(startOfDay, endOfDay)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}