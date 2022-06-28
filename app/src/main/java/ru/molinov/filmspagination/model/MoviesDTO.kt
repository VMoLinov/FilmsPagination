package ru.molinov.filmspagination.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoviesDTO(
    val page: Int,
    val results: List<Movie>,
    val total_results: Int,
    val total_pages: Int
) : Parcelable
