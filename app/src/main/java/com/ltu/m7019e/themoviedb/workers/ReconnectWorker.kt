package com.ltu.m7019e.themoviedb.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

import com.ltu.m7019e.themoviedb.viewmodel.MovieDBViewModel


class ReconnectWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        viewmodel!!.getPopularMovies()
        return Result.success()
    }

    companion object {
        private var viewmodel: MovieDBViewModel? = null

        fun setViewModel(viewModel: MovieDBViewModel) {
            viewmodel = viewModel
        }
    }

}