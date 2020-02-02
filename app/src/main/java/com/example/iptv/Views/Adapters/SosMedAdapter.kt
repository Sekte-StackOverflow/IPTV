package com.example.iptv.Views.Adapters

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.iptv.Models.SocialMedia
import com.example.iptv.R
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso

class SosMedAdapter(data: MutableList<SocialMedia>)
    : BaseQuickAdapter<SocialMedia, BaseViewHolder>(R.layout.social_media_item ,data) {
    override fun convert(helper: BaseViewHolder, item: SocialMedia?) {
        val cirImg = helper.getView<CircularImageView>(R.id.circular_image)
        cirImg.apply {
            circleColor = Color.WHITE
            borderWidth = 8f
            borderColor = Color.WHITE
        }
        Picasso.get()
            .load(item!!.icon)
            .into(cirImg)
        helper.setText(R.id.sosmed_name, item.name)
    }
}