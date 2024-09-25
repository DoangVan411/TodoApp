package com.example.todoapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.databinding.DateItemBinding
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.adapters.HomeDateAdapter
import com.example.todoapp.ui.adapters.TaskAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dateRecyclerView: RecyclerView
    private lateinit var dateAdapter: HomeDateAdapter

    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModel.HomeViewModelFactory(TaskRepository(AppDatabase.getDatabase(requireContext()).taskDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        //set up task recycler view
        taskRecyclerView = binding.rvTask
        taskRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        taskAdapter = TaskAdapter(mutableListOf()) {task ->
            val action = HomeFragmentDirections.actionHomeFragmentToEditFragment(task.id)
            findNavController().navigate(action)
        }
        taskRecyclerView.adapter = taskAdapter

        //set up Date recycler view
        dateRecyclerView = binding.rvDate
        val dates = generateDateList()
        dateAdapter = HomeDateAdapter(dates)
        dateRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        dateRecyclerView.adapter = dateAdapter


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up observer cho homeViewModel.taskList
        setUpTaskListObserver()

        //Button dieu huong den InsertFragment
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_insertFragment)
        }
    }

    private fun setUpTaskListObserver() {
        homeViewModel.taskList.observe(viewLifecycleOwner) {tasks ->
            taskAdapter.updateTasks(tasks)
        }
    }

    private fun generateDateList(): List<Pair<String, String>> {
        val dates = mutableListOf<Pair<String, String>>()
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        val dayOfWeekFormat = SimpleDateFormat("EEE", Locale.getDefault()) // Lấy thứ

        for (i in -30..30) {  // Tạo danh sách từ 30 ngày trước và 30 ngày sau
            calendar.add(Calendar.DAY_OF_YEAR, i)
            val date = dateFormat.format(calendar.time)  // Ngày
            val dayOfWeek = dayOfWeekFormat.format(calendar.time)  // Thứ
            dates.add(Pair(date, dayOfWeek))  // Ghép ngày và thứ thành một cặp
            calendar.add(Calendar.DAY_OF_YEAR, -i)  // Reset lại lịch
        }
        return dates
    }

    override fun onDestroyView() {
        Log.d("HOME FRAGMENT", "onDestroyView")
        super.onDestroyView()
        _binding = null
    }
}