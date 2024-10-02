package com.example.todoapp.ui.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.TaskItemBinding
import com.example.todoapp.model.Status
import com.example.todoapp.model.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(
    private var tasks: List<Task>,
    private val onItemClicked: (Task) -> Unit,
    private val getCategoryColor: (Int) -> Int
): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(private val binding: TaskItemBinding): RecyclerView.ViewHolder(binding.root) {
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
            if(task.importance) {
                ivImportance.setImageResource(R.drawable.star)
            }
            else {
                ivImportance.setImageDrawable(null)
            }

            //set màu nền cho từng task
            val categoryColor = getCategoryColor(task.category)
            val color = ContextCompat.getColor(binding.root.context, categoryColor)
            binding.root.setCardBackgroundColor(color)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]

        holder.bind(currentTask)

        //Thiết lập sự kiện click cho itemView
        holder.itemView.setOnClickListener {
            onItemClicked(currentTask)
        }
    }

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}