package com.example.kustaurant.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ItemHomeRvBinding

class MeRestaurantAdapter(var meRestaurantList: ArrayList<HomeRestaurantItem>): RecyclerView.Adapter<MeRestaurantAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemHomeRvBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(restaurant: HomeRestaurantItem){
            binding.homeCuisine.text = restaurant.restaurantCuisine
            binding.homePosition.text = restaurant.restaurantPosition
            binding.homeName.text = restaurant.restaurantName
            binding.homeBenefit.text = restaurant.partnershipInfo
//            binding.homeMEStar.text = restaurant.star 별점

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
        holder.bind(meRestaurantList[position])
    }

    override fun getItemCount(): Int {
        return meRestaurantList.size
    }

}