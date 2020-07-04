package com.example.study.util.binding


import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.study.data.remote.model.Movie
import com.example.study.ui.adapter.MovieAdapter
import com.example.study.ui.detail.DetailActivity
import io.reactivex.subjects.Subject

@BindingAdapter("bind:loadUrl")
fun ImageView.loadUrl(url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}

@BindingAdapter("bind:bindRating")
fun bindRating(ratingBar: RatingBar, rating: String) {
    ratingBar.rating = rating.toFloat() / 2
}

@BindingAdapter("bind:setTitle")
fun TextView.setTitle(movieTitle: String) {
    if (Build.VERSION.SDK_INT >= 24) {
        this.text =
            Html.fromHtml(movieTitle, Build.VERSION.SDK_INT).toString() // for 24 api and more
    } else {
        this.text = Html.fromHtml(movieTitle).toString() // or for older api
    }
}

@BindingAdapter("bind:setItems")
fun RecyclerView.setItems(items: List<Movie>?) {
    items?.let {
        (adapter as? MovieAdapter)?.setItem(items)
    }
}

@BindingAdapter("bind:setMovieItemClickListener")
fun View.setMovieItemClickListener(link: String?) {
    setOnClickListener {
        link?.let {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.MOVIE_URL, link)
            context.startActivity(intent)
        }
    }
}

@BindingAdapter("bind:buttonClick")
fun View.buttonClick(buttonClickSubject: Subject<Unit>) {
    setOnClickListener {
        buttonClickSubject.onNext(Unit)
    }
}
