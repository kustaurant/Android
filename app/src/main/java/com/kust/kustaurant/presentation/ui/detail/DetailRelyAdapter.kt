package com.kust.kustaurant.presentation.ui.detail

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
import com.kust.kustaurant.data.model.ReplyDataResponse
import com.kust.kustaurant.databinding.ItemDetailReviewReplyBinding

class DetailRelyAdapter(val context : Context) : ListAdapter<ReplyDataResponse, DetailRelyAdapter.ViewHolder>(
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

        fun getLikeIconResource(likeStatus: Int): Int {
            return when(likeStatus) {
                1 -> R.drawable.ic_like_true
                0 -> R.drawable.ic_like_false
                -1 -> R.drawable.ic_like_false
                else -> R.drawable.ic_like_false
            }
        }

        fun getDislikeIconResource(likeStatus: Int): Int {
            return when(likeStatus) {
                1 -> R.drawable.ic_dislike_false
                0 -> R.drawable.ic_dislike_false
                -1 -> R.drawable.ic_dislike_true
                else -> R.drawable.ic_dislike_false
            }
        }


        fun bind(item : ReplyDataResponse){
            binding.ivLike.setImageResource(getLikeIconResource(item.commentLikeStatus))
            binding.ivHate.setImageResource(getDislikeIconResource(item.commentLikeStatus))

            binding.tvUserName.text = item.commentNickname
            binding.tvReview.text = item.commentBody
            binding.tvLike.text = item.commentLikeCount.toString()
            binding.tvHate.text = item.commentDislikeCount.toString()
            binding.tvReviewTime.text = item.commentTime
            Glide.with(context)
                .load(item.commentIconImgUrl)
                .into(binding.ivUserImage)
            binding.flLike.setOnClickListener {
                itemClickListener.onLikeClicked(item.commentId, absoluteAdapterPosition)
            }
            binding.flHate.setOnClickListener {
                itemClickListener.onDisLikeClicked(item.commentId, absoluteAdapterPosition)
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