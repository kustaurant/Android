package com.example.kustaurant.presentation.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ItemMyScrapBinding

class ScrapAdapter(val context: Context, val data : ArrayList<SaveRestaurantData>) : RecyclerView.Adapter<ScrapAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemMyScrapBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : SaveRestaurantData){

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScrapAdapter.ViewHolder {
        val binding = ItemMyScrapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScrapAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}