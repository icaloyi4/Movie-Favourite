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
import example.myapplication.utils.Utils
import kotlinx.android.synthetic.main.model_list_movies_top_rated.view.*
import java.lang.Exception

class TopRatedAdapter(private val mContext: Context, private val list: List<MovieResponse>, private var onClickItem: TopRatedAdapter.onItemRatedClick) : RecyclerView.Adapter<TopRatedAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.model_list_movies_top_rated,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val itemListModel : MovieResponse = list!![position]

        holder.view.txt_title.text = itemListModel.title.toString()
        holder.view.txt_release.text = Utils.changeDateFormat(itemListModel.releaseDate.toString())

        Picasso.get().load("https://image.tmdb.org/t/p/w342/${itemListModel.posterPath}")
            .into(holder.view.img_top_rated, object : Callback{
                override fun onSuccess() {
                    holder.view.img_top_rated.scaleType = ImageView.ScaleType.FIT_XY;
                }

                override fun onError(e: Exception?) {
                }

            })

        holder.view.cardView.setOnClickListener { onClickItem.itemClick(itemListModel) }


    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    interface onItemRatedClick{
        fun itemClick(item : MovieResponse);
    }
}