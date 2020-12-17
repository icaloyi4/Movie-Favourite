package example.myapplication.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import example.myapplication.api.LoadingState
import example.myapplication.api.response.DataResponse
import example.myapplication.api.response.MovieResponse
import example.myapplication.mvvm.model.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(val repo: MainRepository) : ViewModel() {
    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _popularData = MutableLiveData<DataResponse<List<MovieResponse>>>()
    val movieData: LiveData<DataResponse<List<MovieResponse>>>
        get() = _popularData

    private val _topRatedData = MutableLiveData<DataResponse<List<MovieResponse>>>()
    val topRateData: LiveData<DataResponse<List<MovieResponse>>>
        get() = _topRatedData

    private val _nowPlayingData = MutableLiveData<DataResponse<List<MovieResponse>>>()
    val nowPlayingData: LiveData<DataResponse<List<MovieResponse>>>
        get() = _nowPlayingData

    val callbackTopRated : Callback<DataResponse<List<MovieResponse>>> = object : Callback<DataResponse<List<MovieResponse>>>{
        override fun onResponse(
            call: Call<DataResponse<List<MovieResponse>>>,
            response: Response<DataResponse<List<MovieResponse>>>
        ) {
            if (response.isSuccessful) {
                _topRatedData.postValue(response.body())
                _loadingState.postValue(LoadingState.LOADED)
            } else {
                _loadingState.postValue(LoadingState.error(response.errorBody().toString()))
            }
        }

        override fun onFailure(call: Call<DataResponse<List<MovieResponse>>>, t: Throwable) {
            _loadingState.postValue(LoadingState.error(t.message))
        }

    }

    val callbackMovie : Callback<DataResponse<List<MovieResponse>>> = object : Callback<DataResponse<List<MovieResponse>>>{
        override fun onFailure(call: Call<DataResponse<List<MovieResponse>>>, t: Throwable) {
            _loadingState.postValue(LoadingState.error(t.message))
        }

        override fun onResponse(call: Call<DataResponse<List<MovieResponse>>>, response: Response<DataResponse<List<MovieResponse>>>) {
            if (response.isSuccessful) {
                _popularData.postValue(response.body())
                _loadingState.postValue(LoadingState.LOADED)
            } else {
                _loadingState.postValue(LoadingState.error(response.errorBody().toString()))
            }
        }

    }

    val callbackNowPlaying : Callback<DataResponse<List<MovieResponse>>> = object : Callback<DataResponse<List<MovieResponse>>>{
        override fun onFailure(call: Call<DataResponse<List<MovieResponse>>>, t: Throwable) {
            _loadingState.postValue(LoadingState.error(t.message))
        }

        override fun onResponse(call: Call<DataResponse<List<MovieResponse>>>, response: Response<DataResponse<List<MovieResponse>>>) {
            if (response.isSuccessful) {
                _nowPlayingData.postValue(response.body())
                _loadingState.postValue(LoadingState.LOADED)
            } else {
                _loadingState.postValue(LoadingState.error(response.errorBody().toString()))
            }
        }

    }

    init {
        fetchData(1)
    }

    private fun fetchData(page : Int) {
        _loadingState.postValue(LoadingState.LOADING)
        repo.getPopularByPage(page).enqueue(callbackMovie)
        repo.getTopRateByPage(page).enqueue(callbackTopRated)
        repo.getNowPlayingByPage(page).enqueue(callbackNowPlaying)
    }
}