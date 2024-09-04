package com.kust.kustaurant.presentation.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.domain.model.SearchRestaurant

class SearchAdapter(private var restaurantList: List<SearchRestaurant>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantName: TextView = itemView.findViewById(R.id.search_tv_restaurant_Name)
        val restaurantDetails: TextView = itemView.findViewById(R.id.search_tv_restaurant_details)
        val restaurantImage: ImageView = itemView.findViewById(R.id.search_iv_restaurant_img)
        val restaurantTierImage: ImageView = itemView.findViewById(R.id.search_iv_restaurant_tier_img)
        val restaurantFavoriteImage: ImageView = itemView.findViewById(R.id.search_iv_restaurant_favorite_img)
        val restaurantEvaluationImage: ImageView = itemView.findViewById(R.id.search_iv_restaurant_evaluation_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_restaurant, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.restaurantName.text = restaurant.restaurantName
        holder.restaurantDetails.text = "${restaurant.restaurantCuisine} | ${restaurant.restaurantPosition}"

        // 조건부 UI 요소 예시
        if (restaurant.isFavorite) {
            holder.restaurantFavoriteImage.visibility = View.VISIBLE
        } else {
            holder.restaurantFavoriteImage.visibility = View.GONE
        }

        if (restaurant.isEvaluated) {
            holder.restaurantEvaluationImage.visibility = View.VISIBLE
        } else {
            holder.restaurantEvaluationImage.visibility = View.GONE
        }

        // 식당 이미지 로드 - 예시로 Glide나 Picasso 같은 이미지 로딩 라이브러리를 사용할 수 있음
        Glide.with(holder.itemView.context)
            .load(restaurant.restaurantImgUrl)
            .into(holder.restaurantImage)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    fun updateData(newList: List<SearchRestaurant>) {
        restaurantList = newList
        notifyDataSetChanged()
    }
}