package com.example.kustaurant.presentation.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kustaurant.databinding.ItemDetailReviewBinding
import com.example.kustaurant.databinding.ItemDetailReviewEndBinding

class DetailReviewAdapter(private val reviewData: ArrayList<ReviewData>,
    private val updateHeight: () -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_NORMAL = 0
        const val VIEW_TYPE_END = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == reviewData.size - 1) VIEW_TYPE_END else VIEW_TYPE_NORMAL
    }

    inner class NormalViewHolder(val binding: ItemDetailReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReviewData) {
            binding.tvGrade.text = item.grade.toString()
            binding.tvUserName.text = item.userName
            binding.tvReview.text = item.userText
            binding.tvLike.text = item.reviewLike.toString()
            binding.tvHate.text = item.reviewHate.toString()

            val replyAdapter = DetailRelyAdapter(item.replyData)
            binding.detailRvReply.adapter = replyAdapter
            binding.detailRvReply.layoutManager = object : LinearLayoutManager(binding.root.context) {
                override fun canScrollVertically(): Boolean = false
            }

            replyAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    super.onChanged()
                    updateHeight()
                }
            })
        }
    }

    inner class EndViewHolder(val binding: ItemDetailReviewEndBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReviewData) {
            binding.viewContent.visibility = View.GONE

            binding.tvGrade.text = item.grade.toString()
            binding.tvUserName.text = item.userName
            binding.tvReview.text = item.userText
            binding.tvLike.text = item.reviewLike.toString()
            binding.tvHate.text = item.reviewHate.toString()

            binding.detailRvReply.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_NORMAL -> NormalViewHolder(ItemDetailReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            VIEW_TYPE_END -> EndViewHolder(ItemDetailReviewEndBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = reviewData[position]
        if (holder is NormalViewHolder) {
            holder.bind(item)
        } else if (holder is EndViewHolder) {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int = reviewData.size
}
