package com.kust.kustaurant.presentation.ui.home

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ItemHomeRvBinding

class MeRestaurantAdapter(var meRestaurantList: ArrayList<RestaurantModel>): RecyclerView.Adapter<MeRestaurantAdapter.ViewHolder>(){

    lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClicked(data : RestaurantModel)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding: ItemHomeRvBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(restaurant: RestaurantModel){
            binding.homeCuisine.text = restaurant.restaurantCuisine
            binding.homePosition.text = restaurant.restaurantPosition
            binding.homeName.text = restaurant.restaurantName
            binding.homeBenefit.text = restaurant.partnershipInfo

            val score = restaurant.restaurantScore
            if (score == null || score == 0.0) {
                binding.homeIvStar.visibility = View.INVISIBLE
                binding.homeStar.text = "평가없음"
            } else {
                binding.homeStar.text = score.toString()
                binding.homeIvStar.setColorFilter(
                    ContextCompat.getColor(binding.root.context, R.color.tier_3),
                    PorterDuff.Mode.SRC_IN
                )
                binding.homeIvStar.visibility = View.VISIBLE
            }

            val tierDrawable = when(restaurant.mainTier){
                1-> R.drawable.ic_rank_1
                2-> R.drawable.ic_rank_2
                3-> R.drawable.ic_rank_3
                4 -> R.drawable.ic_rank_4
                else -> R.drawable.ic_rank_all
            }

            Glide.with(binding.root.context)
                .load(restaurant.restaurantImgUrl)
                .centerCrop()
                .into(binding.homeImgurl)

            binding.homeTier.setImageResource(tierDrawable)

            binding.homeRV.setOnClickListener{ // recyclerview를 클릭했을 때
                itemClickListener.onItemClicked(restaurant)
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