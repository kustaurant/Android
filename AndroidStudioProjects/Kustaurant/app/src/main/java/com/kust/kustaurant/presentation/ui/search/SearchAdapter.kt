package com.kust.kustaurant.presentation.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.kust.kustaurant.R
import com.kust.kustaurant.domain.model.SearchRestaurant
import com.kust.kustaurant.presentation.ui.home.RestaurantModel

class SearchAdapter(private var restaurantList: List<SearchRestaurant>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(data: SearchRestaurant)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantName: TextView = itemView.findViewById(R.id.search_tv_restaurant_Name)
        val restaurantDetails: TextView = itemView.findViewById(R.id.search_tv_restaurant_details)
        val restaurantImage: ImageView = itemView.findViewById(R.id.search_iv_restaurant_img)
        val restaurantTierImage: ImageView = itemView.findViewById(R.id.search_iv_restaurant_tier_img)
        val restaurantFavoriteImage: ImageView = itemView.findViewById(R.id.search_iv_restaurant_favorite_img)
        val restaurantEvaluationImage: ImageView = itemView.findViewById(R.id.search_iv_restaurant_evaluation_img)

        init {
            itemView.setOnClickListener {
                itemClickListener?.onItemClicked(restaurantList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_restaurant, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.restaurantName.text = restaurant.restaurantName
        holder.restaurantDetails.text = "${restaurant.restaurantCuisine} | ${restaurant.restaurantPosition}"

        holder.restaurantFavoriteImage.visibility = if (restaurant.isFavorite) View.VISIBLE else View.GONE
        holder.restaurantEvaluationImage.visibility = if (restaurant.isEvaluated) View.VISIBLE else View.GONE

        val parentLayout = holder.itemView as ConstraintLayout

        val constraintSet = ConstraintSet()
        constraintSet.clone(parentLayout)

        if (restaurant.isFavorite) {
            constraintSet.connect(
                holder.restaurantEvaluationImage.id, ConstraintSet.END,
                holder.restaurantFavoriteImage.id, ConstraintSet.START, 8
            )
        } else {
            constraintSet.connect(
                holder.restaurantEvaluationImage.id, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END, 0
            )
        }

        constraintSet.applyTo(parentLayout)

        when (restaurant.mainTier) {
            1 -> holder.restaurantTierImage.setImageResource(R.drawable.ic_rank_1)
            2 -> holder.restaurantTierImage.setImageResource(R.drawable.ic_rank_2)
            3 -> holder.restaurantTierImage.setImageResource(R.drawable.ic_rank_3)
            4 -> holder.restaurantTierImage.setImageResource(R.drawable.ic_rank_4)
            else -> holder.restaurantTierImage.setImageResource(R.drawable.ic_rank_all)
        }

        Glide.with(holder.itemView.context)
            .load(restaurant.restaurantImgUrl)
            .override(55, 55)
            .transform(CenterCrop(), RoundedCorners(10))
            .into(holder.restaurantImage)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    fun updateData(newList: List<SearchRestaurant>) {
        restaurantList = newList
        notifyDataSetChanged()
    }
}