package com.example.kustaurant.presentation.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kustaurant.databinding.ItemDetailReviewBinding
import com.example.kustaurant.databinding.ItemDetailReviewReplyBinding

class DetailRelyAdapter(val replyData : ArrayList<ReviewReplyData>) : RecyclerView.Adapter<DetailRelyAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemDetailReviewReplyBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ReviewReplyData){
            binding.tvUserName.text = item.replyUserName
            binding.tvReview.text = item.replyReviewText
            binding.tvLike.text = item.replyReviewLike.toString()
            binding.tvHate.text = item.replyReviewHate.toString()
            binding.tvReviewTime.text = item.replyReviewTime
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailRelyAdapter.ViewHolder {
        val binding = ItemDetailReviewReplyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = replyData.size

    override fun onBindViewHolder(holder: DetailRelyAdapter.ViewHolder, position: Int) {
        holder.bind(replyData[position])
    }
}