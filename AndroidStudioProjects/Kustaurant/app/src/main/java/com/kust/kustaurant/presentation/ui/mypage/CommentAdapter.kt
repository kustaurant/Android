package com.kust.kustaurant.presentation.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.MyCommentResponse
import com.kust.kustaurant.data.model.MyFavoriteResponse
import com.kust.kustaurant.databinding.ItemMyCommentBinding

class CommentAdapter(val context: Context) : ListAdapter<MyCommentResponse, CommentAdapter.ViewHolder>(
    diffUtil) {

    inner class ViewHolder(val binding : ItemMyCommentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : MyCommentResponse){
            binding.tvComment.text = item.postcommentBody
            binding.tvCommentTitle.text = item.postTitle
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentAdapter.ViewHolder {
        val binding = ItemMyCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<MyCommentResponse>() {
            override fun areItemsTheSame(oldItem: MyCommentResponse, newItem: MyCommentResponse): Boolean =
                oldItem.postcommentBody == newItem.postcommentBody

            override fun areContentsTheSame(oldItem: MyCommentResponse, newItem: MyCommentResponse): Boolean =
                oldItem.postcommentBody == newItem.postcommentBody
        }
    }
}