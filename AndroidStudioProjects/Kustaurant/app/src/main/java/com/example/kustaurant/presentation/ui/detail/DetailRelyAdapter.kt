package com.example.kustaurant.presentation.ui.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kustaurant.databinding.ItemDetailReviewReplyBinding

class DetailRelyAdapter(val replyData : ArrayList<ReviewReplyData>) : RecyclerView.Adapter<DetailRelyAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemDetailReviewReplyBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ReviewReplyData){

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailRelyAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = replyData.size

    override fun onBindViewHolder(holder: DetailRelyAdapter.ViewHolder, position: Int) {
        holder.bind(replyData[position])
    }
}