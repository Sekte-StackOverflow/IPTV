package com.example.iptv.api.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.iptv.Models.SocialMedia
import com.example.iptv.Models.SosMed
import com.example.iptv.api.APIClient
import com.example.iptv.api.service.SocialMediaService
import com.mikepenz.materialdrawer.util.otherwise
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SosmedRepo {
    private var liveData: MutableLiveData<MutableList<SosMed>> = MutableLiveData()
    private var liveSosmed: MutableLiveData<MutableList<SocialMedia>> = MutableLiveData()
    private var api: SocialMediaService = APIClient().getAPI().create(SocialMediaService::class.java)

    companion object {
        fun getInstance() = SosmedRepo()
    }

    fun getSocialMedia(): MutableLiveData<MutableList<SocialMedia>> {
        val call = api.getSocialMediaUrl()
        call.enqueue(object : Callback<MutableList<SosMed>> {
            override fun onFailure(call: Call<MutableList<SosMed>>, t: Throwable) {
                Log.d("Sosmed Repo", t.message.toString())
            }
            override fun onResponse(
                call: Call<MutableList<SosMed>>,
                response: Response<MutableList<SosMed>>
            ) {
                if (response.body() != null) {
                    val tmp = response.body() as MutableList<SosMed>
                    liveSosmed.value = conversionData(tmp)
                } else {
                    Log.d("Sosmed Repo", response.message())
                }
            }
        })
        return liveSosmed
    }

    private fun conversionData(sosmed: MutableList<SosMed>): MutableList<SocialMedia> {
        val res: MutableList<SocialMedia> = mutableListOf()
        res.add(SocialMedia(sosmed[2].youtube, sosmed[1].youtube, sosmed[0].youtube))
        res.add(SocialMedia(sosmed[2].instagram, sosmed[1].instagram, sosmed[0].instagram))
        res.add(SocialMedia(sosmed[2].facebook, sosmed[1].facebook, sosmed[0].facebook))
        res.add(SocialMedia(sosmed[2].twitter, sosmed[1].twitter, sosmed[0].twitter))
        res.add(SocialMedia(sosmed[2].linkedin, sosmed[1].linkedin, sosmed[0].linkedin))
        res.add(SocialMedia(sosmed[2].pinterest, sosmed[1].pinterest, sosmed[0].pinterest))
        return res
    }

//    fun getSocialMedia(): MutableLiveData<MutableList<SocialMedia>> {
//        val call = api.getAllSocial()
//        call.enqueue(object : Callback<MutableList<SocialMedia>> {
//            override fun onFailure(call: Call<MutableList<SocialMedia>>, t: Throwable) {
//                Log.d("Sosmed Repo", t.message.toString())
//            }
//
//            override fun onResponse(
//                call: Call<MutableList<SocialMedia>>,
//                response: Response<MutableList<SocialMedia>>
//            ) {
//                if (response.body() != null) {
//                    val tmp = response.body() as MutableList<SocialMedia>
//                    liveSosmed.value = tmp
//                } else {
//                    Log.d("Sosmed Repo", response.message())
//                }
//            }
//        })
//        return liveSosmed
//    }
}