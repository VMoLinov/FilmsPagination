package ru.molinov.filmspagination.remote

import retrofit2.Callback
import ru.molinov.filmspagination.model.Films

interface FilmsRepo {
    fun getFilms(callback: Callback<Films>)
}
