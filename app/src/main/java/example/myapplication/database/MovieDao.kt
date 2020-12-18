package example.myapplication.database

import android.database.Observable
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import example.myapplication.api.response.MovieResponse

@Dao
interface MovieDao {
    @Query("Select * from favourite_movie where id = :id")
    fun findFavouriteById(id : Int) : List<MovieResponse>

    @Query("Select * from favourite_movie where isFavourite")
    fun findFavourite() : LiveData<List<MovieResponse>>

    @Query("Select * from favourite_movie where isFavourite limit :limit")
    fun findFavouriteLimit(limit : Int) : List<MovieResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToFavourite(favourite : MovieResponse)
}