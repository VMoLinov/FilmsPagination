package ru.molinov.filmspagination.ui.imageloader

import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ru.molinov.filmspagination.R

class GlideImageLoader : ImageLoader {

    private val handler = Handler(Looper.getMainLooper())

    override fun loadFilmPoster(url: String?, imageView: ImageView) {
        if (!url.isNullOrEmpty()) loadUrl(url, imageView, true)
        else loadDrawableError(imageView)
    }

    override fun loadFilmBackDrop(url: String?, imageView: ImageView) {
        if (!url.isNullOrEmpty()) loadUrl(url, imageView, false)
        else loadDrawableError(imageView)
    }

    private fun loadUrl(url: String, imageView: ImageView, isCircle: Boolean) {
        val radius: Int =
            if (isCircle) imageView.context.resources.getDimensionPixelSize(R.dimen.corner_radius)
            else 1
        Glide.with(imageView.context)
            .load(BASE_URL + url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    handler.post { loadDrawableError(imageView) }
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .centerCrop()
            .transform(RoundedCorners(radius))
            .into(imageView)
    }

    private fun loadDrawableError(imageView: ImageView) {
        imageView.scaleType = ImageView.ScaleType.CENTER
        Glide.with(imageView.context)
            .load(R.drawable.ic_load_error_vector)
            .into(imageView)
    }

    companion object {
        const val BASE_URL = "https://image.tmdb.org/t/p/w500/"
    }
}
