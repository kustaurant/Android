package com.kust.kustaurant.presentation.ui.detail

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.databinding.ItemDetailReviewBinding
import com.kust.kustaurant.presentation.ui.tier.TierListAdapter.Companion.diffUtil

class DetailReviewAdapter(private val context: Context, private val updateHeight: () -> Unit): ListAdapter<CommentDataResponse, DetailReviewAdapter.ViewHolder>(diffUtil) {

    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClicked(data: CommentDataResponse, position: Int, type: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding: ItemDetailReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                binding.clDialog.visibility = View.GONE
            }

            binding.detailIvDots.setOnClickListener {
                if (binding.clDialog.visibility == View.GONE) {
                    binding.clDialog.visibility = View.VISIBLE
                } else {
                    binding.clDialog.visibility = View.GONE
                }
            }
        }
        fun bind(item: CommentDataResponse) {
            binding.tvGrade.text = item.commentScore.toString()
            binding.tvUserName.text = item.commentNickname
            binding.tvReview.text = item.commentBody
            binding.tvLike.text = item.commentLikeCount.toString()
            binding.tvHate.text = item.commentDislikeCount.toString()
            Log.d("img", item.commentIconImgUrl)
            Glide.with(context)
                .load(item.commentIconImgUrl)
                .into(binding.ivUserImage)

            val gradeAdapter = DetailGradeAdapter(item.commentScore)
            binding.rvGrade.adapter = gradeAdapter
            binding.rvGrade.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)

            val replyAdapter = DetailRelyAdapter(item.commentReplies)
            binding.detailRvReply.adapter = replyAdapter
            binding.detailRvReply.layoutManager = LinearLayoutManager(binding.root.context)



            binding.clReport.setOnClickListener {
                itemClickListener.onItemClicked(item, adapterPosition, 1)
            }

            binding.clDelete.setOnClickListener {
                itemClickListener.onItemClicked(item, adapterPosition, 2)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailReviewAdapter.ViewHolder {
        val binding = ItemDetailReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailReviewAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<CommentDataResponse>() {
            override fun areItemsTheSame(oldItem: CommentDataResponse, newItem: CommentDataResponse): Boolean =
                oldItem.commentId == newItem.commentId

            override fun areContentsTheSame(oldItem: CommentDataResponse, newItem: CommentDataResponse): Boolean =
                oldItem == newItem
        }
    }
}
