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
        val poster: ImageView = helper.getView(R.id.movie_image)
        val url = "https://alvaindopratama.com/admin-eyeplus/media/img/${item?.imageUrl}"
        Picasso.get()
            .load(Uri.parse(url))
            .into(poster)
        helper.setText(R.id.movie_title, item?.name)
            .setText(R.id.movie_views, "${item!!.views} Watching")
            .setText(R.id.movie_status, item.status.toUpperCase())
    }
}