package example.myapplication.mvvm.model

import example.myapplication.api.ApiClient
import example.myapplication.api.response.MovieResponse
import example.myapplication.database.MovieDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavouriteRepository ( private val movieDao: MovieDao) {
    fun getMoviesLimit(limit : Int) = movieDao.findFavouriteLimit(limit)
}