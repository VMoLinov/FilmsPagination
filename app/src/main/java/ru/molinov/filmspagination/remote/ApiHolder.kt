package ru.molinov.filmspagination.remote

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.molinov.filmspagination.model.MoviesDTO
import ru.molinov.filmspagination.model.GenresDTO

object ApiHolder {

    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private val api: DataSource by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(DataSource::class.java)
    }

    fun getData(callback: Callback<MoviesDTO>, page: Int) {
        api.loadData(page).enqueue(callback)
    }

    fun getGenres(callback: Callback<GenresDTO>) {
        api.loadGenres().enqueue(callback)
    }
}
