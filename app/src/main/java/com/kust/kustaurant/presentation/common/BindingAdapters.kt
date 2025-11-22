package com.kust.kustaurant.presentation.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.decode.SvgDecoder
import coil.load

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        if (url != null && url.isNotEmpty()) {
            view.load(url) {
                crossfade(true)
                if (url.endsWith(".svg", ignoreCase = true)) {
                    decoderFactory(SvgDecoder.Factory())
                }
            }
        } else {
            view.setImageDrawable(null)
        }
    }
}