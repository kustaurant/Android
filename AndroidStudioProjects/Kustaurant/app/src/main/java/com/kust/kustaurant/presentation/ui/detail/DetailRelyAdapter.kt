package com.kust.kustaurant.presentation.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.data.model.ReplyDataResponse
import com.kust.kustaurant.databinding.ItemDetailReviewReplyBinding

class DetailRelyAdapter(val replyData : ArrayList<ReplyDataResponse>) : RecyclerView.Adapter<DetailRelyAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemDetailReviewReplyBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ReplyDataResponse){
            binding.tvUserName.text = item.commentNickname
            binding.tvReview.text = item.commentBody
            binding.tvLike.text = item.commentLikeCount.toString()
            binding.tvHate.text = item.commentDislikeCount.toString()
            binding.tvReviewTime.text = item.commentTime
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