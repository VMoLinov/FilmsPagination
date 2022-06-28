package ru.molinov.filmspagination.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.molinov.filmspagination.databinding.ItemRecyclerDetailsCastBinding
import ru.molinov.filmspagination.model.Cast
import ru.molinov.filmspagination.ui.imageloader.GlideImageLoader
import ru.molinov.filmspagination.ui.imageloader.ImageLoader


class CastsAdapter(
    private val imageLoader: ImageLoader = GlideImageLoader(),
    diff: DiffUtil.ItemCallback<Cast> = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(o: Cast, n: Cast): Boolean = o.id == n.id
        override fun areContentsTheSame(o: Cast, n: Cast): Boolean = o.name == n.name
    }
) : ListAdapter<Cast, CastsAdapter.CastViewHolder>(diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            ItemRecyclerDetailsCastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class CastViewHolder(private val binding: ItemRecyclerDetailsCastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cast: Cast) = with(binding) {
            castName.text = cast.name
            character.text = cast.character
            imageLoader.loadFilmPoster(cast.profile_path, castImage, progressLayout.progressBar)
        }
    }
}
