package example.myapplication.mvvm.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import example.myapplication.R
import example.myapplication.adapter.FavouriteMoviesAdapter
import example.myapplication.api.response.MovieResponse
import example.myapplication.mvvm.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.activity_favourite.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteActivity : AppCompatActivity(), FavouriteMoviesAdapter.onItemRatedClick {

    private var countFavorite: Int = 10
    private val mv by viewModel<FavouriteViewModel>()

    var adapterFavourite : FavouriteMoviesAdapter? = null
    var listFavourite : ArrayList<MovieResponse> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "My Favourite List"

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lv_favourite.layoutManager = layoutManager

        lv_favourite.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) //check for scroll down
                {
                    var visibleItemCountPopular = layoutManager.getChildCount()
                    var totalItemCountPopular = layoutManager.getItemCount()
                    var pastVisiblesItemsPopular = layoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCountPopular + pastVisiblesItemsPopular >= totalItemCountPopular) {
                        countFavorite += 10
                        mv.getMovieFavouriteLimit(countFavorite)
                    }
                }
            }
        })

        adapterFavourite = FavouriteMoviesAdapter(this, listFavourite, this)
        lv_favourite.adapter = adapterFavourite

        mv.movieData.observe(this, {
            listFavourite.addAll(it)
            adapterFavourite!!.notifyDataSetChanged()
        })

    }

    override fun onResume() {
        listFavourite.clear()
        mv.getMovieFavouriteLimit(countFavorite)

        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                // API 5+ solution
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun itemClick(item: MovieResponse) {

        val details = item
        val i = Intent(this, DetailActivity::class.java)
        i.putExtra("movie", details)
        startActivity(i)
    }
}
