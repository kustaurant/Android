package com.kust.kustaurant.presentation.ui.detail

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.databinding.ItemDetailReviewBinding
import com.kust.kustaurant.presentation.ui.tier.TierListAdapter.Companion.diffUtil
import org.w3c.dom.Text

class DetailReviewAdapter(private val context: Context): ListAdapter<CommentDataResponse, DetailReviewAdapter.ViewHolder>(diffUtil) {

    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener {
        fun onReportClicked(commentId: Int)
        fun onDeleteClicked(commentId: Int)
        fun onCommentClicked(commentId: Int)
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

            btnConfirm.setOnClickListener {
                itemClickListener.onCommentClicked(getItem(adapterPosition).commentId)
                dialog.dismiss()
            }
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }


            dialog.show()
        }

        private fun showPopupWindow(anchorView: View) {
            val inflater = LayoutInflater.from(context)
            val popupView = inflater.inflate(R.layout.popup_review_comment, null)
            val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

            // 신고하기 버튼
            popupView.findViewById<ConstraintLayout>(R.id.cl_report).setOnClickListener {
                itemClickListener.onReportClicked(getItem(adapterPosition).commentId)
                popupWindow.dismiss()
            }

            // 삭제하기 버튼
            popupView.findViewById<ConstraintLayout>(R.id.cl_delete).setOnClickListener {
                itemClickListener.onDeleteClicked(getItem(adapterPosition).commentId)
                popupWindow.dismiss()
            }

            popupWindow.showAsDropDown(anchorView)
        }

        fun bind(item: CommentDataResponse) {
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

            val gradeAdapter = DetailGradeAdapter(item.commentScore)
            binding.rvGrade.adapter = gradeAdapter
            binding.rvGrade.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)

            val replyAdapter = DetailRelyAdapter(binding.root.context)
            binding.detailRvReply.adapter = replyAdapter
            binding.detailRvReply.layoutManager = LinearLayoutManager(binding.root.context)
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
