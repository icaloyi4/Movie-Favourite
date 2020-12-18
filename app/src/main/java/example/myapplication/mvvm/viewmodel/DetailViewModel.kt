package example.myapplication.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import example.myapplication.api.LoadingState
import example.myapplication.api.response.MovieResponse
import example.myapplication.api.response.ReviewResponse
import example.myapplication.mvvm.model.DetailRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(val repo: DetailRepository) : ViewModel()  {
    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _reviewData = MutableLiveData<ReviewResponse>()
    val reviewData: LiveData<ReviewResponse>
        get() = _reviewData

    private val _movieData = MutableLiveData<List<MovieResponse>>()
    val movieData: LiveData<List<MovieResponse>>
        get() = _movieData

    val callbackMovie: Callback<ReviewResponse> = object :
        Callback<ReviewResponse> {
        override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
            _loadingState.postValue(LoadingState.error(t.message))
        }

        override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
            if (response.isSuccessful) {
                _reviewData.postValue(response.body())
                _loadingState.postValue(LoadingState.LOADED)
            } else {
                _loadingState.postValue(LoadingState.error(response.errorBody().toString()))
            }
        }

    }

    fun getMovieFavouriteByID(id : Int){
        viewModelScope.launch {
            _loadingState.postValue(LoadingState.LOADING)
            _movieData.postValue(repo.getData(id))
            _loadingState.postValue(LoadingState.LOADED)
        }
    }

    fun addMovieFavourite(data : MovieResponse){
        viewModelScope.launch {
            _loadingState.postValue(LoadingState.LOADING)
            repo.addFavorite(data)
            data.id?.let { getMovieFavouriteByID(it) }
            _loadingState.postValue(LoadingState.LOADED)
        }
    }
}

