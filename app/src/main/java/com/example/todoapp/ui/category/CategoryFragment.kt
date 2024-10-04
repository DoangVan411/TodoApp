package com.example.todoapp.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.databinding.CategoryCardViewBinding
import com.example.todoapp.databinding.FragmentCategoryBinding
import com.example.todoapp.repository.CategoryRepository
import com.example.todoapp.ui.adapters.CategoryAdapter
import com.example.todoapp.ui.adapters.CategoryCardAdapter

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryCardAdapter

    private val categoryViewModel: CategoryViewModel by viewModels {
        CategoryViewModel.CategoryViewModelFactory(CategoryRepository(AppDatabase.getDatabase(requireContext()).categoryDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val view = binding.root

        //set up category recycler view
        categoryRecyclerView = binding.rvCategory
        categoryRecyclerView.layoutManager = GridLayoutManager(context, 3)

        categoryAdapter = CategoryCardAdapter(mutableListOf())
        categoryRecyclerView.adapter = categoryAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up observer cho categoryViewModel.categoryList
        setUpCategoryListObserver()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnCreateNewCategory.setOnClickListener {
            findNavController().navigate(R.id.action_categoryFragment_to_insertCategoryFragment)
        }
    }

    private fun setUpCategoryListObserver() {
        categoryViewModel.categoryList.observe(viewLifecycleOwner) {categories ->
            categoryAdapter.updateCategories(categories)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}