package com.example.kustaurant.presentation.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ItemMySaveBinding

class SaveRestaurantAdapter(val context: Context, val data : ArrayList<SaveRestaurantData>) : RecyclerView.Adapter<SaveRestaurantAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemMySaveBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : SaveRestaurantData){
            Glide.with(context)
                .load(item.restaurantImgURL)
                .into(binding.saveIvImg)
            binding.saveTvRestaurantName.text = item.restaurantName
            binding.saveTvRestaurantType.text = item.restaurantType
            binding.saveTvRestaurantPosition.text = item.restaurantPosition

            when (item.mainTier) {
                1 -> binding.saeIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_1)
                2 -> binding.saeIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_2)
                3 -> binding.saeIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_3)
                4 -> binding.saeIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_4)
                else -> binding.saeIvRestaurantTierImg.setImageResource(R.drawable.ic_rank_all)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SaveRestaurantAdapter.ViewHolder {
        val binding = ItemMySaveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaveRestaurantAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}