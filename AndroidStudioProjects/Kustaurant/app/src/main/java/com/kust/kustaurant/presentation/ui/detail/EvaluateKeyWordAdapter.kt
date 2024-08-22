package com.kust.kustaurant.presentation.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ItemEvaluateKeywordBinding

class EvaluateKeyWordAdapter(private val keywords: ArrayList<String>) : RecyclerView.Adapter<EvaluateKeyWordAdapter.ViewHolder>() {
    private val selectedPositions: MutableList<Boolean> = MutableList(keywords.size) { false }

    inner class ViewHolder(val binding: ItemEvaluateKeywordBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.clKeyword.setBackgroundResource(R.drawable.btn_keyword_selector)
            binding.tvKeyword.setTextColor(ContextCompat.getColorStateList(binding.root.context, R.color.btn_keyword_text))
        }

        fun bind(position: Int) {
            val item = keywords[position]
            binding.tvKeyword.text = item
            binding.clKeyword.isSelected = selectedPositions[position]

            binding.clKeyword.setOnClickListener {
                selectedPositions[position] = !selectedPositions[position]
                binding.clKeyword.isSelected = selectedPositions[position]
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEvaluateKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = keywords.size

    fun getSelectedItems(): List<String> {
        return keywords.filterIndexed { index, _ -> selectedPositions[index] }
    }
}
