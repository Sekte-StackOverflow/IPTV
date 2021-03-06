package com.example.iptv.Views.Fragments


import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.JsonWriter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.ActionBarOverlayLayout
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iptv.Models.DetailItem
import com.example.iptv.Models.Product

import com.example.iptv.R
import com.example.iptv.ViewModels.ProductsViewModel
import com.example.iptv.Views.Adapters.DetailAdapter
import com.example.iptv.Views.Adapters.DetailImageAdapter
import com.example.iptv.api.APIClient
import com.example.iptv.api.Repository.ProductRepo
import com.google.android.material.card.MaterialCardView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_product_detail.*
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ProductDetailFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var iklanDialog: Dialog
    private lateinit var currentProduct: Product
    private lateinit var listDetail: MutableList<DetailItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity!!.intent.getParcelableExtra<Product>("data_product") != null) {
            val product = activity!!.intent.getParcelableExtra("data_product") as Product
            currentProduct = product
            detail_product_name.text = currentProduct.name
            showList(listDetail)
        }

        youtube_player.getPlayerUiController().showFullscreenButton(false)
        youtube_player.getPlayerUiController().showYouTubeButton(false)
        youtube_player.addYouTubePlayerListener(object :AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                var id = ""
                if (activity?.intent?.getStringExtra("videoId") != null) {
                    id = activity!!.intent.getStringExtra("videoId")
                } else {
                    id = currentProduct.videoId!!
                }
                youTubePlayer.cueVideo(id!!, 0f)
            }
        })
        showDiaolog()
    }

    private fun showDiaolog() {
        iklanDialog = Dialog(requireContext())
        iklanDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        iklanDialog.setContentView(R.layout.image_dialog)
        val img = iklanDialog.window!!.decorView.findViewById<ImageView>(R.id.dialog_img)
        Picasso.get().load("https://eyeplus.co.id/admin-eyeplus/media/img/Banner-Popup-Web.png").into(img)
        iklanDialog.show()
    }

    fun dataProduct(product: MutableList<DetailItem>) {
        listDetail = product
    }

    private fun showList(details: MutableList<DetailItem>) {
        img_detail.visibility = View.VISIBLE
        if (currentProduct.diskon != 0 && currentProduct.diskon > 0) {
            pd_diskon_frame.visibility = View.VISIBLE
            pd_diskon.text = "${currentProduct.diskon} % OFF"
            pd_price_before.text = idrCurrency(currentProduct.price!!.toDouble())
            pd_price_after.text = idrCurrency(
                totalPrice(
                    currentProduct.price!!.toDouble(),
                    currentProduct.diskon.toDouble()
                )
            )
        } else {
            pd_diskon_frame.visibility = View.INVISIBLE
            pd_price_after.text = idrCurrency(currentProduct.price!!.toDouble())
        }
        Picasso.get().load(APIClient.IMAGE_PATH + currentProduct.image).into(img_detail)
        val adapterDetail = DetailAdapter(details)
        adapterDetail.openLoadAnimation()
        // List Detail
        rv_detail_descr.adapter = adapterDetail
        rv_detail_descr.layoutManager = LinearLayoutManager(requireContext())
        rv_detail_descr.Recycler()
    }

    private fun totalPrice(oriPrice: Double, diskon: Double): Double {
        return (oriPrice - (oriPrice * (diskon / 100)))
    }

    private fun idrCurrency(number: Double): String {
        val locale = Locale("in", "ID")
        val numFor = NumberFormat.getCurrencyInstance(locale)
        return numFor.format(number)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youtube_player.enterFullScreen()
            youtube_player.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            pd_layout_title.visibility = View.GONE
            detail_data.visibility = View.GONE
            yt_layout.layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
            card_youtube.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
            listener!!.fullscreen()
        } else {
            youtube_player.exitFullScreen()
            youtube_player.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            yt_layout.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            card_youtube.layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
            pd_layout_title.visibility = View.VISIBLE
            detail_data.visibility = View.VISIBLE
            listener!!.normalScreen()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun fullscreen()
        fun normalScreen()
    }
}
