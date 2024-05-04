package com.ltu.m7019e.themoviedb.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ltu.m7019e.themoviedb.model.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM favorite_movies")
    suspend fun getFavoriteMovies(): List<Movie>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteMovie(movie: Movie)
    @Query("SELECT * FROM favorite_movies WHERE id = :id")
    suspend fun getMovie(id: Long): Movie
    @Query("DELETE FROM favorite_movies WHERE id = :id")
    suspend fun deleteFavoriteMovie(id: Long)

    @Query("DELETE FROM cached_popular")
    suspend fun clearPopular()
    @Query("DELETE FROM cached_top_rated")
    suspend fun clearTopRated()
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun cachePopular(movies: List<CachedPopular>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun cacheTopRated(movies: List<CachedTopRated>)
    @Query("SELECT * FROM cached_popular ORDER BY listNr ASC")
    suspend fun getPopular(): List<CachedPopular>
    @Query("SELECT * FROM cached_top_rated ORDER BY listNr ASC")
    suspend fun getTopRated(): List<CachedTopRated>
}