package com.example.todoapp.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.DateItemBinding

class HomeDateAdapter(
    private val dates: List<Pair<String, String>>
): RecyclerView.Adapter<HomeDateAdapter.HomeDateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeDateViewHolder {
        val view = DateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeDateViewHolder(view)
    }

    override fun getItemCount(): Int = dates.size

    override fun onBindViewHolder(holder: HomeDateViewHolder, position: Int) {
        val (day, dayOfWeek) = dates[position]

        holder.tvDate.text = day
        holder.tvDay.text = dayOfWeek
    }

    inner class HomeDateViewHolder(binding: DateItemBinding): RecyclerView.ViewHolder(binding.root) {
        val tvDate = binding.tvDate
        val tvDay = binding.tvDay
    }
}