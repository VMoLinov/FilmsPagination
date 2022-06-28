package ru.molinov.filmspagination.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val poster_path: String?,
    val adult: Boolean,
    val overview: String,
    val release_date: String,
    val genre_ids: IntArray,
    val id: Int,
    val original_title: String,
    val original_language: String,
    val title: String,
    val backdrop_path: String?,
    val popularity: Double,
    val vote_count: Int,
    val video: Boolean,
    val vote_average: Float
) : Parcelable, Comparable<Movie> {

    override fun compareTo(other: Movie): Int {
        return when {
            title > other.title -> 1
            title < other.title -> -1
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Movie
        if (id != other.id) return false
        if (title != other.title) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }
}
