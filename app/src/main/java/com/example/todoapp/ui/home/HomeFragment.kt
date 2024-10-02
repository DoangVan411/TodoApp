package com.example.todoapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.repository.CategoryRepository
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.adapters.CategoryAdapter
import com.example.todoapp.ui.adapters.TaskAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModel.HomeViewModelFactory(TaskRepository(AppDatabase.getDatabase(requireContext()).taskDao()), CategoryRepository(AppDatabase.getDatabase(requireContext()).categoryDao()), requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        homeViewModel.insertDefaultCategories()

        //set up task recycler view
        taskRecyclerView = binding.rvTask
        taskRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        taskAdapter = TaskAdapter(mutableListOf(), { task ->
            val action = HomeFragmentDirections.actionHomeFragmentToEditFragment(task.id)
            findNavController().navigate(action)
        }, { categoryId ->
            val categoryColor = homeViewModel.categoryColor.value?.get(categoryId) ?: -1
            Log.d("CATEGORY", categoryColor.toString())
            categoryColor
        })

        taskRecyclerView.adapter = taskAdapter

        //set up category recycler view
        categoryRecyclerView = binding.rvCategory
        categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(mutableListOf()) {}
        categoryRecyclerView.adapter = categoryAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up observer cho homeViewModel.taskList
        setUpTaskListObserver()

        //set up observer cho homeViewModel.categoryList
        setUpCategoryListObserver()

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

    private fun setUpCategoryListObserver() {
        homeViewModel.categoryList.observe(viewLifecycleOwner) {categories ->
            categoryAdapter.updateCategories(categories)
            // Cập nhật màu sắc category
            homeViewModel.updateCategoryColors(categories)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}