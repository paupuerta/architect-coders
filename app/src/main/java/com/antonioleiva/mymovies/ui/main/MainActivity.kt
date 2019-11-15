package com.antonioleiva.mymovies.ui.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.antonioleiva.mymovies.R
import com.antonioleiva.mymovies.ui.common.PermissionRequester
import com.antonioleiva.mymovies.ui.common.app
import com.antonioleiva.mymovies.ui.common.getViewModel
import com.antonioleiva.mymovies.ui.common.startActivity
import com.antonioleiva.mymovies.ui.detail.DetailActivity
import com.antonioleiva.mymovies.ui.main.MainViewModel.UiModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MoviesAdapter
    private val coarsePermissionRequester =
        PermissionRequester(this, ACCESS_COARSE_LOCATION)

    private lateinit var component: MainActivityComponent
    private val viewModel: MainViewModel by lazy { getViewModel { component.mainViewModel } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        component = app.component.plus(MainActivityModule())

        adapter = MoviesAdapter(viewModel::onMovieClicked)
        recycler.adapter = adapter
        viewModel.model.observe(this, Observer(::updateUi))
    }

    private fun updateUi(model: UiModel) {

        progress.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE

        when (model) {
            is UiModel.Content -> adapter.movies = model.movies
            is UiModel.Navigation -> startActivity<DetailActivity> {
                putExtra(DetailActivity.MOVIE, model.movie.id)
            }
            UiModel.RequestLocationPermission -> coarsePermissionRequester.request {
                viewModel.onCoarsePermissionRequested()
            }
        }
    }
}