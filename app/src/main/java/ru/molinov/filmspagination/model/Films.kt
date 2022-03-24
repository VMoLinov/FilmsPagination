package ru.molinov.filmspagination.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class Films(
    @Expose
    val films: List<Film>
) : Parcelable
