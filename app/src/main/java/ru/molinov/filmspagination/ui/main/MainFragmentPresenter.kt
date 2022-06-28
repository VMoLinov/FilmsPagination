package ru.molinov.filmspagination.ui.main

import moxy.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.molinov.filmspagination.R
import ru.molinov.filmspagination.model.Genre
import ru.molinov.filmspagination.model.GenresDTO
import ru.molinov.filmspagination.model.Movie
import ru.molinov.filmspagination.model.MoviesDTO
import ru.molinov.filmspagination.remote.ApiHolder

class MainFragmentPresenter(
    private val api: ApiHolder,
    private var page: Int = 1
) : MvpPresenter<MainFragmentView>() {

    private var genreId = -1

    private val dataCallback = object : Callback<MoviesDTO> {
        override fun onResponse(
            call: Call<MoviesDTO>,
            response: Response<MoviesDTO>
        ) {
            val serverResponse: MoviesDTO? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                moviesListPresenter.movies.apply {
                    addAll(serverResponse.results)
                    if (genreId == -1) {
                        viewState.renderAllFilms(this)
                    } else filterFilms()
                }
            } else {
                viewState.showError("$TAG\nempty data ${response.code()}")
            }
        }

        override fun onFailure(call: Call<MoviesDTO>, t: Throwable) {
            viewState.showError("$TAG\nerror data ${t.stackTraceToString()}")
            viewState.showAlertDialog(R.string.callback_failure)
        }
    }
    private val genresCallback = object : Callback<GenresDTO> {
        override fun onResponse(
            call: Call<GenresDTO>,
            response: Response<GenresDTO>
        ) {
            val serverResponse: List<Genre>? = response.body()?.genres
            if (response.isSuccessful && serverResponse != null) {
                genresListPresenter.genres.apply {
                    addAll(serverResponse)
                    viewState.renderGenres(this)
                }
            } else {
                viewState.showError("$TAG\nempty genres ${response.code()}")
            }
        }

        override fun onFailure(call: Call<GenresDTO>, t: Throwable) {
            viewState.showError("$TAG\nerror genres ${t.stackTraceToString()}")
            viewState.showAlertDialog(R.string.callback_genres_failure)
        }
    }

    inner class MoviesListPresenter : ListPresenter<Movie> {
        val movies: MutableList<Movie> = mutableListOf()
        var filteredMovies: List<Movie> = listOf()
        override var itemCLickListener: ((Movie?) -> Unit)? = null
    }

    inner class GenresListPresenter : ListPresenter<Genre> {
        val genres: MutableList<Genre> = mutableListOf()
        override var itemCLickListener: ((Genre?) -> Unit)? = null
    }

    val moviesListPresenter = MoviesListPresenter()
    val genresListPresenter = GenresListPresenter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadGenres()
        loadData()
        setListener()
    }

    private fun setListener() {
        moviesListPresenter.itemCLickListener = {
        }
        genresListPresenter.itemCLickListener = {
            if (it != null) {
                genreId = it.id
                filterFilms()
            } else {
                genreId = -1
                viewState.renderAllFilms(moviesListPresenter.movies)
            }
        }
    }

    private fun filterFilms() {
        moviesListPresenter.apply {
            filteredMovies = movies.filter {
                it.genre_ids.contains(genreId)
            }
            viewState.renderFilteredFilms(filteredMovies)
        }
    }

    fun loadData() = api.getData(dataCallback, page++)
    fun loadGenres() = api.getGenres(genresCallback)


    companion object {
        const val TAG = "MainFragmentPresenter"
    }
}
