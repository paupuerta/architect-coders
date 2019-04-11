package com.antonioleiva.mymovies

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMovieDbService {
    @GET("discover/movie?sort_by=popularity.desc")
    fun listPopularMoviesAsync(@Query("api_key") apiKey: String): Deferred<MovieDbResult>
}