package ru.molinov.filmspagination.model

import retrofit2.Callback
import ru.molinov.filmspagination.remote.ApiHolder
import ru.molinov.filmspagination.remote.FilmsRepo

class FilmsRepoImpl : FilmsRepo {
    override fun getFilms(callback: Callback<Films>) = ApiHolder.getData(callback)
}
