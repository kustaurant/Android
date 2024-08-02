package com.example.kustaurant.presentation.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kustaurant.databinding.ItemDetailReviewBinding

class DetailReviewAdapter(private val reviewData: ArrayList<ReviewData>): RecyclerView.Adapter<DetailReviewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemDetailReviewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ReviewData){
            binding.tvGrade.text = item.grade.toString()
            binding.tvUserName.text = item.userName
            binding.tvReview.text = item.userText
            binding.tvLike.text = item.reviewLike.toString()
            binding.tvHate.text = item.reviewHate.toString()

            val replyAdapter = DetailRelyAdapter()
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailReviewAdapter.ViewHolder {
        val binding = ItemDetailReviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailReviewAdapter.ViewHolder, position: Int) {
        holder.bind(reviewData[position])
    }

    override fun getItemCount(): Int = reviewData.size
}