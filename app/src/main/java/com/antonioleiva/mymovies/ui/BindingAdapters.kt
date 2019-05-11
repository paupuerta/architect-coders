package com.antonioleiva.mymovies.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.antonioleiva.mymovies.ui.common.loadUrl

@BindingAdapter("url")
fun ImageView.bindUrl(url: String?) {
    if (url != null) loadUrl(url)
}