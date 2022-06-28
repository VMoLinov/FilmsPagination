package ru.molinov.filmspagination.ui.main

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.molinov.filmspagination.R
import ru.molinov.filmspagination.databinding.ItemRecyclerMainMovieBinding
import ru.molinov.filmspagination.model.Movie
import ru.molinov.filmspagination.ui.details.DetailsFragment
import ru.molinov.filmspagination.ui.imageloader.GlideImageLoader
import ru.molinov.filmspagination.ui.imageloader.ImageLoader

class MoviesAdapter(
    private val presenter: MainFragmentPresenter.MoviesListPresenter,
    private val manager: FragmentManager,
    private val imageLoader: ImageLoader = GlideImageLoader(),
    diff: DiffUtil.ItemCallback<Movie> = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(o: Movie, n: Movie): Boolean = o.id == n.id
        override fun areContentsTheSame(o: Movie, n: Movie): Boolean = o.title == n.title
    }
) : ListAdapter<Movie, MoviesAdapter.FilmsViewHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder {
        return FilmsViewHolder(
            ItemRecyclerMainMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                presenter.itemCLickListener?.invoke(currentList[adapterPosition])
                showDetails()
            }
        }
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: FilmsViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun submitList(list: List<Movie>?) {
        super.submitList(list)
        notifyItemChanged(currentList.lastIndex)
    }

    inner class FilmsViewHolder(private val binding: ItemRecyclerMainMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) = with(binding) {
            movieName.text = movie.title
            released.text = movie.release_date
            imageLoader.loadFilmPoster(movie.poster_path, movieImage, progressLayout.progressBar)
            loadRating(movie.vote_average)
            movieImage.transitionName = movie.title
        }

        fun showDetails() {
            manager.beginTransaction()
                .replace(R.id.container, DetailsFragment.newInstance(currentList[adapterPosition]))
                .addSharedElement(binding.movieImage, "shared")
                .addToBackStack(null)
                .commit()
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
