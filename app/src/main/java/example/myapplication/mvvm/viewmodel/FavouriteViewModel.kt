package example.myapplication.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import example.myapplication.api.LoadingState
import example.myapplication.api.response.MovieResponse
import example.myapplication.mvvm.model.FavouriteRepository
import kotlinx.coroutines.launch

class FavouriteViewModel(val repo: FavouriteRepository) : ViewModel()  {

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _movieData = MutableLiveData<List<MovieResponse>>()
    val movieData: LiveData<List<MovieResponse>>
        get() = _movieData


    fun getMovieFavouriteLimit(limit : Int){
        viewModelScope.launch {
            _loadingState.postValue(LoadingState.LOADING)
            _movieData.postValue(repo.getMoviesLimit(limit))
            _loadingState.postValue(LoadingState.LOADED)
        }
    }

}