package example.myapplication.mvvm.model

import androidx.lifecycle.LiveData
import example.myapplication.api.ApiClient
import example.myapplication.api.response.MovieResponse
import example.myapplication.database.MovieDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailRepository(private val api: ApiClient, private val movieDao: MovieDao) {

    fun getReview(page : Int, id : String) = api.getReview(id, page )

//    fun getMovieById(id : Int) = movieDao.findFavouriteById(id)
    fun getData(id : Int) = movieDao.findFavouriteById(id)
    suspend fun addFavorite(data : MovieResponse) {
        withContext(Dispatchers.IO) {
            movieDao.insertToFavourite(data)
        }
    }
/*
    suspend fun getMovieById(id : Int) {
        withContext(Dispatchers.IO) {
            data = movieDao.findFavouriteById(id)
        }
    }*/
}