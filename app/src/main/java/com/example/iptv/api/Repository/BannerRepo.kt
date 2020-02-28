package com.example.iptv.api.Repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.iptv.Models.Banner
import com.example.iptv.api.APIClient
import com.example.iptv.api.service.BannerAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BannerRepo {
    private val api = APIClient().getAPI().create(BannerAPIService::class.java)
    private var liveData: MutableLiveData<MutableList<Banner>> = MutableLiveData()
    private var liveData2: MutableLiveData<MutableList<Banner>> = MutableLiveData()

    companion object {
        fun newInstance() = BannerRepo()
    }

    @SuppressLint("DefaultLocale")
    fun getBanner(type: String): MutableLiveData<MutableList<Banner>> {
        val data = when(type.toLowerCase()) {
            "channel" -> api.getBannerChannel()
            "sosmed" -> api.getBannerSosmed()
            "product" -> api.getBannerProduct()
            else -> api.getBannerProduct()
        }
        data.enqueue(object : Callback<MutableList<Banner>> {
            override fun onFailure(call: Call<MutableList<Banner>>, t: Throwable) {
                Log.d("BannerRepo", t.message.toString())
            }
            override fun onResponse(
                call: Call<MutableList<Banner>>,
                response: Response<MutableList<Banner>>
            ) {
                if (response.body() != null) {
                    val tmp = response.body() as MutableList<Banner>
                    liveData.value = tmp
                }
            }
        })
        return liveData
    }

    fun getImagePopUp(): MutableLiveData<MutableList<Banner>> {
        val data = api.getImageDialog()
        data.enqueue(object : Callback<MutableList<Banner>> {
            override fun onFailure(call: Call<MutableList<Banner>>, t: Throwable) {
                Log.d("BannerRepo", t.message.toString())
            }
            override fun onResponse(
                call: Call<MutableList<Banner>>,
                response: Response<MutableList<Banner>>
            ) {
                if (response.body() != null) {
                    val tmp = response.body() as MutableList<Banner>
                    liveData2.value = tmp
                }
            }
        })
        return liveData2
    }
}