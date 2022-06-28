package ru.molinov.filmspagination.ui.imageloader

import android.view.View
import android.widget.ImageView

interface ImageLoader {
    fun loadFilmPoster(url: String?, imageView: ImageView, progress: View?)
    fun loadFilmBackDrop(url: String?, imageView: ImageView, progress: View?)
}
