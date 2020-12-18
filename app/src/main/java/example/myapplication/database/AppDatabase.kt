package example.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import example.myapplication.api.response.MovieResponse

@Database(entities = [MovieResponse::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}