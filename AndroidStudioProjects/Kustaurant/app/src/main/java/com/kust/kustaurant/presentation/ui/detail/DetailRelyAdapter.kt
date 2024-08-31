package com.kust.kustaurant.presentation.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.R
import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.ReplyDataResponse
import com.kust.kustaurant.databinding.ItemDetailReviewReplyBinding
import com.kust.kustaurant.presentation.ui.tier.TierListAdapter.Companion.diffUtil

class DetailRelyAdapter(val context : Context) : ListAdapter<ReplyDataResponse, DetailRelyAdapter.ViewHolder>(
    diffUtil) {

    private lateinit var itemClickListener : OnItemClickListener


    interface OnItemClickListener {
        fun onReportClicked(commentId: Int)
        fun onDeleteClicked(commentId: Int)
        fun onLikeClicked(commentId: Int)
        fun onDisLikeClicked(commentId: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding : ItemDetailReviewReplyBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.replyIvDots.setOnClickListener { view ->
                showPopupWindow(view)
            }
        }

        private fun showPopupWindow(anchorView: View) {
            val inflater = LayoutInflater.from(context)
            val layoutRes = if (getItem(absoluteAdapterPosition).isCommentMine) {
                R.layout.popup_review_comment
            } else {
                R.layout.popup_review_only_report
            }
            val popupView = inflater.inflate(layoutRes, null)
            val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

            // 신고 버튼 설정
            popupView.findViewById<ConstraintLayout>(R.id.cl_report).setOnClickListener {
                itemClickListener.onReportClicked(getItem(absoluteAdapterPosition).commentId)
                popupWindow.dismiss()
            }

            // 삭제 버튼이 존재하는 경우에만 설정
            popupView.findViewById<ConstraintLayout>(R.id.cl_delete)?.setOnClickListener {
                itemClickListener.onDeleteClicked(getItem(absoluteAdapterPosition).commentId)
                popupWindow.dismiss()
            }

            popupWindow.showAsDropDown(anchorView)
        }


        fun bind(item : ReplyDataResponse){
            binding.tvUserName.text = item.commentNickname
            binding.tvReview.text = item.commentBody
            binding.tvLike.text = item.commentLikeCount.toString()
            binding.tvHate.text = item.commentDislikeCount.toString()
            binding.tvReviewTime.text = item.commentTime
            binding.ivLike.setOnClickListener {
                itemClickListener.onLikeClicked(item.commentId)
            }
            binding.ivHate.setOnClickListener {
                itemClickListener.onDisLikeClicked(item.commentId)
            }
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