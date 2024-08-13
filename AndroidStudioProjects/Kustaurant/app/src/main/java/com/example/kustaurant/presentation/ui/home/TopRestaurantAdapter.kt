package com.example.kustaurant.presentation.ui.home

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ItemHomeRvBinding

class TopRestaurantAdapter(var topRestaurantList: ArrayList<RestaurantModel>): RecyclerView.Adapter<TopRestaurantAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemHomeRvBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(restaurant: RestaurantModel){
            binding.homeCuisine.text = restaurant.restaurantCuisine
            binding.homePosition.text = restaurant.restaurantPosition
            binding.homeName.text = restaurant.restaurantName
            binding.homeBenefit.text = restaurant.partnershipInfo
            binding.homeStar.text = restaurant.restaurantScore.toString()

            binding.homeIvStar.setColorFilter(
                ContextCompat.getColor(binding.root.context, R.color.tier_3),
                PorterDuff.Mode.SRC_IN
            )

            val tierDrawable = when(restaurant.mainTier){
                1-> R.drawable.ic_rank_1
                2-> R.drawable.ic_rank_2
                3-> R.drawable.ic_rank_3
                else -> R.drawable.ic_rank_1
            }

            Glide.with(binding.root.context)
                .load(restaurant.restaurantImgUrl)
                .centerCrop()
                .into(binding.homeImgurl)

            binding.homeTier.setImageResource(tierDrawable)

            binding.homeRV.setOnClickListener{ // recyclerview를 클릭했을 때

            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemHomeRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(topRestaurantList[position])
    }

    override fun getItemCount(): Int {
        return topRestaurantList.size
    }

}