package com.example.kustaurant.presentation.ui.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kustaurant.databinding.ItemDetailTierInfoBinding

class DetailTierInfoAdapter(val data : ArrayList<TierInfoData>) : RecyclerView.Adapter<DetailTierInfoAdapter.ViewHolder>(){

    inner class ViewHolder(val binding : ItemDetailTierInfoBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : TierInfoData){

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailTierInfoAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: DetailTierInfoAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}