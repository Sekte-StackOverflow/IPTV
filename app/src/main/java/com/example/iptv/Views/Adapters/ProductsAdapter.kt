package com.example.iptv.Views.Adapters

import android.net.Uri
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.iptv.Models.Product
import com.example.iptv.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_item.view.*

class ProductsAdapter(data: MutableList<Product>?) :
    BaseQuickAdapter<Product, BaseViewHolder>(R.layout.product_item ,data) {
    override fun convert(helper: BaseViewHolder, item: Product?) {
        val img: ImageView = helper.getView(R.id.img_product)
//        val url = if (item?.image != "no_image") {
//            item?.image
//        } else "https://www.akeneo.com/wp-content/uploads/2018/06/Akeneo_Illustration_Website_Header_Banner_Multichannel@2x.png"
        helper.addOnClickListener(R.id.img_product)
        Picasso
            .get()
            .load("https://www.akeneo.com/wp-content/uploads/2018/06/Akeneo_Illustration_Website_Header_Banner_Multichannel@2x.png")
            .into(img)
    }
}