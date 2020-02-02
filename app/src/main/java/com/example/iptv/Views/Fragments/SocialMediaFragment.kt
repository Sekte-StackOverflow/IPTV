package com.example.iptv.Views.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.iptv.Models.SocialMedia

import com.example.iptv.R
import com.example.iptv.Views.Adapters.SosMedAdapter
import kotlinx.android.synthetic.main.fragment_social_media.*

/**
 * A simple [Fragment] subclass.
 */
class SocialMediaFragment : Fragment() {
    private lateinit var sosMedAdapter: SosMedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_social_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sosMedAdapter = SosMedAdapter(getDataDummy())
        rv_sosmed.adapter = sosMedAdapter
        rv_sosmed.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_sosmed.Recycler()
    }

    fun getDataDummy(): MutableList<SocialMedia> {
        val data: MutableList<SocialMedia> = mutableListOf()

        data.add(
            SocialMedia(
                "Youtube",
                "https://www.egedeniztextile.com/wp-content/uploads/2017/09/Youtube-logo-2017-480x480.png",
                ""
            )
        )

        data.add(
            SocialMedia(
                "Instagram",
                "https://img.freepik.com/free-vector/instagram-logo_1199-122.jpg?size=338&ext=jpg&ve=1",
                ""
            )
        )

        data.add(
            SocialMedia(
                "Facebook",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fmarketexclusive.com%2Fwp-content%2Fuploads%2F2016%2F11%2FFacebook-Inc-1.jpg&f=1&nofb=1",
                ""
            )
        )

        data.add(
            SocialMedia(
                "Twitter",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fseeklogo.net%2Fwp-content%2Fuploads%2F2016%2F11%2Ftwitter-icon-circle-blue-logo-preview-400x400.png&f=1&nofb=1",
                ""
            )
        )

        data.add(
            SocialMedia(
                "Pinterest",
                "https://www.mpa-pro.fr/resize/360x360/zc/3/f/0/src/sites/mpapro/files/products/d11458.png",
                ""
            )
        )

        data.add(
            SocialMedia(
                "LinkedIn",
                "https://www.shareicon.net/data/2015/08/29/92755_linkedin_606x606.png",
                ""
            )
        )
        return data
    }


}
