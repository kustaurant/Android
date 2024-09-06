package com.kust.kustaurant.presentation.ui.draw

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kust.kustaurant.R
import com.kust.kustaurant.data.model.DrawRestaurantData

class DrawSelectResultAdapter(private val restaurants: MutableList<DrawRestaurantData>) :
    RecyclerView.Adapter<DrawSelectResultAdapter.RestaurantViewHolder>() {

    private var highlightedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_draw_select_result_img, parent, false)
        return RestaurantViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(restaurants[position])

        // Highlight with stroke if needed
        if (position == highlightedPosition) {
            val drawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setStroke(2, ContextCompat.getColor(holder.context, R.color.signature_1))
                cornerRadius = 30f
            }
            holder.imageView.background = drawable
            holder.imageView.setPadding(2)
        } else {
            holder.imageView.background = null
            holder.imageView.setPadding(0, 0, 0, 0)
        }
    }
 
    fun highlightItem(position: Int) {
        val previousHighlightedPosition = highlightedPosition
        highlightedPosition = position
        notifyItemChanged(previousHighlightedPosition)
        notifyItemChanged(position)
    }

    override fun getItemCount() = restaurants.size

    class RestaurantViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.draw_iv_image_Restaurant)

        fun bind(restaurant: DrawRestaurantData) {
            Glide.with(context)
                .load(restaurant.restaurantImgUrl)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(30)))
                .into(imageView)

        }
    }
}