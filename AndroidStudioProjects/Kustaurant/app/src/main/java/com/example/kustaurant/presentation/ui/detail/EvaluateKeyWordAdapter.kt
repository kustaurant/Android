package com.example.kustaurant.presentation.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ItemDetailMenuBinding
import com.example.kustaurant.databinding.ItemEvaluateKeywordBinding

class EvaluateKeyWordAdapter(private val keywords: ArrayList<String>) : RecyclerView.Adapter<EvaluateKeyWordAdapter.ViewHolder>() {
    private val selectedPositions: MutableList<Boolean> = MutableList(keywords.size) { false }

    inner class ViewHolder(val binding: ItemEvaluateKeywordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = keywords[position]
            val isSelected = selectedPositions[position]

            binding.tvKeyword.text = item
            updateUI(isSelected, binding)

            binding.clKeyword.setOnClickListener {
                selectedPositions[position] = !selectedPositions[position]
                updateUI(selectedPositions[position], binding)
            }
        }

        private fun updateUI(isSelected: Boolean, binding: ItemEvaluateKeywordBinding) {
            if (isSelected) {
                binding.clKeyword.setBackgroundColor(binding.root.context.getColor(R.color.tier_2))
                binding.clKeyword.setBackgroundResource(R.drawable.all_radius_100)
                binding.tvKeyword.setTextColor(binding.root.context.getColor(R.color.signature_1))
            } else {
                binding.clKeyword.setBackgroundResource(R.drawable.all_radius_100)
                binding.tvKeyword.setTextColor(binding.root.context.getColor(R.color.cement_4))
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
