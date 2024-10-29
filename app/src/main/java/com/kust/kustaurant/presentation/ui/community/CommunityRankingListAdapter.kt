package com.kust.kustaurant.presentation.ui.community

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.databinding.ItemCommunityRankingBinding
import com.kust.kustaurant.domain.model.CommunityRanking

class CommunityRankingListAdapter :
    ListAdapter<CommunityRanking, CommunityRankingListAdapter.ViewHolder>(communityRankingListDiffUtil) {
    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class ViewHolder(
        private val viewBinding: ItemCommunityRankingBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: CommunityRanking) {
            viewBinding.communityTvNickname.text = item.userNickname
            viewBinding.communityTvItemCommentCnt.text = item.evaluationCount.toString() + "개"
            viewBinding.communityTvRestaurantRank.text = item.rank.toString()

            Glide.with(viewBinding.communityIvUserIcon.context)
                .load(item.rankImg)
                .into(viewBinding.communityIvUserIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCommunityRankingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        Log.d("CommunityRankingAdapter", "Binding item at position $position: $item")
        holder.bind(item)
    }

    companion object {
        val communityRankingListDiffUtil = object : DiffUtil.ItemCallback<CommunityRanking>() {
            override fun areItemsTheSame(oldItem: CommunityRanking, newItem: CommunityRanking): Boolean {
                return oldItem.userNickname == newItem.userNickname
            }

            override fun areContentsTheSame(oldItem: CommunityRanking, newItem: CommunityRanking): Boolean {
                return oldItem == newItem
            }
        }
    }
}



