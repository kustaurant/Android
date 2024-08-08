package com.example.kustaurant.presentation.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kustaurant.databinding.ItemDetailMenuBinding

class DetailMenuAdapter(val context: Context) : ListAdapter<MenuData, DetailMenuAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(val binding: ItemDetailMenuBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : MenuData){
            Glide.with(context)
                .load(item.menuImgUrl)
                .into(binding.siMenu)

            binding.tvMenuName.text = item.menuName
            binding.tvMenuPrice.text = item.menuPrice.toString()
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailMenuAdapter.ViewHolder {
        val binding = ItemDetailMenuBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailMenuAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<MenuData>() {
            override fun areItemsTheSame(oldItem: MenuData, newItem: MenuData): Boolean =
                oldItem.menuId == newItem.menuId

            override fun areContentsTheSame(oldItem: MenuData, newItem: MenuData): Boolean =
                oldItem == newItem
        }
    }
}