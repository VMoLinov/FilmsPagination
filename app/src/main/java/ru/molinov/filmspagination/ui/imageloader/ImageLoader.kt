package ru.molinov.filmspagination.ui.imageloader

import android.widget.ImageView

interface ImageLoader {
    fun loadFilmUrl(url: String?, imageView: ImageView)
}
