package example.myapplication.mvvm.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import example.myapplication.R
import example.myapplication.api.response.MovieResponse
import example.myapplication.utils.Utils
import kotlinx.android.synthetic.main.activity_detail.*
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val model: MovieResponse? = intent.getSerializableExtra("movie") as MovieResponse?
        if (model != null) {
            Picasso.get().load("https://image.tmdb.org/t/p/w500/${model.backdropPath}")
                .into(img_foto, object : Callback {
                    override fun onSuccess() {
                        img_foto.scaleType = ImageView.ScaleType.CENTER_CROP;
                    }

                    override fun onError(e: Exception?) {
                        TODO("Not yet implemented")
                    }

                })
            txt_title.text = model.title.toString()
            txt_release.text = Utils.changeDateFormat(model.releaseDate.toString())
            txt_content.text = model.overview.toString()
        }
    }
}
