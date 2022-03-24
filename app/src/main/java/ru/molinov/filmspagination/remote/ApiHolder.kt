package ru.molinov.filmspagination.remote

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.molinov.filmspagination.model.Films

object ApiHolder {

    private const val BASE_URL = "https://s3-eu-west-1.amazonaws.com/sequeniatesttask/"
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

    fun getData(callback: Callback<Films>) {
        api.loadData().enqueue(callback)
    }
}
