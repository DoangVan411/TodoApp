package com.example.todoapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.TaskItemBinding
import com.example.todoapp.model.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(
    private val onItemClicked: (Task) -> Unit,
    private val getCategoryColor: (Int) -> Int
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    inner class TaskViewHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val tvTitle = binding.tvTitle
        private val tvDescription = binding.tvDescription
        private val tvDueDate = binding.tvDueDate
        private val ivImportance = binding.ivStar

        fun bind(task: Task) {
            tvTitle.text = task.title
            tvDescription.text = task.description
            val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            tvDueDate.text = timeFormat.format(task.dueDate)

            // Thiết lập icon cho tầm quan trọng
            if (task.importance) {
                ivImportance.setImageResource(R.drawable.star)
            } else {
                ivImportance.setImageDrawable(null)
            }

            // Set màu nền cho từng task
            val categoryColor = getCategoryColor(task.category)
            val color = ContextCompat.getColor(binding.root.context, categoryColor)
            binding.root.setCardBackgroundColor(color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.bind(currentTask)

        // Thiết lập sự kiện click cho itemView
        holder.itemView.setOnClickListener {
            onItemClicked(currentTask)
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}