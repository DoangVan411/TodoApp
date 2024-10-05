package com.example.todoapp.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.databinding.FragmentSearchBinding
import com.example.todoapp.repository.CategoryRepository
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.adapters.TaskAdapter
import com.example.todoapp.ui.home.HomeFragmentDirections

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskRecyclerView: RecyclerView

    private val searchViewModel: SearchViewModel by viewModels {
        SearchViewModel.SearchViewModelFactory(TaskRepository(AppDatabase.getDatabase(requireContext()).taskDao()), CategoryRepository(AppDatabase.getDatabase(requireContext()).categoryDao()), requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        taskAdapter = TaskAdapter({ task ->
            val action = SearchFragmentDirections.actionSearchFragmentToEditFragment(task.id)
            findNavController().navigate(action)
        }, { categoryId ->
            val categoryColor = searchViewModel.categoryColor.value?.get(categoryId) ?: -1
            categoryColor
        })


        taskRecyclerView = binding.rvTask
        taskRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        taskRecyclerView.adapter = taskAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up taskList Observer
        searchViewModel.taskList.observe(viewLifecycleOwner) {tasks ->
            taskAdapter.submitList(tasks)
        }

        //set up categoryList Observer
        searchViewModel.categoryList.observe(viewLifecycleOwner) { categories ->
            searchViewModel.updateCategoryColors(categories)
        }

        //điều hướng về màn Home bằng nút back
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        //set up search view
        setUpSearchViewListener()
    }

    private fun setUpSearchViewListener () {
        binding.svSearchTask.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchViewModel.searchTasks(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchViewModel.searchTasks(it)
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}