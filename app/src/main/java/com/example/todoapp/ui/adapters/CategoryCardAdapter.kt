package com.example.todoapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.CategoryCardViewBinding
import com.example.todoapp.model.Category

class CategoryCardAdapter(private var categories: List<Category>): RecyclerView.Adapter<CategoryCardAdapter.CategoryCardViewHolder>() {

    inner class CategoryCardViewHolder(private val binding: CategoryCardViewBinding): RecyclerView.ViewHolder(binding.root) {
        val icon = binding.ivCardImage
        val title = binding.tvCardTitle

        fun bind(category: Category) {
            icon.setImageResource(category.icon)
            title.text = category.title
            Log.d("CATEGORY COLOR", category.color.toString())
            binding.root.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, category.color))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryCardViewHolder {
        val binding = CategoryCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryCardViewHolder(binding)
    }

    override fun getItemCount(): Int  = categories.size

    override fun onBindViewHolder(holder: CategoryCardViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    fun updateCategories(newCategories: List<Category>) {
        this.categories = newCategories
        notifyDataSetChanged()
    }

}