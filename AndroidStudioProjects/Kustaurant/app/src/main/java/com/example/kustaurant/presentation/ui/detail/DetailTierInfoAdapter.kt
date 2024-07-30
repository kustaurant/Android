package com.example.kustaurant.presentation.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kustaurant.databinding.ItemDetailTierKeywordBinding
import com.example.kustaurant.databinding.ItemDetailTierTierBinding

class DetailTierInfoAdapter(val context : Context, private val tierData : ArrayList<TierInfoData>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        const val VIEW_TYPE_TIER = 0
        const val VIEW_TYPE_KEYWORD = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_TIER
        } else
            VIEW_TYPE_KEYWORD
    }

    inner class TierViewHolder(val binding : ItemDetailTierTierBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : TierInfoData){
            binding.ivTier.setImageResource(item.tierImage)
            binding.tvTierName.text = item.tierName
            binding.tvTierNumber.text = item.tierNumber.toString()
        }
    }

    inner class KeyWordViewHolder(val binding : ItemDetailTierKeywordBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : TierInfoData){
            binding.tvTier.text = item.tierName
        }
    }
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            return when (viewType) {
                VIEW_TYPE_TIER -> TierViewHolder(ItemDetailTierTierBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                VIEW_TYPE_KEYWORD -> KeyWordViewHolder(ItemDetailTierKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                else -> throw IllegalArgumentException("태정 : 티어, 키워드 리사이클러 오류")
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder){
                is TierViewHolder -> holder.bind(tierData[position])
                is KeyWordViewHolder -> holder.bind(tierData[position])
            }
        }

        override fun getItemCount(): Int = tierData.size
}