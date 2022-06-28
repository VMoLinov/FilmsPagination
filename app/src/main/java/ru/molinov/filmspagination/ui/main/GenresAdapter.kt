package ru.molinov.filmspagination.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import ru.molinov.filmspagination.databinding.ItemRecyclerMainGenreBinding
import ru.molinov.filmspagination.model.Genre

class GenresAdapter(
    private val presenter: MainFragmentPresenter.GenresListPresenter,
    diff: DiffUtil.ItemCallback<Genre> = object : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(o: Genre, n: Genre): Boolean = o.id == n.id
        override fun areContentsTheSame(o: Genre, n: Genre): Boolean = o.name == n.name
    }
) : ListAdapter<Genre, GenresAdapter.GenresViewHolder>(diff) {
    private var lastChecked: Chip? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        return GenresViewHolder(
            ItemRecyclerMainGenreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply { itemView.setOnClickListener { onItemClick() } }
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class GenresViewHolder(private val binding: ItemRecyclerMainGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {
            binding.genre.apply {
                text = genre.name
                isChecked = lastChecked?.text == text
            }
        }

        fun onItemClick() {
            if (binding.genre.isChecked) {
                lastChecked?.let { it.isChecked = false }
                lastChecked = binding.genre
                presenter.itemCLickListener?.invoke(currentList[adapterPosition])
            } else {
                lastChecked = null
                presenter.itemCLickListener?.invoke(null)
            }
        }
    }
}
