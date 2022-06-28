package ru.molinov.filmspagination.ui

import android.os.Bundle
import android.view.MenuItem
import moxy.MvpAppCompatActivity
import ru.molinov.filmspagination.databinding.ActivityMainBinding
import ru.molinov.filmspagination.ui.main.MainFragment

class MainActivity : MvpAppCompatActivity(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, MainFragment())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFragmentManager.popBackStack()
        }
        return true
    }

    fun getToolBar() = supportActionBar
}
