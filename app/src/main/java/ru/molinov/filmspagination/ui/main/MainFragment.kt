package ru.molinov.filmspagination.ui.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.molinov.filmspagination.R
import ru.molinov.filmspagination.databinding.FragmentMainBinding
import ru.molinov.filmspagination.model.FilmsRepoImpl
import ru.molinov.filmspagination.navigation.BackButtonListener
import ru.molinov.filmspagination.ui.App
import ru.molinov.filmspagination.ui.MainActivity

class MainFragment : MvpAppCompatFragment(), MainFragmentView, BackButtonListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val presenter by moxyPresenter {
        MainFragmentPresenter(
            FilmsRepoImpl(),
            App.instance.router
        )
    }
    private val adapter by lazy { MainAdapter(presenter.filmsListPresenter) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return adapter.getViewType(position)
            }
        }
        binding.recyclerMain.layoutManager = layoutManager
        binding.recyclerMain.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun renderData() {
        binding.recyclerMain.post {
            adapter.notifyDataSetChanged()
            binding.loading.animationView.visibility = View.GONE
        }
    }

    override fun notifyItemsExclude(position: Int, range: IntRange, scroll: Boolean) {
        repeat(range.count()) {
            binding.recyclerMain.post {
                if (it != position) adapter.notifyItemChanged(it)
                if (scroll) binding.recyclerMain.scrollToPosition(range.last)
            }
        }
    }

    override fun removeRange(range: List<Int>) {
        binding.recyclerMain.post {
            adapter.notifyItemRangeRemoved(range.first(), range[range.lastIndex])
        }
    }

    override fun addRange(range: List<Int>) {
        binding.recyclerMain.post {
            adapter.notifyItemRangeInserted(range.first(), range[range.lastIndex])
        }
    }

    override fun showAlertDialog(message: Int) {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(message))
            .setPositiveButton(getString(R.string.repeat)) { dialogInterface, _ ->
                dialogInterface.dismiss()
                presenter.loadData()
            }
            .create()
            .show()
    }

    override fun backPressed(): Boolean {
        return presenter.backPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val actionBar = (activity as MainActivity).getToolBar()
        actionBar?.let {
            it.title = getString(R.string.main_fragment)
            it.setDisplayHomeAsUpEnabled(false)
        }
    }

    companion object {
        const val GRID_SPAN_COUNT = 2 //columns of Grid Layout
    }
}
