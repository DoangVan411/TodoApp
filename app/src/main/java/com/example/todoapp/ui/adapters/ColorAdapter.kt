package com.example.todoapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.ColorItemBinding

class ColorAdapter(
    private val colors: List<Int>,
    private val onColorClick: (Int) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION
    private var selectedColor: Int? = null

    inner class ColorViewHolder(private val binding: ColorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(color: Int) {
            binding.colorView.setBackgroundColor(ContextCompat.getColor(binding.root.context, color))

            // Thiết lập kích thước item view
            val scale = if (adapterPosition == selectedPosition) 0.85f else 1.0f // Kích thước khi chọn (thu nhỏ)
            binding.root.scaleX = scale
            binding.root.scaleY = scale

            binding.colorView.setOnClickListener {
                onColorClick(color)
                notifyItemChanged(selectedPosition)
                selectedPosition = adapterPosition
                selectedColor = color
                Log.d("COLOR SELECTED", selectedColor.toString())
                notifyItemChanged(selectedPosition)
            }
        }
    }

    fun getColorSelected(): Int? {
        return selectedColor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val binding = ColorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position])
    }

    override fun getItemCount(): Int = colors.size
}
