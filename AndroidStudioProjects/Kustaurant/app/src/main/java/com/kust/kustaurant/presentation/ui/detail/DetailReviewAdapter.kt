package com.kust.kustaurant.presentation.ui.detail

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.databinding.ItemDetailReviewBinding

class DetailReviewAdapter(private val context: Context): ListAdapter<CommentDataResponse, DetailReviewAdapter.ViewHolder>(diffUtil) {

    private lateinit var itemClickListener : OnItemClickListener
    var interactionListener: DetailRelyAdapter.OnItemClickListener? = null

    interface OnItemClickListener {
        fun onReportClicked(commentId: Int)
        fun onDeleteClicked(commentId: Int)
        fun onCommentClicked(commentId: Int)
        fun onLikeClicked(commentId: Int, position: Int)
        fun onDisLikeClicked(commentId: Int, position: Int)

    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding: ItemDetailReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.detailIvDots.setOnClickListener { view ->
                showPopupWindow(view)
            }

            binding.ivComment.setOnClickListener {
                showDialog()
            }
        }

        private fun showDialog() {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.dialog_show_comment, null)
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
                itemClickListener.onCommentClicked(getItem(absoluteAdapterPosition).commentId)
                dialog.dismiss()
            }
            btnCancel.setOnClickListener {
                dialog.dismiss()
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

        fun bind(item: CommentDataResponse) {
            binding.ivLike.setImageResource(getLikeIconResource(item.commentLikeStatus))
            binding.ivHate.setImageResource(getDislikeIconResource(item.commentLikeStatus))

            binding.tvGrade.text = item.commentScore.toString()
            binding.tvReviewTime.text = item.commentTime
            binding.tvUserName.text = item.commentNickname
            binding.tvReview.text = item.commentBody
            binding.tvLike.text = item.commentLikeCount.toString()
            binding.tvHate.text = item.commentDislikeCount.toString()
            Log.d("img", item.commentIconImgUrl)
            Glide.with(context)
                .load(item.commentIconImgUrl)
                .into(binding.ivUserImage)
            if (item.commentImgUrl != null){
                binding.detailCvPhoto.visibility = View.VISIBLE
                Glide.with(context)
                    .load(item.commentImgUrl)
                    .into(binding.detailIvPhoto)
            } else {
                binding.detailCvPhoto.visibility = View.GONE
                Glide.with(context)
                    .clear(binding.detailIvPhoto)
                binding.detailIvPhoto.setImageDrawable(null)
            }

            binding.ivLike.setOnClickListener {
                itemClickListener.onLikeClicked(item.commentId, absoluteAdapterPosition)
            }
            binding.ivHate.setOnClickListener {
                itemClickListener.onDisLikeClicked(item.commentId, absoluteAdapterPosition)
            }

            val gradeAdapter = DetailGradeAdapter(item.commentScore)
            binding.rvGrade.adapter = gradeAdapter
            binding.rvGrade.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)

            val replyAdapter = DetailRelyAdapter(context).apply {
                setOnItemClickListener(object : DetailRelyAdapter.OnItemClickListener{
                    override fun onReportClicked(commentId: Int) {
                        interactionListener?.onReportClicked(commentId)
                    }

                    override fun onDeleteClicked(commentId: Int) {
                        interactionListener?.onDeleteClicked(commentId)
                    }

                    override fun onLikeClicked(commentId: Int, position: Int) {
                        interactionListener?.onLikeClicked(commentId, position)
                    }

                    override fun onDisLikeClicked(commentId: Int, position: Int) {
                        interactionListener?.onDisLikeClicked(commentId, position)
                    }

                })
            }
            binding.detailRvReply.adapter = replyAdapter
            binding.detailRvReply.layoutManager = LinearLayoutManager(binding.root.context)
            replyAdapter.submitList(item.commentReplies)
            replyAdapter.notifyDataSetChanged()
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
