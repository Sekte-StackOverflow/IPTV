package com.example.iptv.api.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.iptv.Models.PublicIPAddress
import com.example.iptv.api.APIClient
import com.example.iptv.api.service.PublicIpAddressAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PublicIPRepo {
    private val api = APIClient().getIPPublicInstance().create(PublicIpAddressAPIService::class.java)
    private val liveData: MutableLiveData<PublicIPAddress> = MutableLiveData()
    
    companion object {
        fun newInstance() = PublicIPRepo()
    }
    
    fun getPubllicIP(): MutableLiveData<PublicIPAddress> {
        val call = api.getPublicIp()
        call.enqueue(object : Callback<PublicIPAddress> {
            override fun onFailure(call: Call<PublicIPAddress>, t: Throwable) {
                Log.d("Repo IP_PUBLIC", t.message.toString())
            }

            override fun onResponse(
                call: Call<PublicIPAddress>,
                response: Response<PublicIPAddress>
            ) {
                val tmp = response.body()
                liveData.value = tmp
            }
        })
        return liveData
    }
}