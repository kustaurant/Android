package com.example.kustaurant.presentation.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ItemMyCommentBinding

class CommentAdapter(val context: Context, val data : ArrayList<CommentData>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemMyCommentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : CommentData){
            binding.tvComment.text = item.postCommentBody
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
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}