package com.example.iptv.Views.Fragments


import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.iptv.Models.Product

import com.example.iptv.R
import com.example.iptv.Views.Adapters.ProductsAdapter
import com.google.android.exoplayer2.Timeline
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.fragment_product.*

/**
 * A simple [Fragment] subclass.
 */
class ProductFragment : Fragment() {
    private lateinit var productsAdapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsAdapter = ProductsAdapter(dummyData())
        productsAdapter.setOnItemChildClickListener{
            adapter, view, position ->
            val product: Product = adapter.data[position] as Product
            Toast.makeText(requireContext(), product.name, Toast.LENGTH_SHORT).show()
        }
        rv_products.adapter = productsAdapter
        rv_products.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_products.Recycler()

        val url: Uri = Uri.parse("https://www.youtube.com/embed/ljbcEDDUpz4")
        youtube_view_player.getPlayerUiController().showFullscreenButton(true)
        youtube_view_player.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = "ljbcEDDUpz4"
                youTubePlayer.cueVideo(videoId, 0f)
            }
        })
        youtube_view_player.getPlayerUiController().setFullScreenButtonClickListener(View.OnClickListener {
            if (youtube_view_player.isFullScreen()) {
                youtube_view_player.exitFullScreen()
                rv_products.visibility = View.VISIBLE
                activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            } else {
                youtube_view_player.enterFullScreen()
                rv_products.visibility = View.GONE
//                youtube_view_player.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            }
        })

        btn_buy_now.setOnClickListener{
            Toast.makeText(requireContext(), "OK GAN", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dummyData(): MutableList<Product> {
        val pdc: MutableList<Product> = mutableListOf()
        pdc.add(
            Product(
                "Sale 70%",
                "https://static.vecteezy.com/system/resources/previews/000/279/608/original/sale-poster-for-shopping-discount-retail-product-promotion-vector-illustration-sale-banner-template.jpg",
                "https://static.vecteezy.com/system/resources/previews/000/279/608/original/sale-poster-for-shopping-discount-retail-product-promotion-vector-illustration-sale-banner-template.jpg"
            )
        )
        pdc.add(
            Product(
                "Shoes Sale",
                link = "https://cmkt-image-prd.global.ssl.fastly.net/0.1.0/ps/522794/3000/1997/m1/fpnw/wm0/flat-product-sale-shoe-banner-p1-.jpg?1434193319&s=5e32357a0be6ce6fded220d5ad54624f"
            )
        )
        pdc.add(
            Product(
                "WhatEver",
                link = "http://www.dienmaythienhoa.vn/uploads/products/giadung/MAY-XAY-DAU-NANH-KANGAROO-KG609-banner.jpg"
            )
        )
        pdc.add(
            Product(
                "Smartphone Sale",
                "https://cmkt-image-prd.global.ssl.fastly.net/0.1.0/ps/522786/3000/1997/m1/fpnw/wm0/flat-product-sale-mobile-banner-p1-.jpg?1434193356&s=5c40522be1701b2ce7b3c617b12fa70b",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcmkt-image-prd.global.ssl.fastly.net%2F0.1.0%2Fps%2F522786%2F3000%2F1997%2Fm1%2Ffpnw%2Fwm0%2Fflat-product-sale-mobile-banner-p1-.jpg%3F1434193356%26s%3D5c40522be1701b2ce7b3c617b12fa70b&f=1&nofb=1"
            )
        )
        return pdc
    }
}
