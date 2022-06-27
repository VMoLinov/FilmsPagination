package ru.molinov.filmspagination.ui.main

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.molinov.filmspagination.R
import ru.molinov.filmspagination.model.Film
import ru.molinov.filmspagination.model.FilmsDTO
import ru.molinov.filmspagination.model.Genre
import ru.molinov.filmspagination.model.GenresDTO
import ru.molinov.filmspagination.navigation.Screens
import ru.molinov.filmspagination.remote.ApiHolder

class MainFragmentPresenter(
    private val api: ApiHolder,
    private val router: Router,
    private var page: Int = 1
) : MvpPresenter<MainFragmentView>() {

    private var genreId = -1

    private val dataCallback = object : Callback<FilmsDTO> {
        override fun onResponse(
            call: Call<FilmsDTO>,
            response: Response<FilmsDTO>
        ) {
            val serverResponse: FilmsDTO? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                filmsListPresenter.films.apply {
                    addAll(serverResponse.results)
                    if (genreId == -1) {
                        viewState.renderAllFilms(this)
                    } else filterFilms()
                }
            } else {
                viewState.showError("$TAG\nempty data ${response.code()}")
            }
        }

        override fun onFailure(call: Call<FilmsDTO>, t: Throwable) {
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

    inner class FilmsListPresenter : ListPresenter<Film> {
        internal val films: MutableList<Film> = mutableListOf()
        internal var filteredFilms: List<Film> = listOf()
        override var itemCLickListener: ((Film?) -> Unit)? = null
    }

    inner class GenresListPresenter : ListPresenter<Genre> {
        internal val genres: MutableList<Genre> = mutableListOf()
        override var itemCLickListener: ((Genre?) -> Unit)? = null
    }

    val filmsListPresenter = FilmsListPresenter()
    val genresListPresenter = GenresListPresenter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        api.getGenres(genresCallback)
        loadData()
        setListener()
    }

    private fun setListener() {
        filmsListPresenter.itemCLickListener = {
            it?.let { router.navigateTo(Screens.detailsScreen(it)) }
        }
        genresListPresenter.itemCLickListener = {
            if (it != null) {
                genreId = it.id
                filterFilms()
            } else {
                genreId = -1
                viewState.renderAllFilms(filmsListPresenter.films)
            }
        }
    }

    private fun filterFilms() {
        filmsListPresenter.apply {
            filteredFilms = films.filter {
                it.genre_ids?.contains(genreId) ?: false
            }
            viewState.renderFilteredFilms(filteredFilms)
        }
    }

    fun loadData() = api.getData(dataCallback, page++)
    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    companion object {
        const val TAG = "MainFragmentPresenter"
    }
}
