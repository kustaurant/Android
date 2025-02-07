package com.kust.kustaurant.presentation.ui.draw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.R
import com.kust.kustaurant.presentation.common.CategoryItem

class DrawCategoryAdapter(private val categoryList: List<CategoryItem>,
                          private val itemClickListener: CategoryItemClickListener) :
    RecyclerView.Adapter<DrawCategoryAdapter.CategoryViewHolder>() {

    private var selectedItems: Set<String> = setOf()

    interface CategoryItemClickListener {
        fun onCategoryItemClick(category: String)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.category_image)
        val textView: TextView = itemView.findViewById(R.id.category_text)

        init {
            itemView.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val categoryItem = categoryList[position]
                    itemClickListener.onCategoryItemClick(categoryItem.text)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryItem = categoryList[position]
        holder.imageView.setImageResource(categoryItem.imageResId)
        holder.textView.text = categoryItem.text

        holder.itemView.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.btn_selector_draw_category)
        holder.itemView.isSelected = selectedItems.contains(categoryItem.text)
    }

    override fun getItemCount(): Int = categoryList.size

    fun updateSelectedItems(newSelectedItems: Set<String>) {
        val oldSelectedItems = selectedItems
        selectedItems = newSelectedItems

        categoryList.indices
            .filter { index -> oldSelectedItems.contains(categoryList[index].text) != newSelectedItems.contains(categoryList[index].text) }
            .forEach { notifyItemChanged(it) }
    }
}