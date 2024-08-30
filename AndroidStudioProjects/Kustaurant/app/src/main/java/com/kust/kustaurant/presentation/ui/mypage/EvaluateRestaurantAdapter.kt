package com.kust.kustaurant.presentation.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.data.model.MyEvaluateResponse
import com.kust.kustaurant.databinding.ItemMyEvaluateBinding

class EvaluateRestaurantAdapter() : ListAdapter<MyEvaluateResponse, EvaluateRestaurantAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding : ItemMyEvaluateBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : MyEvaluateResponse){
            binding.myTvEvaluateRestaurant.text = item.restaurantName
            binding.myTvEvaluateBody.text = item.restaurantName
            binding.myTvReviewTime.text = item.restaurantName
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EvaluateRestaurantAdapter.ViewHolder {
        val binding = ItemMyEvaluateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EvaluateRestaurantAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<MyEvaluateResponse>() {
            override fun areItemsTheSame(oldItem: MyEvaluateResponse, newItem: MyEvaluateResponse): Boolean =
                oldItem.restaurantName == newItem.restaurantName

            override fun areContentsTheSame(oldItem: MyEvaluateResponse, newItem: MyEvaluateResponse): Boolean =
                oldItem.restaurantName == newItem.restaurantName
        }
    }

}