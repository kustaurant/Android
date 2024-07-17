package com.example.kustaurant

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kustaurant.databinding.ItemRestaurantBinding

class TierListAdapter : ListAdapter<TierModel, TierListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: TierModel) {
            binding.restaurantId.text = item.restaurantId.toString()
            binding.restaurantName.text = item.restaurantName

            if(item.restaurantImgUrl.isEmpty()) {
                Glide.with(binding.root)
                    .load(item.restaurantImgUrl)
                    .into(binding.restaurantImage)
            }
            else {
                binding.restaurantImage.setImageResource(R.drawable.img_default_restaurant)
            }

            binding.restaurantDetails.text = "${item.restaurantCuisine} | ${item.restaurantPosition}"

            when(item.mainTier) {
                1 -> {
                    binding.restaurantTier.setImageResource(R.drawable.ic_rank_1)
                }
                2 -> {
                    binding.restaurantTier.setImageResource(R.drawable.ic_rank_2)
                }
                3 -> {
                    binding.restaurantTier.setImageResource(R.drawable.ic_rank_3)
                }
                else -> {
                    binding.restaurantTier.setImageDrawable(null)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
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
