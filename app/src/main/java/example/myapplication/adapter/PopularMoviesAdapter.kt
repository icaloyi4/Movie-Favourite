package example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import example.myapplication.R
import example.myapplication.api.response.MovieResponse
import kotlinx.android.synthetic.main.model_list_movies.view.*
import java.lang.Exception

class PopularMoviesAdapter(private val mContext: Context, private val list: List<MovieResponse>, private var onClickItem: PopularMoviesAdapter.onItemClick) : RecyclerView.Adapter<PopularMoviesAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.model_list_movies,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val itemListModel : MovieResponse = list!![position]

        Picasso.get().load("https://image.tmdb.org/t/p/w500/${itemListModel.backdropPath}")
            .into(holder.view.img_popular, object : Callback{
                override fun onSuccess() {
                    holder.view.img_popular.scaleType = ImageView.ScaleType.CENTER_CROP;
                }

                override fun onError(e: Exception?) {
                    TODO("Not yet implemented")
                }

            })

        holder.view.crd_movie.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                onClickItem.itemClick(itemListModel)
            }

        })


    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    interface onItemClick{
       fun itemClick(item : MovieResponse);
    }
}