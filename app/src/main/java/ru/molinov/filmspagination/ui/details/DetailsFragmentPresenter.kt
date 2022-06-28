package ru.molinov.filmspagination.ui.details

import moxy.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.molinov.filmspagination.R
import ru.molinov.filmspagination.model.Movie
import ru.molinov.filmspagination.model.MovieCreditsDTO
import ru.molinov.filmspagination.model.MoviesDTO
import ru.molinov.filmspagination.remote.ApiHolder
import ru.molinov.filmspagination.ui.main.MainFragmentPresenter

class DetailsFragmentPresenter(
    private val api: ApiHolder,
    private val movie: Movie?
) : MvpPresenter<DetailsFragmentView>() {
    private val callback = object : Callback<MovieCreditsDTO> {
        override fun onResponse(
            call: Call<MovieCreditsDTO>,
            response: Response<MovieCreditsDTO>
        ) {
            val serverResponse: MovieCreditsDTO? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                val casts = serverResponse.cast
                viewState.showCasts(casts)
            } else {
                viewState.showError("${TAG}\nempty data ${response.code()}")
            }
        }

        override fun onFailure(call: Call<MovieCreditsDTO>, t: Throwable) {
            viewState.showError("${TAG}\nerror data ${t.stackTraceToString()}")
            viewState.showAlertDialog(R.string.callback_failure)
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        movie?.let {
            viewState.showData(it)
            getCredits()
        }
    }

    fun getCredits() {
        movie?.let { api.getCredits(callback, it.id) }
    }

    companion object {
        const val TAG = "DetailsFragmentPresenter"
    }
}
