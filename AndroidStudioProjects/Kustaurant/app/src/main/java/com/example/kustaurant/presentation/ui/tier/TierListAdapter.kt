package com.example.kustaurant

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kustaurant.databinding.ItemRestaurantExpandBinding
import com.example.kustaurant.databinding.ItemRestaurantReductionBinding

class TierListAdapter(private var isExpanded: Boolean = true) : ListAdapter<TierModel, RecyclerView.ViewHolder>(diffUtil) {

    fun setExpanded(expanded: Boolean) {
        if (isExpanded != expanded) {
            isExpanded = expanded
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isExpanded) VIEW_TYPE_REDUCED else VIEW_TYPE_EXPANDED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EXPANDED -> {
                val binding = ItemRestaurantExpandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ExpandedViewHolder(binding)
            }
            VIEW_TYPE_REDUCED -> {
                val binding = ItemRestaurantReductionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ReducedViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ExpandedViewHolder -> holder.bind(item)
            is ReducedViewHolder -> holder.bind(item)
        }
    }

    inner class ExpandedViewHolder(private val binding: ItemRestaurantExpandBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: TierModel) {
            binding.restaurantId.text = item.restaurantId.toString()

            binding.restaurantTextName.text = item.restaurantName
            binding.restaurantTextDetails.text = "${item.restaurantCuisine} | ${item.restaurantPosition}"

            if(item.restaurantImgUrl.isEmpty()) {
                Glide.with(binding.root)
                    .load(R.drawable.img_default_restaurant)
                    .into(binding.restaurantImage)
            } else {
                Glide.with(binding.root)
                    .load(item.restaurantImgUrl)
                    .into(binding.restaurantImage)
            }

            when(item.mainTier) {
                1 -> binding.restaurantTier.setImageResource(R.drawable.ic_rank_1)
                2 -> binding.restaurantTier.setImageResource(R.drawable.ic_rank_2)
                3 -> binding.restaurantTier.setImageResource(R.drawable.ic_rank_3)
                else -> binding.restaurantTier.setImageDrawable(null)
            }

            // Set visibility for favorite and evaluation icons
            binding.favoriteImg.visibility = if (item.isFavorite) View.VISIBLE else View.GONE
            binding.evaluationImg.visibility = if (item.isChecked) View.VISIBLE else View.GONE

        }
    }

    inner class ReducedViewHolder(private val binding: ItemRestaurantReductionBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: TierModel) {
            binding.restaurantTextName.text = item.restaurantName
            binding.restaurantTextDetails.text = "${item.restaurantCuisine} | ${item.restaurantPosition}"

            if(item.restaurantImgUrl.isEmpty()) {
                Glide.with(binding.root)
                    .load(R.drawable.img_default_restaurant)
                    .into(binding.restaurantImage)
            } else {
                Glide.with(binding.root)
                    .load(item.restaurantImgUrl)
                    .into(binding.restaurantImage)
            }

            when(item.mainTier) {
                1 -> binding.restaurantTier.setImageResource(R.drawable.ic_rank_1)
                2 -> binding.restaurantTier.setImageResource(R.drawable.ic_rank_2)
                3 -> binding.restaurantTier.setImageResource(R.drawable.ic_rank_3)
                else -> binding.restaurantTier.setImageDrawable(null)
            }

            binding.favoriteImg.visibility = if (item.isFavorite) View.VISIBLE else View.GONE
            binding.evaluationImg.visibility = if (item.isChecked) View.VISIBLE else View.GONE

            if(item.alliance.isEmpty()) {
                binding.restaurantTextAlliance.text = item.alliance
            } else {
                binding.restaurantTextAlliance.text = "제휴 해당사항 없음"
            }
        }

    }

    companion object {
        private const val VIEW_TYPE_EXPANDED = 0
        private const val VIEW_TYPE_REDUCED = 1

        val diffUtil = object : DiffUtil.ItemCallback<TierModel>() {
            override fun areItemsTheSame(oldItem: TierModel, newItem: TierModel): Boolean {
                return oldItem.restaurantId == newItem.restaurantId
            }

            override fun areContentsTheSame(oldItem: TierModel, newItem: TierModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}