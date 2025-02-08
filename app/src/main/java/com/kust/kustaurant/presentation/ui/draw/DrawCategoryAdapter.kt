package com.kust.kustaurant.presentation.ui.draw

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ItemCategoryBinding
import com.kust.kustaurant.presentation.common.CategoryItem

class DrawCategoryAdapter(
    private val categoryList: List<CategoryItem>,
    private val itemClickListener: CategoryItemClickListener
) : RecyclerView.Adapter<DrawCategoryAdapter.CategoryViewHolder>() {

    private var selectedItems: Set<String> = setOf()

    interface CategoryItemClickListener {
        fun onCategoryItemClick(category: String)
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val categoryItem = categoryList[position]
                    itemClickListener.onCategoryItemClick(categoryItem.text)
                }
            }
        }

        fun bind(categoryItem: CategoryItem, isSelected: Boolean) {
            binding.categoryImage.setImageResource(categoryItem.imageResId)
            binding.categoryText.text = categoryItem.text
            binding.root.background = ContextCompat.getDrawable(binding.root.context, R.drawable.btn_selector_draw_category)
            binding.root.isSelected = isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryItem = categoryList[position]
        val isSelected = selectedItems.contains(categoryItem.text)
        holder.bind(categoryItem, isSelected)
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
