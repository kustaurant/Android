package com.kust.kustaurant.presentation.ui.community

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.databinding.ItemCommunityPostBinding
import com.kust.kustaurant.domain.model.community.CommunityPostListItem
import com.kust.kustaurant.presentation.common.communityRegex

class CommunityPostListAdapter() :
    ListAdapter<CommunityPostListItem, CommunityPostListAdapter.ViewHolder>(
        communityPostListDiffUtil
    ) {
    lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClicked(data : CommunityPostListItem)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(
        private val viewBinding:
        ItemCommunityPostBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClicked(getItem(position))
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: CommunityPostListItem) {
            viewBinding.communityTvCategory.text = item.category
            val imageUrl = item.photoUrl

            if (imageUrl != null) {
                viewBinding.communityIvPostImg.visibility = View.VISIBLE
                Glide.with(viewBinding.communityIvPostImg.context)
                    .load(imageUrl)
                    .into(viewBinding.communityIvPostImg)
            } else {
                viewBinding.communityIvPostImg.visibility = View.GONE
            }

            // <img> 태그 제거 후 나머지 텍스트를 TextView에 적용
            val postBodyWithoutImg = item.body.replace(communityRegex(), "")
            viewBinding.communityTvBoardContent.text = Html.fromHtml(postBodyWithoutImg, Html.FROM_HTML_MODE_LEGACY)

            // 나머지 UI 요소 설정
            viewBinding.communityTvLikeCnt.text = item.totalLikes.toString()
            viewBinding.communityTvCommentCnt.text = item.commentCount.toString()
            viewBinding.communityTvUserNickname.text = item.writernickname
            viewBinding.communityTvPostTitle.text = item.title
            viewBinding.communityTvTimeAgo.text = item.timeAgo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         return ViewHolder(
            ItemCommunityPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val communityPostListDiffUtil = object : DiffUtil.ItemCallback<CommunityPostListItem>() {
            override fun areItemsTheSame(oldItem: CommunityPostListItem, newItem: CommunityPostListItem): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(
                oldItem: CommunityPostListItem,
                newItem: CommunityPostListItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}