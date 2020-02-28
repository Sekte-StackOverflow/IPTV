package com.example.iptv.Views.Fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.iptv.Models.Banner
import com.example.iptv.Models.SocialMedia
import com.example.iptv.Models.SosMed

import com.example.iptv.R
import com.example.iptv.ViewModels.BannerViewModel
import com.example.iptv.ViewModels.SosMedViewModel
import com.example.iptv.Views.Adapters.SosMedAdapter
import com.example.iptv.api.APIClient
import com.example.iptv.api.service.SocialMediaService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_social_media.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class SocialMediaFragment : Fragment() {
    private lateinit var sosMedAdapter: SosMedAdapter
    private lateinit var listSosmed: MutableList<SocialMedia>
    private lateinit var sosMedViewModel: SosMedViewModel
    private lateinit var bannerViewModel: BannerViewModel

    private lateinit var listBanner: MutableList<Banner>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sosMedViewModel = ViewModelProviders.of(this).get(SosMedViewModel::class.java)
        bannerViewModel = ViewModelProviders.of(this).get(BannerViewModel::class.java)

        return inflater.inflate(R.layout.fragment_social_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sosMedViewModel.init()
        bannerViewModel.init()

        bannerViewModel.getBanner("sosmed").observe(this, Observer {
            showBanner(it)
        })

        sosMedViewModel.getSocialMedia().observe(this, Observer {
            socialMedia -> showSocial(socialMedia)
        })
    }
    private fun showBanner(list: MutableList<Banner>) {
        listBanner = mutableListOf()
        listBanner = list
        poster_sosmed.setImageListener { position, imgV ->
            imgV.scaleType = ImageView.ScaleType.FIT_XY
            val imgUri = APIClient.IMAGE_PATH + listBanner[position].img
            Picasso.get().load(imgUri).into(imgV)
        }
        poster_sosmed.pageCount = listBanner.size
    }
    private fun showSocial(mutableList: MutableList<SocialMedia>) {
        listSosmed = mutableListOf()
        listSosmed = mutableList
        sosMedAdapter = SosMedAdapter(mutableList)
        sosMedAdapter.openLoadAnimation()
        sosMedAdapter.setOnItemClickListener{
            adapter, view, position ->
//            Toast.makeText(context, listSosmed[position].url, Toast.LENGTH_SHORT).show()
            val socialMedia = adapter.data[position] as SocialMedia
            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(socialMedia.url)))
        }
        sosMedAdapter.notifyDataSetChanged()
        rv_sosmed.adapter = sosMedAdapter
        rv_sosmed.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_sosmed.Recycler()
    }
}
