package example.myapplication.mvvm.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import example.myapplication.R
import example.myapplication.adapter.ReviewAdapter
import example.myapplication.api.LoadingState
import example.myapplication.api.response.MovieResponse
import example.myapplication.api.response.ReviewResponse
import example.myapplication.mvvm.viewmodel.DetailViewModel
import example.myapplication.utils.Utils
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private var countReview: Int = 1
    private val mv by viewModel<DetailViewModel>()

    lateinit var context : Context

    var adapterReview : ReviewAdapter? = null
    var listReview : ArrayList<ReviewResponse.Result> = arrayListOf()

    var model :MovieResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        context = this;

        lin_back.setOnClickListener { finish()}

        model = intent.getSerializableExtra("movie") as MovieResponse?

        if (model!=null) {
            init()
            fetchData()
        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun init(){
        btn_favourite.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if(model!!.isFavourite){
                    Toast.makeText(context, "Remove from faourite list", Toast.LENGTH_SHORT).show()
                    model!!.isFavourite = false
                } else {
                    Toast.makeText(context, "Add to favourite list", Toast.LENGTH_SHORT).show()
                    model!!.isFavourite = true
                }

                mv.addMovieFavourite(model!!)
            }

        })

        Picasso.get().load("https://image.tmdb.org/t/p/w500/${model!!.backdropPath}")
            .into(img_foto, object : Callback {
                override fun onSuccess() {
                    img_foto.scaleType = ImageView.ScaleType.CENTER_CROP;
                }

                override fun onError(e: Exception?) {

                }

            })
        txt_title.text = model!!.title.toString()
        txt_release.text = Utils.changeDateFormat(model!!.releaseDate.toString())
        txt_content.text = model!!.overview.toString()

        btn_share.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "https://image.tmdb.org/t/p/w500/${model!!.backdropPath}\n" +
                            "${model!!.title.toString()} \n " +
                            "${model!!.overview.toString()}")
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }

        })
    }

    private fun fetchData() {
        mv.movieData.observe(this, Observer {
            it.forEach {
                model = it
                if(model!!.isFavourite){
                    btn_favourite.setColorFilter(resources.getColor(android.R.color.holo_red_light))
                } else {
                    btn_favourite.setColorFilter(resources.getColor(android.R.color.black))
                }
            }
        })

        mv.reviewData.observe(this, Observer {
            if (it.getResults().isNotEmpty()){
                txt_no_review.visibility = View.GONE
                listReview.addAll(it.getResults())
            }
            adapterReview!!.notifyDataSetChanged()
        })


        mv.loadingState.observe(this, Observer {
            when (it.status) {
                LoadingState.Status.FAILED -> {
                    Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
                LoadingState.Status.RUNNING -> {
                    progressBar.visibility = View.VISIBLE
                }
                LoadingState.Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onStart() {
        mv.getMovieFavouriteByID(model!!.id!!)
        mv.repo.getReview(countReview, model!!.id.toString())
            .enqueue(mv.callbackMovie)
        initRecycle()
        super.onStart()
    }

    private fun initRecycle() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lv_review.layoutManager = layoutManager

        adapterReview = ReviewAdapter(this, listReview)
        lv_review.adapter = adapterReview

        lv_review.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) //check for scroll down
                {
                    var visibleItemCountReview = layoutManager.getChildCount()
                    var totalItemCountReview = layoutManager.getItemCount()
                    var pastVisiblesItemsReview = layoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCountReview + pastVisiblesItemsReview >= totalItemCountReview) {
                        countReview++
                        mv.repo.getReview(countReview, model!!.id.toString())
                            .enqueue(mv.callbackMovie)
                    }
                }
            }
        })
    }
}
