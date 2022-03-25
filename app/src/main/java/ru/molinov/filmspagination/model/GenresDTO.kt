package ru.molinov.filmspagination.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenresDTO(
    val genres: List<Genre>,
) : Parcelable
