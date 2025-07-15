package com.kust.kustaurant.presentation.ui.draw

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.data.model.DrawRestaurantData

class DrawSelectResultAdapter(
    val restaurants: MutableList<DrawRestaurantData>
) : RecyclerView.Adapter<DrawSelectResultAdapter.RestaurantViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(restaurant: DrawRestaurantData)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_draw_select_result_img, parent, false)
        return RestaurantViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(restaurants[position])
            holder.imageView.background = null

        holder.itemView.setOnClickListener {
            Log.d("어댑터", "클릭됨: ${restaurants[position].restaurantName}")

            itemClickListener?.onItemClick(restaurants[position])
        }
    }

    override fun getItemCount() = restaurants.size

    class RestaurantViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.draw_iv_image_Restaurant)

        fun bind(restaurant: DrawRestaurantData) {
            Glide.with(context)
                .load(restaurant.restaurantImgUrl)
                .into(imageView)

        }
    }
}