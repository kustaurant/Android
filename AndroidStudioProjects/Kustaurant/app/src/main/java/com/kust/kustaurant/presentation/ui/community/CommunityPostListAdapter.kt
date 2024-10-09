package com.kust.kustaurant.presentation.ui.community

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.databinding.ItemCommunityPostBinding
import com.kust.kustaurant.domain.model.CommunityPost

class CommunityPostListAdapter() :
    ListAdapter<CommunityPost, CommunityPostListAdapter.ViewHolder>(communityPostListDiffUtil) {
    lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClicked(data : CommunityPost)
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
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClicked(getItem(position))
                }
            }
        }

        fun bind(item: CommunityPost) {
            viewBinding.communityTvCategory.text = item.postCategory

            // 이미지 URL 추출 및 제거
            val regex = "<img[^>]+src=\"([^\"]+)\"[^>]*>".toRegex()
            val matchResult = regex.find(item.postBody)
            val imageUrl = matchResult?.groups?.get(1)?.value

            // 이미지가 있을 경우 Glide를 사용해 로드
            if (imageUrl != null) {
                viewBinding.communityIvPostImg.visibility = View.VISIBLE
                Glide.with(viewBinding.communityIvPostImg.context)
                    .load(imageUrl)
                    .into(viewBinding.communityIvPostImg)
            } else {
                viewBinding.communityIvPostImg.visibility = View.GONE
            }

            // <img> 태그 제거 후 나머지 텍스트를 TextView에 적용
            val postBodyWithoutImg = item.postBody.replace(regex, "")
            viewBinding.communityTvBoardContent.text = Html.fromHtml(postBodyWithoutImg, Html.FROM_HTML_MODE_LEGACY)

            // 나머지 UI 요소 설정
            viewBinding.communityTvLikeCnt.text = item.likeCount.toString()
            viewBinding.communityTvCommentCnt.text = item.commentCount.toString()
            viewBinding.communityTvUserNickname.text = item.user.userNickname
            viewBinding.communityTvPostTitle.text = item.postTitle
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
        val communityPostListDiffUtil = object : DiffUtil.ItemCallback<CommunityPost>() {
            override fun areItemsTheSame(oldItem: CommunityPost, newItem: CommunityPost): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(
                oldItem: CommunityPost,
                newItem: CommunityPost
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}