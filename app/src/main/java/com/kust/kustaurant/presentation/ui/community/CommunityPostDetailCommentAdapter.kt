package com.kust.kustaurant.presentation.ui.community

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
import coil.decode.SvgDecoder
import coil.load
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ItemDetailReviewBinding
import com.kust.kustaurant.domain.model.community.CommunityPostComment
import com.kust.kustaurant.domain.model.community.LikeEvent

class CommunityPostDetailCommentAdapter(private val context: Context) :
    ListAdapter<CommunityPostComment, CommunityPostDetailCommentAdapter.ViewHolder>(diffUtil) {

    private lateinit var itemClickListener: OnItemClickListener
    var interactionListener: CommunityPostDetailCommentReplyAdapter.OnItemClickListener? = null

    interface OnItemClickListener {
        fun onReportClicked(commentId: Long)
        fun onDeleteClicked(commentId: Long)
        fun onCommentClicked(commentId: Long)
        fun onLikeClicked(commentId: Long, position: Int)
        fun onDisLikeClicked(commentId: Long, position: Int)

    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding: ItemDetailReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.detailFlDots.setOnClickListener { view ->
                showPopupWindow(view)
            }

            binding.flComment.setOnClickListener {
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
            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )

            if (layoutRes == R.layout.popup_review_only_report) {
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

        private fun getLikeIconResource(reaction: LikeEvent?): Int = when (reaction) {
            LikeEvent.LIKE -> R.drawable.ic_like_true
            LikeEvent.DISLIKE, null -> R.drawable.ic_like_false
        }

        private fun getDislikeIconResource(reaction: LikeEvent?): Int = when (reaction) {
            LikeEvent.DISLIKE-> R.drawable.ic_dislike_true
            LikeEvent.LIKE, null  -> R.drawable.ic_dislike_false
        }

        fun bind(item: CommunityPostComment) {
            val reaction = LikeEvent.from(item.reactionType)
            binding.ivLike.setImageResource(getLikeIconResource(reaction))
            binding.ivHate.setImageResource(getDislikeIconResource(reaction))
            binding.rvGrade.visibility = View.GONE
            binding.tvGrade.visibility = View.GONE
            binding.tvReviewTime.text = item.timeAgo
            binding.tvUserName.text = item.writernickname
            binding.tvReview.text = item.body
            binding.tvLike.text = if(item.likeCount < 0) "0" else item.likeCount.toString()
            binding.tvHate.text = if(item.dislikeCount < 0) "0" else item.dislikeCount.toString()

            binding.ivUserImage.load(item.writericonUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_baby_cow)
                error(R.drawable.ic_baby_cow)

                if (item.writericonUrl?.endsWith(".svg", true) == true) {
                    decoderFactory(SvgDecoder.Factory())
                }
            }

            binding.flLike.setOnClickListener {
                itemClickListener.onLikeClicked(item.commentId, absoluteAdapterPosition)
            }
            binding.flHate.setOnClickListener {
                itemClickListener.onDisLikeClicked(item.commentId, absoluteAdapterPosition)
            }

            val replyAdapter = CommunityPostDetailCommentReplyAdapter(context).apply {
                setOnItemClickListener(object :
                    CommunityPostDetailCommentReplyAdapter.OnItemClickListener {
                    override fun onReportClicked(commentId: Long) {
                        interactionListener?.onReportClicked(commentId)
                    }

                    override fun onDeleteClicked(commentId: Long) {
                        interactionListener?.onDeleteClicked(commentId)
                    }

                    override fun onLikeClicked(commentId: Long, position: Int) {
                        interactionListener?.onLikeClicked(commentId, position)
                    }

                    override fun onDisLikeClicked(commentId: Long, position: Int) {
                        interactionListener?.onDisLikeClicked(commentId, position)
                    }
                })
            }

            binding.detailRvReply.adapter = replyAdapter
            binding.detailRvReply.layoutManager = LinearLayoutManager(binding.root.context)
            replyAdapter.submitList(item.replies)
            replyAdapter.notifyItemChanged(absoluteAdapterPosition)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityPostDetailCommentAdapter.ViewHolder {
        val binding =
            ItemDetailReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CommunityPostDetailCommentAdapter.ViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        Log.d("CommunityPostDetailCommentReplyAdapter", "Binding position $position: $item")
        holder.bind(item)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CommunityPostComment>() {
            override fun areItemsTheSame(
                oldItem: CommunityPostComment,
                newItem: CommunityPostComment
            ): Boolean =
                oldItem.commentId == newItem.commentId

            override fun areContentsTheSame(
                oldItem: CommunityPostComment,
                newItem: CommunityPostComment
            ): Boolean {
                val isContentSame =
                    oldItem.commentId == newItem.commentId &&
                    oldItem.likeCount == newItem.likeCount &&
                    oldItem.status == newItem.status &&
                    oldItem.dislikeCount == newItem.dislikeCount &&
                    oldItem.timeAgo == newItem.timeAgo &&
                    oldItem.reactionType == newItem.reactionType

                // 대댓글 리스트 비교
                val areRepliesSame = oldItem.replies.size == newItem.replies.size &&
                        oldItem.replies.zip(newItem.replies).all { (oldReply, newReply) ->
                            oldReply == newReply
                        }
                return isContentSame && areRepliesSame
            }
        }
    }
}
