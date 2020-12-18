package example.myapplication.utils

import android.app.Application
import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import example.myapplication.api.ApiClient
import example.myapplication.database.AppDatabase
import example.myapplication.database.MovieDao
import example.myapplication.mvvm.model.DetailRepository
import example.myapplication.mvvm.model.FavouriteRepository
import example.myapplication.mvvm.model.MainRepository
import example.myapplication.mvvm.viewmodel.DetailViewModel
import example.myapplication.mvvm.viewmodel.FavouriteViewModel
import example.myapplication.mvvm.viewmodel.MainViewModel
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Array.get

val viewModelModule = module {
    viewModel {
        MainViewModel(get())
//        DetailViewModel(get())
    }
    viewModel {
//        MainViewModel(get())
        DetailViewModel(get())
    }

    viewModel {
//        MainViewModel(get())
        FavouriteViewModel(get())
    }
}

val repositoryModule = module {
    single {
        MainRepository(get())
    }

    fun provideFavouriteRepository(dao: MovieDao): FavouriteRepository {
        return FavouriteRepository( dao)
    }
    single {
        provideFavouriteRepository(get())
    }

    fun provideDetailRepository(api: ApiClient, dao: MovieDao): DetailRepository {
        return DetailRepository(api, dao)
    }
    single {
        provideDetailRepository(get(),get())
    }
}

val apiModule = module {
    fun provideUseApi(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }

    single { provideUseApi(get()) }
}

val retrofitModule = module {

    fun provideCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    fun provideHttpClient(cache: Cache): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .cache(cache)

        return okHttpClientBuilder.build()
    }

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()
    }

    single { provideCache(androidApplication()) }
    single { provideHttpClient(get()) }
    single { provideGson() }
    single { provideRetrofit(get(), get()) }
}

val databaseModule = module {

    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "movie.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }


    fun provideDao(database: AppDatabase): MovieDao {
        return database.movieDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
}
