package com.kust.kustaurant.presentation.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ItemDetailTierKeywordBinding
import com.kust.kustaurant.databinding.ItemDetailTierTierBinding

class DetailTierInfoAdapter(val context : Context, private val tierData : TierInfoData) : ListAdapter<TierInfoData, RecyclerView.ViewHolder>(diffUtil){

    companion object {
        const val VIEW_TYPE_TIER = 0
        const val VIEW_TYPE_KEYWORD = 1

        private val diffUtil = object : DiffUtil.ItemCallback<TierInfoData>() {
            override fun areItemsTheSame(oldItem: TierInfoData, newItem: TierInfoData): Boolean =
                oldItem.tierNumber == newItem.tierNumber

            override fun areContentsTheSame(oldItem: TierInfoData, newItem: TierInfoData): Boolean =
                oldItem == newItem
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (tierData.tierNumber == -1) {
            VIEW_TYPE_KEYWORD
        } else {
            if (position == 0) VIEW_TYPE_TIER else VIEW_TYPE_KEYWORD
        }
    }

    inner class TierViewHolder(val binding : ItemDetailTierTierBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : TierInfoData){
            if(item.tierNumber == -1){
                binding.clTierBackground.visibility = View.GONE
            } else{
                when(item.tierNumber){
                    1 -> binding.clTierBackground.background.setTint(ContextCompat.getColor(context, R.color.tier_1))
                    2 -> binding.clTierBackground.background.setTint(ContextCompat.getColor(context, R.color.tier_2))
                    3 -> binding.clTierBackground.background.setTint(ContextCompat.getColor(context, R.color.tier_3))
                    4 -> binding.clTierBackground.background.setTint(ContextCompat.getColor(context, R.color.tier_4))
                }
                Glide.with(context)
                    .load(item.tierImage)
                    .into(binding.ivTier)
                binding.tvTierName.text = item.tierName
                binding.tvTierNumber.text = item.tierNumber.toString()
            }
        }
    }

    inner class KeyWordViewHolder(val binding : ItemDetailTierKeywordBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: String){
            binding.tvTier.text = item
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
            if (tierData.tierNumber == -1) {
                (holder as KeyWordViewHolder).bind(tierData.situationList[position])
            } else {
                if (holder is TierViewHolder && position == 0) {
                    holder.bind(tierData)
                } else if (holder is KeyWordViewHolder) {
                    holder.bind(tierData.situationList[position - 1])
                }
            }
        }

        override fun getItemCount(): Int {
            return if (tierData.tierNumber == -1)
                tierData.situationList.size
            else 1 + tierData.situationList.size
        }
}