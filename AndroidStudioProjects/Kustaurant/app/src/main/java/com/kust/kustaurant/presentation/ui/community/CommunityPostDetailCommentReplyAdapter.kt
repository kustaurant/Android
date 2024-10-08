package com.kust.kustaurant.presentation.ui.community

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ItemDetailReviewReplyBinding
import com.kust.kustaurant.domain.model.CommunityPostComment

class CommunityPostDetailCommentReplyAdapter(val context : Context) : ListAdapter<CommunityPostComment, CommunityPostDetailCommentReplyAdapter.ViewHolder>(
    diffUtil) {

    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener {
        fun onReportClicked(commentId: Int)
        fun onDeleteClicked(commentId: Int)
        fun onLikeClicked(commentId: Int, position: Int)
        fun onDisLikeClicked(commentId: Int, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding : ItemDetailReviewReplyBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.replyFlDots.setOnClickListener { view ->
                showPopupWindow(view)
            }
        }

        private fun showPopupWindow(anchorView: View) {
            val inflater = LayoutInflater.from(context)
            val layoutRes = if (getItem(absoluteAdapterPosition).isCommentMine) {
                R.layout.popup_review_only_delete
            } else {
                R.layout.popup_review_only_report
            }
            val popupView = inflater.inflate(layoutRes, null)
            val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

            if(layoutRes == R.layout.popup_review_only_report){
                popupView.findViewById<ConstraintLayout>(R.id.cl_report).setOnClickListener {
                    showReportDialog()
                    popupWindow.dismiss()
                }
            } else {
                popupView.findViewById<ConstraintLayout>(R.id.cl_delete)?.setOnClickListener {
                    itemClickListener.onDeleteClicked(getItem(absoluteAdapterPosition).commentId)
                    popupWindow.dismiss()
                }
            }

            popupWindow.showAsDropDown(anchorView)
        }

        private fun showReportDialog() {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.dialog_show_report, null)
            val btnConfirm = view.findViewById<TextView>(R.id.detail_tv_confirm)
            val btnCancel = view.findViewById<TextView>(R.id.detail_tv_cancel)
            val dialog = AlertDialog.Builder(context)
                .setView(view)
                .create()

            dialog.show()

            dialog.window?.apply {
                val displayMetrics = context.resources.displayMetrics
                val width = (displayMetrics.widthPixels * 0.6).toInt()

                setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
                setGravity(Gravity.CENTER)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            btnConfirm.setOnClickListener {
                itemClickListener.onReportClicked(getItem(absoluteAdapterPosition).commentId)
                dialog.dismiss()
            }
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        fun getLikeIconResource(likeStatus: Boolean): Int {
            return when(likeStatus) {
                true -> R.drawable.ic_like_true
                false -> R.drawable.ic_like_false
            }
        }

        fun getDislikeIconResource(likeStatus: Boolean): Int {
            return when(likeStatus) {
                true -> R.drawable.ic_dislike_true
                false -> R.drawable.ic_dislike_false
            }
        }

        fun bind(item: CommunityPostComment) {
            binding.ivLike.setImageResource(getLikeIconResource(item.isLiked))
            binding.ivHate.setImageResource(getDislikeIconResource(item.isDisliked))
            binding.tvUserName.text = item.user.userNickname
            binding.tvReview.text = item.commentBody
            binding.tvLike.text = item.likeCount.toString()
            binding.tvHate.text = item.dislikeCount.toString()
            binding.tvReviewTime.text = item.timeAgo
            Glide.with(context)
                .load(item.user.rankImg)
                .into(binding.ivUserImage)

            binding.flLike.setOnClickListener {
                itemClickListener.onLikeClicked(item.commentId, absoluteAdapterPosition)
            }
            binding.flHate.setOnClickListener {
                itemClickListener.onDisLikeClicked(item.commentId, absoluteAdapterPosition)
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityPostDetailCommentReplyAdapter.ViewHolder {
        val binding = ItemDetailReviewReplyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommunityPostDetailCommentReplyAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<CommunityPostComment>() {
            override fun areItemsTheSame(oldItem: CommunityPostComment, newItem: CommunityPostComment): Boolean =
                oldItem.commentId == newItem.commentId

            override fun areContentsTheSame(oldItem: CommunityPostComment, newItem: CommunityPostComment): Boolean =
                oldItem == newItem
        }
    }
}