package com.example.iptv.Views.Adapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.iptv.Models.Product
import com.example.iptv.R
import com.example.iptv.api.APIClient
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*

class ProductsAdapter(data: MutableList<Product>?) :
    BaseQuickAdapter<Product, BaseViewHolder>(R.layout.product_item ,data) {
    override fun convert(helper: BaseViewHolder, item: Product?) {
        val img: ImageView = helper.getView(R.id.img_product)

        if (item?.diskon != 0) {
            val total = totalPrice(item!!.price!!.toDouble(), item.diskon.toDouble())
            helper.setText(R.id.product_diskon_percen, "${item.diskon} % OFF")
                .setText(R.id.price_product_before, idrCurrency(item.price!!.toDouble()))
                .setText(R.id.price_product_after, idrCurrency(total))
            helper.getView<LinearLayout>(R.id.product_diskon).visibility = View.VISIBLE
        } else {
            helper.getView<LinearLayout>(R.id.product_diskon).visibility = View.INVISIBLE
            val mPrice = item.price?.let { idrCurrency(it.toDouble()) }
            helper.setText(R.id.price_product_after, "$mPrice")
        }

        helper.addOnClickListener(R.id.img_product)
        Picasso
            .get()
            .load(APIClient.IMAGE_PATH + item.image)
            .into(img)
    }

    private fun totalPrice(oriPrice: Double, diskon: Double): Double {
        return (oriPrice - (oriPrice * (diskon / 100)))
    }

    private fun idrCurrency(number: Double): String {
        val locale = Locale("in", "ID")
        val numFor = NumberFormat.getCurrencyInstance(locale)
        return numFor.format(number)
    }
}