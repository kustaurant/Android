package com.kust.kustaurant.presentation.ui.mypage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayoutManager
import com.kust.kustaurant.data.model.MyEvaluateResponse
import com.kust.kustaurant.databinding.ItemMyEvaluateBinding
import com.kust.kustaurant.presentation.ui.detail.DetailGradeAdapter

class EvaluateRestaurantAdapter(val context: Context) : ListAdapter<MyEvaluateResponse, EvaluateRestaurantAdapter.ViewHolder>(diffUtil) {

    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener {
        fun onEvaluateClicked(restaurantId : Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding : ItemMyEvaluateBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : MyEvaluateResponse){
            binding.myTvEvaluateRestaurant.text = item.restaurantName
            binding.myTvEvaluateBody.text = item.evaluationBody
            Glide.with(context)
                .load(item.restaurantImgURL)
                .into(binding.myIvEvaluateRestaurant)

            val keyWordAdapter = EvaluateMyKeyWordAdapter()
            binding.myRvEvaluateCuisine.adapter = keyWordAdapter
            binding.myRvEvaluateCuisine.layoutManager = FlexboxLayoutManager(binding.root.context)
            keyWordAdapter.submitList(item.evaluationItemScores)

            val gradeAdapter = EvaluateGradeAdapter(item.evaluationScore)
            binding.myRvGrade.adapter = gradeAdapter
            binding.myRvGrade.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)

            binding.myClEvaluate.setOnClickListener {
                itemClickListener.onEvaluateClicked(item.restaurantId)
            }

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EvaluateRestaurantAdapter.ViewHolder {
        val binding = ItemMyEvaluateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EvaluateRestaurantAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<MyEvaluateResponse>() {
            override fun areItemsTheSame(oldItem: MyEvaluateResponse, newItem: MyEvaluateResponse): Boolean =
                oldItem.restaurantId == newItem.restaurantId

            override fun areContentsTheSame(oldItem: MyEvaluateResponse, newItem: MyEvaluateResponse): Boolean =
                oldItem.restaurantId == newItem.restaurantId
        }
    }

}