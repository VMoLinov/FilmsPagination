package ru.molinov.filmspagination.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film(

    @Expose
    val id: Long,

    @Expose
    val localized_name: String,

    @Expose
    val name: String,

    @Expose
    val year: Long?,

    @Expose
    val rating: Double?,

    @Expose
    val image_url: String?,

    @Expose
    val description: String?,

    @Expose
    val genres: List<String>?

) : Parcelable, Comparable<Film> {
    override fun compareTo(other: Film): Int {
        return when {
            localized_name > other.localized_name -> 1
            localized_name < other.localized_name -> -1
            else -> 0
        }
    }
}
