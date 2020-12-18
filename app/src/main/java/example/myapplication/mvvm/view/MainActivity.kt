package example.myapplication.mvvm.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import example.myapplication.R
import example.myapplication.adapter.PopularMoviesAdapter
import example.myapplication.adapter.TopRatedAdapter
import example.myapplication.api.LoadingState
import example.myapplication.api.response.MovieResponse
import example.myapplication.mvvm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), PopularMoviesAdapter.onItemClick, TopRatedAdapter.onItemRatedClick {
    private val mv by viewModel<MainViewModel>()

    var adapterNowPlaying : TopRatedAdapter? = null
    var listNowPlaying : ArrayList<MovieResponse> = arrayListOf()
    var pastVisiblesItemsNowPlaying = 0
    var visibleItemCountNowPlaying:Int = 0
    var totalItemCountNowPlaying:Int = 0
    var countNowPlaying:Int =1

    var adapterPopular : PopularMoviesAdapter? = null
    var listMovie : ArrayList<MovieResponse> = arrayListOf()
    var pastVisiblesItemsPopular = 0
    var visibleItemCountPopular:Int = 0
    var totalItemCountPopular:Int = 0
    var countPopular:Int =1

    var adapterTopRated : TopRatedAdapter? = null
    var listTopRated : ArrayList<MovieResponse> = arrayListOf()
    var pastVisiblesItemsTopRated = 0
    var visibleItemCountTopRated:Int = 0
    var totalItemCountTopRated:Int = 0
    var countTopRated:Int =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Home"

        initRecycleLayout()
        fetchDataAll()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(
            R.menu.menu_home,
            menu
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.action_one) {
            val i = Intent(this, FavouriteActivity::class.java)
            startActivity(i)
            return true
        }

        return super.onOptionsItemSelected(item)

    }

    private fun fetchDataAll() {
        mv.movieData.observe(this, Observer {
            it.results?.let { it1 -> listMovie.addAll(it1) }
            adapterPopular!!.notifyDataSetChanged()
        })

        mv.topRateData.observe(this, Observer {
            it.results?.let { it1 -> listTopRated.addAll(it1) }
            adapterTopRated!!.notifyDataSetChanged()
        })

        mv.nowPlayingData.observe(this, Observer {
            it.results?.let { it1 -> listNowPlaying.addAll(it1) }
            adapterNowPlaying!!.notifyDataSetChanged()
        })


        mv.loadingState.observe(this, Observer {
            when (it.status) {
                LoadingState.Status.FAILED -> {
                    Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initRecycleLayout() {

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        lv_popular.layoutManager = layoutManager

        lv_popular.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dx > 0) //check for scroll down
                {
                    visibleItemCountPopular = layoutManager.getChildCount()
                    totalItemCountPopular = layoutManager.getItemCount()
                    pastVisiblesItemsPopular = layoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCountPopular + pastVisiblesItemsPopular >= totalItemCountPopular) {
                        countPopular++
                        mv.repo.getPopularByPage(countPopular)
                            .enqueue(mv.callbackMovie)
                    }
                }
            }
        })

        adapterPopular = PopularMoviesAdapter(this, listMovie, this)
        lv_popular.adapter = adapterPopular

        val layoutManagerTop = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        lv_top_rated.layoutManager = layoutManagerTop

        adapterTopRated = TopRatedAdapter(this, listTopRated,this)
        lv_top_rated.adapter = adapterTopRated

            lv_top_rated.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dx > 0) //check for scroll down
                    {
                        visibleItemCountTopRated = layoutManagerTop.getChildCount()
                        totalItemCountTopRated = layoutManagerTop.getItemCount()
                        pastVisiblesItemsTopRated = layoutManagerTop.findFirstVisibleItemPosition()
                        if (visibleItemCountTopRated + pastVisiblesItemsTopRated >= totalItemCountTopRated) {
                            countTopRated++
                            mv.repo.getTopRateByPage(countTopRated).enqueue(mv.callbackTopRated)
                        }
                    }
                }
            })

        val layoutManagerNow = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        lv_now_playing.layoutManager = layoutManagerNow

        adapterNowPlaying = TopRatedAdapter(this, listNowPlaying,this)
        lv_now_playing.adapter = adapterNowPlaying

        lv_now_playing.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dx > 0) //check for scroll down
                {
                    visibleItemCountNowPlaying = layoutManagerNow.getChildCount()
                    totalItemCountNowPlaying = layoutManagerNow.getItemCount()
                    pastVisiblesItemsNowPlaying = layoutManagerNow.findFirstVisibleItemPosition()
                    if (visibleItemCountNowPlaying + pastVisiblesItemsNowPlaying >= totalItemCountNowPlaying) {
                        countNowPlaying++
                        mv.repo.getNowPlayingByPage(countNowPlaying).enqueue(
                            mv.callbackNowPlaying
                        )
                    }
                }
            }
        })
    }

    override fun itemClick(item: MovieResponse) {

        val details = item
        val i = Intent(this, DetailActivity::class.java)
        i.putExtra("movie", details)
        startActivity(i)
    }
}
