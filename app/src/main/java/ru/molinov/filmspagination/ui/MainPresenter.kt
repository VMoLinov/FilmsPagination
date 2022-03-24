package ru.molinov.filmspagination.ui

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.molinov.filmspagination.navigation.Screens

class MainPresenter(private val router: Router) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(Screens.mainScreen())
    }

    fun backPressed() {
        router.exit()
    }
}
