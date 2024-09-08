package com.kust.kustaurant.presentation.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.R

data class CategoryItem(val imageResId: Int, val text: String)
class CategoryAdapter(private val categoryList: List<CategoryItem>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.category_image)
        val textView: TextView = itemView.findViewById(R.id.category_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryItem = categoryList[position]
        Glide.with(holder.itemView.context)
            .load(categoryItem.imageResId)
            .into(holder.imageView)
        holder.textView.text = categoryItem.text
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}