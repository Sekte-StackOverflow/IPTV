package com.example.iptv.Views.Adapters

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.iptv.R
import com.squareup.picasso.Picasso

class DetailImageAdapter(data: MutableList<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_detail_pic, data) {
    override fun convert(helper: BaseViewHolder, item: String?) {
        val img = helper.getView<ImageView>(R.id.item_detail_imgView)
        Picasso.get().load(item).into(img)
    }
}