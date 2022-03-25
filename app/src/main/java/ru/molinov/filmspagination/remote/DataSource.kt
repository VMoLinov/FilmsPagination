package ru.molinov.filmspagination.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.molinov.filmspagination.BuildConfig
import ru.molinov.filmspagination.model.FilmsDTO
import ru.molinov.filmspagination.model.GenresDTO

interface DataSource {

    @GET("discover/movie?api_key=$TOKEN&page=")
    fun loadData(@Query("page") page: Int): Call<FilmsDTO>

    @GET("genre/movie/list?api_key=$TOKEN")
    fun loadGenres(): Call<GenresDTO>

    companion object {
        const val TOKEN = BuildConfig.FILMS_API_KEY
    }
}
