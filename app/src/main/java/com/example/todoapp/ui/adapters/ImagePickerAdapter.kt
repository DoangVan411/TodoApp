package com.example.todoapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.CategoryIconItemBinding

class ImagePickerAdapter(
    private val imageList: List<Int>,
    private val onItemClick: (Int) -> Unit
): RecyclerView.Adapter<ImagePickerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: CategoryIconItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val iv: ImageView = binding.ivIcon

        fun bind(imageResId: Int) {
            iv.setImageResource(imageResId)
            binding.root.setOnClickListener {
                onItemClick(imageResId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = CategoryIconItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val ivResouce = imageList[position]
        holder.bind(ivResouce)
    }
}