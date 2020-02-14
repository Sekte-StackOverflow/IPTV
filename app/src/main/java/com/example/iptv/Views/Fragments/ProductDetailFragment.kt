package com.example.iptv.Views.Fragments


import android.app.Dialog
import android.content.Context
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iptv.Models.DetailItem
import com.example.iptv.Models.Product

import com.example.iptv.R
import com.example.iptv.Views.Adapters.DetailAdapter
import com.example.iptv.Views.Adapters.DetailImageAdapter
import com.google.android.material.card.MaterialCardView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_product_detail.*

/**
 * A simple [Fragment] subclass.
 */
class ProductDetailFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var iklanDialog: Dialog
    private lateinit var currentProduct: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        youtube_player.getPlayerUiController().showFullscreenButton(true)
        youtube_player.getPlayerUiController().showYouTubeButton(false)
        youtube_player.addYouTubePlayerListener(object :AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = currentProduct.videoId
                youTubePlayer.cueVideo(videoId!!, 0f)
            }
        })

        youtube_player.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                listener!!.fullscreen()
                yt_layout.layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
                card_youtube.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
                pd_layout_title.visibility = View.GONE
                detail_data.visibility = View.GONE
            }

            override fun onYouTubePlayerExitFullScreen() {
                listener!!.normalScreen()
                yt_layout.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                card_youtube.layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
                pd_layout_title.visibility = View.VISIBLE
                detail_data.visibility = View.GONE
            }
        })

        showList(dummyPic(), dummyList())

        iklanDialog = Dialog(requireContext())
        iklanDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val dialogView = layoutInflater.inflate(R.layout.image_dialog, null)
        val img = dialogView.findViewById<ImageView>(R.id.dialog_img)
        Picasso.get().load("https://alvaindopratama.com/eyeplus/image/banner/popup1.png").into(img)
        iklanDialog.setContentView(dialogView)
        iklanDialog.show()
    }

    fun dataProduct(product: Product) {
        currentProduct = product
    }

    private fun showList(images: MutableList<String>, details: MutableList<DetailItem>) {
        val adapterDetail = DetailAdapter(details)
        val adapterPic = DetailImageAdapter(images)
        adapterDetail.openLoadAnimation()
        adapterPic.openLoadAnimation()

        // List Detail
        rv_detail_descr.adapter = adapterDetail
        rv_detail_descr.layoutManager = LinearLayoutManager(requireContext())
        rv_detail_descr.Recycler()

        // List Picture
        rv_detail_pic.adapter = adapterPic
        rv_detail_pic.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_detail_pic.Recycler()
    }

    private fun dummyPic(): MutableList<String> {
        val dummyImg = mutableListOf<String>()
        for (i in 1..8) {
            dummyImg.add("https://rectmedia.com/wp-content/uploads/2019/07/dummy-product.jpeg")
        }
        return dummyImg
    }

    private fun dummyList(): MutableList<DetailItem> {
        val dummies = mutableListOf<DetailItem>()
        for (i in 1..10) {
            dummies.add(DetailItem("This is key", resources.getString(R.string.descipt_txt)))
        }
        return dummies
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
