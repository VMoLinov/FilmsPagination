package ru.molinov.filmspagination.ui.main

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.molinov.filmspagination.R
import ru.molinov.filmspagination.databinding.ItemRecyclerMainFilmBinding
import ru.molinov.filmspagination.model.Film
import ru.molinov.filmspagination.ui.imageloader.GlideImageLoader
import ru.molinov.filmspagination.ui.imageloader.ImageLoader

class FilmsAdapter(
    private val presenter: MainFragmentPresenter.FilmsListPresenter,
    private val imageLoader: ImageLoader = GlideImageLoader(),
    diff: DiffUtil.ItemCallback<Film> = object : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(o: Film, n: Film): Boolean = o.id == n.id
        override fun areContentsTheSame(o: Film, n: Film): Boolean = o.title == n.title
    }
) : ListAdapter<Film, FilmsAdapter.FilmsViewHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder {
        return FilmsViewHolder(
            ItemRecyclerMainFilmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply { itemView.setOnClickListener { presenter.itemCLickListener?.invoke(currentList[adapterPosition]) } }
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: FilmsViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun submitList(list: List<Film>?) {
        super.submitList(list)
        notifyItemChanged(currentList.lastIndex)
    }


    inner class FilmsViewHolder(private val binding: ItemRecyclerMainFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(film: Film) = with(binding) {
            filmName.text = film.title
            released.text = film.release_date
            imageLoader.loadFilmPoster(film.poster_path, filmImage)
            loadRating(film.vote_average)
        }

        private fun loadRating(vote: Float?) = with(binding) {
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
}
