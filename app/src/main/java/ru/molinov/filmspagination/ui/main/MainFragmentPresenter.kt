package ru.molinov.filmspagination.ui.main

import android.util.Log
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.molinov.filmspagination.R
import ru.molinov.filmspagination.model.Film
import ru.molinov.filmspagination.model.Films
import ru.molinov.filmspagination.navigation.Screens
import ru.molinov.filmspagination.remote.FilmsRepo
import java.util.*

class MainFragmentPresenter(
    private val data: FilmsRepo,
    private val router: Router
) : MvpPresenter<MainFragmentView>() {

    private val callback = object : Callback<Films> {
        override fun onResponse(
            call: Call<Films>,
            response: Response<Films>
        ) {
            val serverResponse: Films? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                filmsListPresenter.films.addAll(serverResponse.films)
                val genres: MutableSet<String> = mutableSetOf()
                serverResponse.films.forEach {
                    it.genres?.forEach { genre ->
                        genres.add(genre)
                    }
                }
                filmsListPresenter.genres.addAll(genres)
                filmsListPresenter.apply {
                    data =
                        (listOf(MainAdapter.Constants.GENRES_DELIMITER) + TreeSet(genres)
                                + MainAdapter.Constants.FILMS_DELIMITER + TreeSet(films)).toList()
                    cachedData = data.toList()
                }
                viewState.renderData()
            } else {
                Log.d(TAG, "empty data " + response.code())
            }
        }

        override fun onFailure(call: Call<Films>, t: Throwable) {
            Log.d(TAG, "error " + t.stackTraceToString())
            viewState.showAlertDialog(R.string.callback_failure)
        }
    }

    inner class FilmsListPresenter : ListPresenter<MainAdapter.BaseViewHolder> {

        var films = mutableListOf<Film>()
        var genres = mutableListOf<String>()
        var data: List<Any> = mutableListOf()
        var cachedData: List<Any> = mutableListOf()
        internal var selectedItem: Int = -1
        override var itemCLickListener: ((MainAdapter.BaseViewHolder) -> Unit)? = null

        override fun getCount(): Int = data.size

        override fun bind(view: MainAdapter.BaseViewHolder) {
            when (view) {
                is MainAdapter.GenreViewHolder -> bindGenre(view)
                is MainAdapter.FilmsViewHolder -> bindFilm(view)
                is MainAdapter.DelimiterViewHolder -> bindDelimiter(view)
            }
        }

        private fun bindGenre(holder: MainAdapter.GenreViewHolder) {
            holder.loadString(data[holder.pos] as String)
            holder.button.isChecked = holder.pos == selectedItem
        }

        private fun bindFilm(holder: MainAdapter.FilmsViewHolder) {
            val item = data[holder.pos] as Film
            holder.loadString(item.localized_name)
            holder.loadImage(item.image_url)
        }

        private fun bindDelimiter(holder: MainAdapter.DelimiterViewHolder) {
            val constant = data[holder.pos] as MainAdapter.Constants
            holder.loadString(constant.string)

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
            val genre = holder.button.text
            val filteredFilms: MutableList<Film> = mutableListOf()
            films.forEach { film ->
                if (film.genres?.contains(genre) == false) filteredFilms.add(film)
            }
            val newData = cachedData - filteredFilms.toSet()
            val removeRange: List<Int> = data.indices - newData.indices
            data = newData.toList()
            val addRange: List<Int> = newData.indices - data.indices
            if (removeRange.isNotEmpty()) viewState.removeRange(removeRange)
            if (addRange.isNotEmpty()) viewState.addRange(addRange)
            viewState.notifyItemsExclude(selectedItem, data.indices, true)
        }
    }

    fun loadData() {
        data.getFilms(callback)
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    companion object {
        const val TAG = "MainFragmentPresenter"
    }
}