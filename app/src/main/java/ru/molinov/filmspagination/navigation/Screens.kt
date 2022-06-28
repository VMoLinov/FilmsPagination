package ru.molinov.filmspagination.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.molinov.filmspagination.model.Movie
import ru.molinov.filmspagination.ui.details.DetailsFragment
import ru.molinov.filmspagination.ui.main.MainFragment

object Screens {
    fun mainScreen() = FragmentScreen { MainFragment() }
    fun detailsScreen(movie: Movie) = FragmentScreen { DetailsFragment.newInstance(movie) }
}
