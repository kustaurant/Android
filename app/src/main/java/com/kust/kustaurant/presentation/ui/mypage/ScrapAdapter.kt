package com.kust.kustaurant.presentation.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.data.model.MyScrapResponse
import com.kust.kustaurant.databinding.ItemMyScrapBinding

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

//            Glide.with(context)
//                .load(item.postTitle)
//                .into(binding.myIvEvaluateRestaurant)

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