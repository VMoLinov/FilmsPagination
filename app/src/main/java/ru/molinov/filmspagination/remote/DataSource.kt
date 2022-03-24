package ru.molinov.filmspagination.remote

import retrofit2.Call
import retrofit2.http.GET
import ru.molinov.filmspagination.model.Films

interface DataSource {
    @GET("films.json")
    fun loadData(): Call<Films>
}
