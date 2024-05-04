package com.ltu.m7019e.themoviedb.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ltu.m7019e.themoviedb.database.PageType
import com.ltu.m7019e.themoviedb.utils.Constants.RELOAD_PAGE_TAG

import com.ltu.m7019e.themoviedb.viewmodel.MovieDBViewModel


class ReconnectWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val data = inputData.getString(RELOAD_PAGE_TAG)
        when (data) {
            PageType.POPULAR.name -> {
                viewmodel!!.getPopularMovies()
            }
            PageType.TOP_RATED.name -> {
                viewmodel!!.getTopRatedMovies()
            }
            else -> {
                return Result.failure()
            }
        }
        return Result.success()
    }

    companion object {
        private var viewmodel: MovieDBViewModel? = null

        fun setViewModel(viewModel: MovieDBViewModel) {
            viewmodel = viewModel
        }
    }

}