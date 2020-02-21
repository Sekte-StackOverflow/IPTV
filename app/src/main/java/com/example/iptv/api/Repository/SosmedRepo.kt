package com.example.iptv.api.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.iptv.Models.SocialMedia
import com.example.iptv.Models.SosMed
import com.example.iptv.api.APIClient
import com.example.iptv.api.service.SocialMediaService
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

//    fun getSocial(): MutableLiveData<MutableList<SosMed>> {
////        val api = APIClient().getAPI().create(SocialMediaService::class.java)
//        val call = api.getSocialMediaUrl()
//        call.enqueue(object : Callback<MutableList<SosMed>> {
//            override fun onFailure(call: Call<MutableList<SosMed>>, t: Throwable) {
//                Log.d("Sosmed Repo", t.message.toString())
//            }
//            override fun onResponse(
//                call: Call<MutableList<SosMed>>,
//                response: Response<MutableList<SosMed>>
//            ) {
//                if (response.body() != null) {
//                    val tmp = response.body() as MutableList<SosMed>
//                    liveData.value = tmp
//                } else {
//                    Log.d("Sosmed Repo", response.message())
//                }
//            }
//        })
//        return liveData
//    }

    fun getSocialMedia(): MutableLiveData<MutableList<SocialMedia>> {
        val call = api.getAllSocial()
        call.enqueue(object : Callback<MutableList<SocialMedia>> {
            override fun onFailure(call: Call<MutableList<SocialMedia>>, t: Throwable) {
                Log.d("Sosmed Repo", t.message.toString())
            }

            override fun onResponse(
                call: Call<MutableList<SocialMedia>>,
                response: Response<MutableList<SocialMedia>>
            ) {
                if (response.body() != null) {
                    val tmp = response.body() as MutableList<SocialMedia>
                    liveSosmed.value = tmp
                } else {
                    Log.d("Sosmed Repo", response.message())
                }
            }
        })
        return liveSosmed
    }
}