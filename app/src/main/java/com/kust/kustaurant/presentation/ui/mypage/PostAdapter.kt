package com.kust.kustaurant.presentation.ui.mypage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayoutManager
import com.kust.kustaurant.data.model.MyCommunityListResponse
import com.kust.kustaurant.data.model.MyEvaluateResponse
import com.kust.kustaurant.databinding.ItemMyEvaluateBinding
import com.kust.kustaurant.databinding.ItemMyPostBinding
import com.kust.kustaurant.presentation.common.DimensionUtils.dpToPx
import com.kust.kustaurant.presentation.ui.detail.DetailGradeAdapter

class PostAdapter(val context: Context) : ListAdapter<MyCommunityListResponse, PostAdapter.ViewHolder>(diffUtil) {

    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener {
        fun onPostClicked(postId : Int)
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding : ItemMyPostBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : MyCommunityListResponse){
            binding.myTvPostCategory.text = item.postCategory
            binding.myTvPostTitle.text = item.postTitle
            binding.myTvPostTime.text = item.timeAgo
            binding.myTvPostBody.text = item.postBody
            binding.myTvPostLike.text = item.likeCount.toString()
            binding.myTvPostComment.text = item.commentCount.toString()

            //이미지로 수정 해야함
            if (!item.postBody.isNullOrEmpty()) {
                binding.myCvEvaluateRestaurant.visibility = View.VISIBLE
                Glide.with(context)
                    .load(item.postBody)
                    .into(binding.myIvEvaluateRestaurant)

                val params = binding.myClPostInfo.layoutParams as ViewGroup.MarginLayoutParams
                params.marginEnd = context.dpToPx(22)
                binding.myClPostInfo.layoutParams = params

            } else {
                binding.myCvEvaluateRestaurant.visibility = View.GONE
            }

            binding.myClPost.setOnClickListener {
                itemClickListener.onPostClicked(item.postId)
            }

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostAdapter.ViewHolder {
        val binding = ItemMyPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<MyCommunityListResponse>() {
            override fun areItemsTheSame(oldItem: MyCommunityListResponse, newItem: MyCommunityListResponse): Boolean =
                oldItem.postTitle == newItem.postTitle

            override fun areContentsTheSame(oldItem: MyCommunityListResponse, newItem: MyCommunityListResponse): Boolean =
                oldItem.postTitle == newItem.postTitle
        }
    }

}