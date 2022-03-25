package ru.molinov.filmspagination.ui.main

import android.util.Log
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

    private val dataCallback = object : Callback<FilmsDTO> {
        override fun onResponse(
            call: Call<FilmsDTO>,
            response: Response<FilmsDTO>
        ) {
            val serverResponse: FilmsDTO? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                filmsListPresenter.films.addAll(serverResponse.results)
                val genres: MutableSet<Int> = mutableSetOf()
                serverResponse.results.forEach {
                    it.genre_ids?.forEach { genre ->
                        genres.add(genre)
                    }
                }
                filmsListPresenter.genres.addAll(genres)
                filmsListPresenter.apply {
                    if (cachedData.isNotEmpty()) {
                        val cachedInt = mutableSetOf<Int>()
                        val cachedFilms = mutableSetOf<Film>()
                        cachedData.forEach {
                            if (it is Int) cachedInt.add(it)
                            if (it is Film) cachedFilms.add(it)
                        }
                        data =
                            ((cachedInt + genres).toSet() + MainAdapter.Companion.Delimiter()
                                    + (cachedFilms + films).toSet()).toList()
                    } else {
                        data = (genres + MainAdapter.Companion.Delimiter() + films).toList()
                        cachedData = data.toList()
                    }
                }
                viewState.renderData()
            } else {
                Log.d(TAG, "empty data " + response.code())
            }
        }

        override fun onFailure(call: Call<FilmsDTO>, t: Throwable) {
            Log.d(TAG, "error data " + t.stackTraceToString())
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
                filmsListPresenter.genresIds.addAll(serverResponse)
                api.getData(dataCallback, page++)
            } else {
                Log.d(TAG, "empty genres " + response.code())
            }
        }

        override fun onFailure(call: Call<GenresDTO>, t: Throwable) {
            Log.d(TAG, "error genres " + t.stackTraceToString())
            viewState.showAlertDialog(R.string.callback_genres_failure)
        }
    }

    inner class FilmsListPresenter : ListPresenter<MainAdapter.BaseViewHolder> {

        var films = mutableSetOf<Film>()
        var genres = mutableSetOf<Int>()
        var genresIds = mutableSetOf<Genre>()
        var data: List<Any> = mutableListOf()
        var cachedData: List<Any> = mutableListOf()
        internal var selectedItem: Int = -1
        override var itemCLickListener: ((MainAdapter.BaseViewHolder) -> Unit)? = null

        override fun getCount(): Int = data.size

        override fun bind(view: MainAdapter.BaseViewHolder) {
            when (view) {
                is MainAdapter.GenreViewHolder -> bindGenre(view)
                is MainAdapter.FilmsViewHolder -> bindFilm(view)
            }
        }

        private fun bindGenre(holder: MainAdapter.GenreViewHolder) {
            val id = data[holder.pos]
            genresIds.forEach {
                if (it.id == id) holder.loadTitle(it.name)
            }
            holder.button.isChecked = holder.pos == selectedItem
        }

        private fun bindFilm(holder: MainAdapter.FilmsViewHolder) {
            val item = data[holder.pos] as Film
            holder.loadTitle(item.title)
            holder.loadImage(item.poster_path)
            holder.loadReleased(item.release_date.toString())
            holder.loadRating(item.vote_average)
        }

        fun restoreData() {
            data = cachedData.toList()
            viewState.notifyItemsExclude(selectedItem, data.indices, false)
            selectedItem = -1
        }
    }

    val filmsListPresenter = FilmsListPresenter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadData()
        setListener()
    }

    private fun setListener() {
        filmsListPresenter.apply {
            itemCLickListener = {
                when (it) {
                    is MainAdapter.GenreViewHolder -> {
                        handleFilterReaction(it)
                    }
                    is MainAdapter.FilmsViewHolder -> {
                        router.navigateTo(Screens.detailsScreen(data[it.pos] as Film))
                    }
                }
            }
        }
    }

    private fun handleFilterReaction(holder: MainAdapter.GenreViewHolder) {
        filmsListPresenter.apply {
            selectedItem = holder.pos
            val genre = data[selectedItem] as Int
            val filteredFilms: MutableList<Film> = mutableListOf()
            films.forEach { film ->
                if (film.genre_ids?.contains(genre) == false) filteredFilms.add(film)
            }
            val newData = cachedData - filteredFilms.toSet()
            val removeRange: List<Int> = data.indices - newData.indices
            data = newData.toList()
            val addRange: List<Int> = newData.indices - data.indices
            if (removeRange.isNotEmpty()) viewState.removeRange(removeRange)
            if (addRange.isNotEmpty()) viewState.addRange(addRange)
            viewState.notifyItemsExclude(selectedItem, data.indices, false)
        }
    }

    fun loadData() {
        if (page != 1) api.getData(dataCallback, page++)
        else api.getGenres(genresCallback)
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    companion object {
        const val TAG = "MainFragmentPresenter"
    }
}