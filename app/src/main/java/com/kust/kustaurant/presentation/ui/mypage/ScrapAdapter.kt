package com.kust.kustaurant.presentation.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.data.model.MyScrapResponse
import com.kust.kustaurant.databinding.ItemMyScrapBinding
import com.kust.kustaurant.presentation.common.DimensionUtils
import com.kust.kustaurant.presentation.common.DimensionUtils.dpToPx

class ScrapAdapter(val context: Context) : ListAdapter<MyScrapResponse, ScrapAdapter.ViewHolder>(diffUtil) {

    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener {
        fun onPostClicked(postId : Int)
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding : ItemMyScrapBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : MyScrapResponse){
            binding.myTvScrapCategory.text = item.postCategory
            binding.myTvScrapTitle.text = item.postTitle
            binding.myTvScrapTime.text = item.timeAgo
            binding.myTvScrapBody.text = item.postBody
            binding.myTvScrapLike.text = item.likeCount.toString()
            binding.myTvScrapComment.text = item.commentCount.toString()

            //이미지로 수정 해야함
            if (!item.postBody.isNullOrEmpty()) {
                binding.myCvEvaluateRestaurant.visibility = View.VISIBLE
                Glide.with(context)
                    .load(item.postBody)
                    .into(binding.myIvEvaluateRestaurant)

                val params = binding.myClScrapInfo.layoutParams as ViewGroup.MarginLayoutParams
                params.marginEnd = context.dpToPx(22)
                binding.myClScrapInfo.layoutParams = params

            } else {
                binding.myCvEvaluateRestaurant.visibility = View.GONE
            }

            binding.myClScrapInfo.setOnClickListener {
                itemClickListener.onPostClicked(item.postId)
            }

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScrapAdapter.ViewHolder {
        val binding = ItemMyScrapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScrapAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<MyScrapResponse>() {
            override fun areItemsTheSame(oldItem: MyScrapResponse, newItem: MyScrapResponse): Boolean =
                oldItem.postTitle == newItem.postTitle

            override fun areContentsTheSame(oldItem: MyScrapResponse, newItem: MyScrapResponse): Boolean =
                oldItem.postTitle == newItem.postTitle
        }
    }

}