package com.example.iptv.api.Repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.iptv.Models.Channel
import com.example.iptv.api.APIClient
import com.example.iptv.api.service.ChannelApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.timer

class ChannelRepo() {
    private var list: MutableLiveData<MutableList<Channel>> = MutableLiveData()
    private var api = APIClient().getAPI().create(ChannelApiService::class.java)

    companion object {
        fun getInstance()= ChannelRepo()
    }

    fun getChannels(): MutableLiveData<MutableList<Channel>> {
        val call = api.getChannels()
        call.enqueue(object : Callback<MutableList<Channel>> {
            override fun onFailure(call: Call<MutableList<Channel>>, t: Throwable) {
                Log.d("Channel Repo", t.message.toString())
            }

            override fun onResponse(
                call: Call<MutableList<Channel>>,
                response: Response<MutableList<Channel>>
            ) {
                if (response.body() != null) {
                    val tmp = response.body() as MutableList<Channel>
                    list.value = tmp
                } else {
                    Log.d("Channel Repo", response.message())
                }
            }
        })
        return list
    }


}