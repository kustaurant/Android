package com.kust.kustaurant.presentation.ui.mypage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.databinding.ItemEvaluateKeywordBinding

class EvaluateMyKeyWordAdapter() : ListAdapter<String, EvaluateMyKeyWordAdapter.ViewHolder>(
    diffUtil) {

    inner class ViewHolder(val binding: ItemEvaluateKeywordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvKeyword.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEvaluateKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // null 일 때 0으로 반환, 갯수 0

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}
