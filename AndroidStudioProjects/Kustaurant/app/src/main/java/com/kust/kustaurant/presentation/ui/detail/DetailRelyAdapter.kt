package com.kust.kustaurant.presentation.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.ReplyDataResponse
import com.kust.kustaurant.databinding.ItemDetailReviewReplyBinding
import com.kust.kustaurant.presentation.ui.tier.TierListAdapter.Companion.diffUtil

class DetailRelyAdapter(val context : Context) : ListAdapter<ReplyDataResponse, DetailRelyAdapter.ViewHolder>(
    diffUtil) {

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

    override fun onBindViewHolder(holder: DetailRelyAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ReplyDataResponse>() {
            override fun areItemsTheSame(oldItem: ReplyDataResponse, newItem: ReplyDataResponse): Boolean =
                oldItem.commentId == newItem.commentId

            override fun areContentsTheSame(oldItem: ReplyDataResponse, newItem: ReplyDataResponse): Boolean =
                oldItem == newItem
        }
    }
}