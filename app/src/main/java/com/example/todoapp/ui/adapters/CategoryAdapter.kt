package com.example.todoapp.ui.adapters

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.CategoryItemBinding
import com.example.todoapp.model.Category

class CategoryAdapter (
    private var categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION
    private var selectedCategory: Category? = null

    inner class CategoryViewHolder(private val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        val ivIcon = binding.ivCategoryIcon
        val tvName = binding.tvCategoryName
        val linearLayout = binding.linearLayout

        fun bind(category: Category) {
            ivIcon.setImageResource(category.icon)
            tvName.text = category.title

            //set màu cho từng category
            val background = linearLayout.background as? GradientDrawable
            background?.setColor(ContextCompat.getColor(binding.root.context, category.color))

            // Thiết lập kích thước item view
            val scale = if (adapterPosition == selectedPosition) 0.9f else 1.0f // Kích thước khi chọn (thu nhỏ)
            binding.root.scaleX = scale
            binding.root.scaleY = scale

            // Thiết lập sự kiện click cho item
            binding.root.setOnClickListener {
                onCategoryClick(category)

                notifyItemChanged(selectedPosition) // Cập nhật item trước đó
                selectedPosition = adapterPosition // Cập nhật item hiện tại là đã chọn
                selectedCategory = category
                notifyItemChanged(selectedPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    // Hàm để lấy category đã chọn
    fun getSelectedCategory(): Category? {
        return selectedCategory
    }

    fun updateCategories(newCategories: List<Category>) {
        this.categories = newCategories
        notifyDataSetChanged()
    }
}
