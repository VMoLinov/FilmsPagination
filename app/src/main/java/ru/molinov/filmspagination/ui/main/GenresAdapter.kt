package ru.molinov.filmspagination.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.molinov.filmspagination.databinding.ItemRecyclerMainGenreBinding
import ru.molinov.filmspagination.model.Genre

class GenresAdapter(
    private val presenter: MainFragmentPresenter.GenresListPresenter,
    diff: DiffUtil.ItemCallback<Genre> = object : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(o: Genre, n: Genre): Boolean = o.id == n.id
        override fun areContentsTheSame(o: Genre, n: Genre): Boolean = o.name == n.name
    }
) : ListAdapter<Genre, GenresAdapter.GenresViewHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        return GenresViewHolder(
            ItemRecyclerMainGenreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply { itemView.setOnClickListener { presenter.itemCLickListener?.invoke(currentList[adapterPosition]) } }
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class GenresViewHolder(private val binding: ItemRecyclerMainGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {
            binding.genre.text = genre.name
        }
    }
}
