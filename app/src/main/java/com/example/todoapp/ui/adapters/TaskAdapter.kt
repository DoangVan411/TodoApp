package com.example.todoapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.TaskItemBinding
import com.example.todoapp.model.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(
    private var tasks: List<Task>,
    private val onItemClicked: (Task) -> Unit
): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(private val binding: TaskItemBinding): RecyclerView.ViewHolder(binding.root) {
        val tvTitle = binding.tvTitle
        val tvDescription = binding.tvDescription
        val tvDueDate = binding.tvDueDate
        val ivImportance = binding.ivStar
        val tvStatus = binding.tvStatus
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]
        holder.tvTitle.text = currentTask.title
        holder.tvDescription.text = currentTask.description
        val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        holder.tvDueDate.text = timeFormat.format(currentTask.dueDate)
        holder.tvStatus.text = currentTask.status.displayName
        if(tasks[position].importance) {
            holder.ivImportance.setImageResource(R.drawable.star)
        }
        else {
            holder.ivImportance.setImageDrawable(null)
        }

        holder.itemView.setOnClickListener {
            onItemClicked(currentTask)
        }
    }

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

}