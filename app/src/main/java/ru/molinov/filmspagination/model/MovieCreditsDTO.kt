package ru.molinov.filmspagination.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieCreditsDTO(
    val id: Int,
    val cast: List<Cast>,
    val crew: List<Crew>
) : Parcelable
