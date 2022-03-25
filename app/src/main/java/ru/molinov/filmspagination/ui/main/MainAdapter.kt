package ru.molinov.filmspagination.ui.main

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import ru.molinov.filmspagination.R
import ru.molinov.filmspagination.databinding.ItemRecyclerMainDelimiterBinding
import ru.molinov.filmspagination.databinding.ItemRecyclerMainFilmBinding
import ru.molinov.filmspagination.databinding.ItemRecyclerMainGenreBinding
import ru.molinov.filmspagination.model.Film
import ru.molinov.filmspagination.ui.imageloader.GlideImageLoader
import ru.molinov.filmspagination.ui.imageloader.ImageLoader

class MainAdapter(
    private val presenter: MainFragmentPresenter.FilmsListPresenter,
    private val imageLoader: ImageLoader = GlideImageLoader()
) : RecyclerView.Adapter<MainAdapter.BaseViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            GENRES -> {
                GenreViewHolder(
                    ItemRecyclerMainGenreBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ).apply {
                    button.setOnClickListener {
                        if (button.isChecked) presenter.itemCLickListener?.invoke(this)
                        else presenter.restoreData()
                    }
                }
            }
            FILMS -> {
                FilmsViewHolder(
                    ItemRecyclerMainFilmBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ).apply { itemView.setOnClickListener { presenter.itemCLickListener?.invoke(this) } }
            }
            else -> {
                DelimiterViewHolder(
                    ItemRecyclerMainDelimiterBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (presenter.data[position]) {
            is Delimiter -> DELIMITERS
            is Int -> GENRES
            is Film -> FILMS
            else -> throw IllegalStateException()
        }
    }

    fun getViewType(position: Int): Int { //return span weight
        return when (presenter.data[position]) {
            is Delimiter -> DELIMITERS
            is Int -> GENRES
            is Film -> FILMS
            else -> throw IllegalStateException()
        }
    }

    override fun getItemCount(): Int = presenter.getCount()

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        presenter.bind(holder.apply { pos = position })
    }

    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract var pos: Int
        abstract fun loadTitle(text: String)
    }

    inner class FilmsViewHolder(private val binding: ItemRecyclerMainFilmBinding) :
        BaseViewHolder(binding.root) {
        override var pos: Int = -1

        override fun loadTitle(text: String) {
            binding.filmName.text = text
        }

        fun loadReleased(text: String) {
            binding.released.text = text
        }

        fun loadImage(url: String?) {
            imageLoader.loadFilmPoster(url, binding.filmImage)
        }

        fun loadRating(vote: Float?) = with(binding) {
            val resources = textRating.context.resources
            when {
                vote == null || vote == 0f -> textRating.text =
                    resources.getString(R.string.no_rating)
                vote <= 4 -> setRating(resources, (vote * 10).toInt(), R.drawable.rate_circle_red)
                vote < 7 -> setRating(resources, (vote * 10).toInt(), R.drawable.rate_circle_yellow)
                else -> setRating(resources, (vote * 10).toInt(), R.drawable.rate_circle_green)
            }
        }

        @SuppressLint("SetTextI18n")
        private fun setRating(resources: Resources, percent: Int, rateCircle: Int) = with(binding) {
            rating.progressDrawable =
                ResourcesCompat.getDrawable(resources, rateCircle, null)
            rating.progress = percent
            textRating.text = "$percent${resources.getString(R.string.percent)}"
        }
    }

    inner class GenreViewHolder(private val binding: ItemRecyclerMainGenreBinding) :
        BaseViewHolder(binding.root) {
        override var pos: Int = -1
        val button: Chip = binding.genre

        override fun loadTitle(text: String) {
            binding.genre.text = text
        }
    }

    inner class DelimiterViewHolder(binding: ItemRecyclerMainDelimiterBinding) :
        BaseViewHolder(binding.root) {
        override var pos: Int = -1
        override fun loadTitle(text: String) {}
    }

    companion object {
        const val FILMS = 2
        const val GENRES = 1
        const val DELIMITERS = 4

        class Delimiter
    }
}
