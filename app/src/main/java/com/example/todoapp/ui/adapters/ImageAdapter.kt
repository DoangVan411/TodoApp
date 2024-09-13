package com.example.todoapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R

class ImageAdapter(private val imageList: List<Int>): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = LayoutInflater.from(parent.context).inflate(R.layout.item_image_view, parent, false) as ImageView
        return ImageViewHolder(imageView)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageResource(imageList[position])
    }

    inner class ImageViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
}