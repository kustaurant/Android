package com.kust.kustaurant.presentation.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.databinding.ItemDetailReviewBinding

class DetailReviewAdapter(private val reviewData: ArrayList<ReviewData>, private val updateHeight: () -> Unit): RecyclerView.Adapter<DetailReviewAdapter.ViewHolder>() {

    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClicked(data: ReviewData, position: Int, type: Int)
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
        holder.bind(reviewData[position])
    }

    override fun getItemCount(): Int = reviewData.size
}
