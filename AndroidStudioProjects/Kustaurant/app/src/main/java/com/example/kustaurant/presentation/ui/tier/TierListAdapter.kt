package com.example.kustaurant.presentation.ui.tier

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ItemRestaurantExpandBinding
import com.example.kustaurant.databinding.ItemRestaurantReductionBinding
import com.example.kustaurant.domain.model.TierRestaurant


class TierListAdapter(private var isExpanded: Boolean = true) : ListAdapter<TierRestaurant, RecyclerView.ViewHolder>(
    diffUtil
) {

    @SuppressLint("NotifyDataSetChanged")
    fun setExpanded(expanded: Boolean) {
        if (isExpanded != expanded) {
            isExpanded = expanded
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isExpanded) VIEW_TYPE_EXPANDED else VIEW_TYPE_REDUCED
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
        fun bind(item: TierRestaurant) {
            binding.tierTvRestaurantName.text = item.restaurantName
            binding.tierTvRestaurantDetails.text = "${item.restaurantCuisine} | ${item.restaurantPosition}"

            if(item.restaurantName.isBlank())
                binding.tierTvRestaurantPartnershipInfo.text = R.string.restaurant_no_partnership_info.toString()
            else
                binding.tierTvRestaurantPartnershipInfo.text =  item.partnershipInfo

            Glide.with(binding.root)
                .load(if (item.restaurantImgUrl.isEmpty()) R.drawable.img_default_restaurant else item.restaurantImgUrl)
                .override(55, 55)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(binding.tierIvRestaurantImg)

            when (item.mainTier) {
                1 -> binding.tierIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_1)
                2 -> binding.tierIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_2)
                3 -> binding.tierIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_3)
                4 -> binding.tierIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_4)
                else -> binding.tierIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_all)
            }

            binding.tierIvRestaurantFavoriteImg.visibility = if (item.isFavorite) View.VISIBLE else View.GONE
            binding.tierIvRestaurantEvaluationImg.visibility = if (item.isEvaluated) View.VISIBLE else View.GONE
        }
    }


    inner class ReducedViewHolder(private val binding: ItemRestaurantReductionBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: TierRestaurant) {
            binding.tierTvRestaurantId.text = (position + 1).toString()
            binding.tierTvRestaurantName.text = item.restaurantName
            binding.tierTvRestaurantDetails.text = "${item.restaurantCuisine} | ${item.restaurantPosition}"

            Glide.with(binding.root)
                .load(if (item.restaurantImgUrl.isEmpty()) R.drawable.img_default_restaurant else item.restaurantImgUrl)
                .override(74, 74)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(binding.tierIvRestaurantImg)

            when (item.mainTier) {
                1 -> binding.tierIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_1)
                2 -> binding.tierIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_2)
                3 -> binding.tierIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_3)
                4 -> binding.tierIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_4)
                else -> binding.tierIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_all)
            }

            // Set visibility for favorite and evaluation icons
            binding.tierIvRestaurantFavoriteImg.visibility = if (item.isFavorite) View.VISIBLE else View.GONE
            binding.tierIvRestaurantEvaluationImg.visibility = if (item.isEvaluated) View.VISIBLE else View.GONE

            // Adjust constraints if only one icon is visible
            adjustIconConstraints(item.isFavorite, item.isEvaluated)
        }

        private fun adjustIconConstraints(isFavorite: Boolean, isEvaluated: Boolean) {
            val evaluationParams = binding.tierIvRestaurantEvaluationImg.layoutParams as ConstraintLayout.LayoutParams
            val favoriteParams = binding.tierIvRestaurantFavoriteImg.layoutParams as ConstraintLayout.LayoutParams

            when {
                isEvaluated && isFavorite -> {
                    // Both icons are visible, use the chain
                    evaluationParams.endToStart = binding.tierIvRestaurantFavoriteImg.id
                    evaluationParams.endToEnd = ConstraintSet.UNSET
                    favoriteParams.startToEnd = binding.tierIvRestaurantEvaluationImg.id
                    favoriteParams.startToStart = ConstraintSet.UNSET
                }
                isEvaluated -> {
                    // Only evaluation icon is visible
                    evaluationParams.endToStart = ConstraintSet.UNSET
                    evaluationParams.endToEnd = ConstraintSet.PARENT_ID
                }
                isFavorite -> {
                    // Only favorite icon is visible
                    favoriteParams.startToEnd = ConstraintSet.UNSET
                    favoriteParams.startToStart = binding.tierTvRestaurantName.id
                }
            }

            binding.tierIvRestaurantEvaluationImg.layoutParams = evaluationParams
            binding.tierIvRestaurantFavoriteImg.layoutParams = favoriteParams
        }
    }


    companion object {
        private const val VIEW_TYPE_EXPANDED = 0
        private const val VIEW_TYPE_REDUCED = 1

        val diffUtil = object : DiffUtil.ItemCallback<TierRestaurant>() {
            override fun areItemsTheSame(oldItem: TierRestaurant, newItem: TierRestaurant): Boolean {
                return oldItem.restaurantId == newItem.restaurantId
            }

            override fun areContentsTheSame(oldItem: TierRestaurant, newItem: TierRestaurant): Boolean {
                return oldItem == newItem
            }
        }
    }
}