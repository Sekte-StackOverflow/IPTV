package com.example.iptv.Views.Adapters

import android.net.Uri
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.iptv.Models.Channel
import com.example.iptv.R
import com.squareup.picasso.Picasso

class MoviesAdapters(data: MutableList<Channel>?) :
    BaseQuickAdapter<Channel, BaseViewHolder>(R.layout.movie_item, data) {
    override fun convert(helper: BaseViewHolder, item: Channel?) {
        val watcher: Int = 2100
        val poster: ImageView = helper.getView(R.id.movie_poster)
        Picasso.get()
            .load(Uri.parse(item?.imageUrl))
            .into(poster)
        helper.setText(R.id.movie_title, item?.name)
            .setText(R.id.number_watching, "$watcher Watching")
    }
}