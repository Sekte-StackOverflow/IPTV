package com.example.iptv.Views.Fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.iptv.Models.SocialMedia
import com.example.iptv.Models.SosMed

import com.example.iptv.R
import com.example.iptv.ViewModels.SosMedViewModel
import com.example.iptv.Views.Adapters.SosMedAdapter
import com.example.iptv.api.APIClient
import com.example.iptv.api.service.SocialMediaService
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sosMedViewModel = ViewModelProviders.of(this).get(SosMedViewModel::class.java)
        return inflater.inflate(R.layout.fragment_social_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sosMedViewModel.init()
        sosMedViewModel.getSocialMedia().observe(this, Observer {
            t -> showSocial(getDataDummy(t[0]))
        })
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

    fun getDataDummy(sosmedLink: SosMed): MutableList<SocialMedia> {
        val data: MutableList<SocialMedia> = mutableListOf()
        data.add(
            SocialMedia(
                "Youtube",
                "https://www.egedeniztextile.com/wp-content/uploads/2017/09/Youtube-logo-2017-480x480.png",
                sosmedLink.youtube
            )
        )

        data.add(
            SocialMedia(
                "Instagram",
                "https://img.freepik.com/free-vector/instagram-logo_1199-122.jpg?size=338&ext=jpg&ve=1",
                sosmedLink.instagram
            )
        )

        data.add(
            SocialMedia(
                "Facebook",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fmarketexclusive.com%2Fwp-content%2Fuploads%2F2016%2F11%2FFacebook-Inc-1.jpg&f=1&nofb=1",
                sosmedLink.facebook
            )
        )

        data.add(
            SocialMedia(
                "Twitter",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fseeklogo.net%2Fwp-content%2Fuploads%2F2016%2F11%2Ftwitter-icon-circle-blue-logo-preview-400x400.png&f=1&nofb=1",
                sosmedLink.twitter
            )
        )

        data.add(
            SocialMedia(
                "Pinterest",
                "https://www.mpa-pro.fr/resize/360x360/zc/3/f/0/src/sites/mpapro/files/products/d11458.png",
                sosmedLink.pinterest
            )
        )

        data.add(
            SocialMedia(
                "LinkedIn",
                "https://www.shareicon.net/data/2015/08/29/92755_linkedin_606x606.png",
                sosmedLink.linkedin
            )
        )
        return data
    }


}
