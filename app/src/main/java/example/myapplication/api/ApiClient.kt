package example.myapplication.api

import example.myapplication.api.response.DataResponse
import example.myapplication.api.response.MovieResponse
import example.myapplication.api.response.ReviewResponse
import example.myapplication.utils.App
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {
    @GET("movie/popular")
    fun getPopular(
        @Query("page") page : Int,
        @Query("api_key") apiKey : String = App.API_KEY
        ): Call<DataResponse<List<MovieResponse>>>

    @GET("movie/top_rated")
    fun getTopRate(
        @Query("page") page : Int,
        @Query("api_key") apiKey : String = App.API_KEY
    ): Call<DataResponse<List<MovieResponse>>>

    @GET("movie/now_playing")
    fun getNowPlaying(
        @Query("page") page : Int,
        @Query("api_key") apiKey : String = App.API_KEY
    ): Call<DataResponse<List<MovieResponse>>>

    @GET("movie/{movie_id}/reviews")
    fun getReview(
        @Path("movie_id") movie_id : String,
        @Query("page") page : Int,
        @Query("api_key") apiKey : String = App.API_KEY
    ): Call<ReviewResponse>
}
