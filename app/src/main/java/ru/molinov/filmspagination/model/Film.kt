package ru.molinov.filmspagination.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: IntArray?,
    val id: Long,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val title: String,
    val video: Boolean,
    val vote_average: Float?,
    val vote_count: Int?
) : Parcelable, Comparable<Film> {

    override fun compareTo(other: Film): Int {
        return when {
            title > other.title -> 1
            title < other.title -> -1
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Film
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
