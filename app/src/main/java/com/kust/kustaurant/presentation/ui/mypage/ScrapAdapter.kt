package com.kust.kustaurant.presentation.ui.mypage

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.data.model.MyScrapResponse
import com.kust.kustaurant.databinding.ItemMyScrapBinding
import com.kust.kustaurant.presentation.common.DimensionUtils.dpToPx
import com.kust.kustaurant.presentation.common.communityRegex

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
            val postBodyWithoutImg = if (item.body.trim().startsWith("<p><img")) {
                ""
            } else {
                item.body.replace(communityRegex(), "")
            }
            binding.myTvScrapBody.text = Html.fromHtml(postBodyWithoutImg, Html.FROM_HTML_MODE_LEGACY)

            binding.myTvScrapCategory.text = item.postCategory
            binding.myTvScrapTitle.text = item.postTitle
            binding.myTvScrapTime.text = item.timeAgo
            binding.myTvScrapLike.text = item.likeCount.toString()
            binding.myTvScrapComment.text = item.commentCount.toString()

            if (item.postImgUrl != null && item.postImgUrl.isNotEmpty()) {
                binding.myCvEvaluateRestaurant.visibility = View.VISIBLE
                Glide.with(context)
                    .load(item.postImgUrl)
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
                oldItem.postId == newItem.postId

            override fun areContentsTheSame(oldItem: MyScrapResponse, newItem: MyScrapResponse): Boolean =
                oldItem.body == newItem.body
        }
    }

}