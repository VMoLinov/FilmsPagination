package ru.molinov.filmspagination.ui.imageloader

import android.widget.ImageView

interface ImageLoader {
    fun load(url: String?, imageView: ImageView)
}
