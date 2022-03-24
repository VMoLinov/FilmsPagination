package ru.molinov.filmspagination.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
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
            is Constants -> DELIMITERS
            is String -> GENRES
            is Film -> FILMS
            else -> throw IllegalStateException()
        }
    }

    fun getViewType(position: Int): Int { //return span weight
        return when (presenter.data[position]) {
            is Constants -> GENRES
            is String -> GENRES
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
        abstract fun loadString(text: String)
    }

    inner class FilmsViewHolder(private val binding: ItemRecyclerMainFilmBinding) :
        BaseViewHolder(binding.root) {
        override var pos: Int = -1

        override fun loadString(text: String) {
            binding.filmName.text = text
        }

        fun loadImage(url: String?) {
            imageLoader.load(url, binding.filmImage)
        }
    }

    inner class GenreViewHolder(private val binding: ItemRecyclerMainGenreBinding) :
        BaseViewHolder(binding.root) {
        override var pos: Int = -1
        val button: Chip = binding.genre

        override fun loadString(text: String) {
            binding.genre.text = text
        }
    }

    inner class DelimiterViewHolder(private val binding: ItemRecyclerMainDelimiterBinding) :
        BaseViewHolder(binding.root) {
        override var pos: Int = -1

        override fun loadString(text: String) {
            binding.delimiter.text = text
        }
    }

    companion object {
        const val FILMS = 1
        const val GENRES = 2
        const val DELIMITERS = 3
    }

    enum class Constants(val string: String) {
        GENRES_DELIMITER("Жанры"),
        FILMS_DELIMITER("Фильмы")
    }
}
