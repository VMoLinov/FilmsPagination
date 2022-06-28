package ru.molinov.filmspagination.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.molinov.filmspagination.BuildConfig
import ru.molinov.filmspagination.model.MoviesDTO
import ru.molinov.filmspagination.model.GenresDTO
import ru.molinov.filmspagination.model.MovieCreditsDTO

interface DataSource {

    @GET("discover/movie?$TOKEN")
    fun loadData(@Query("page") page: Int): Call<MoviesDTO>

    @GET("genre/movie/list?$TOKEN")
    fun loadGenres(): Call<GenresDTO>

    @GET("movie/{movieId}/credits?$TOKEN")
    fun loadCredits(@Path("movieId") movieId: Int): Call<MovieCreditsDTO>

    companion object {
        const val TOKEN = "api_key=${BuildConfig.FILMS_API_KEY}"
    }
}
