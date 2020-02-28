package com.example.iptv.api.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.iptv.Models.ApiResponse
import com.example.iptv.Models.AppDataSet
import com.example.iptv.Models.UserActivities
import com.example.iptv.api.APIClient
import com.example.iptv.api.service.UserActivityService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivitiesRepo {
    private val TAG = "ACTIVITY_REPO"

    private val api = APIClient().getAPI().create(UserActivityService::class.java)
    private val apiClient = APIClient().getAPIfromClient().create(UserActivityService::class.java)

    private val dataActivity: MutableLiveData<ApiResponse> = MutableLiveData()
    private val dataActivity2: MutableLiveData<ApiResponse> = MutableLiveData()

    private val dataSet: MutableLiveData<MutableList<AppDataSet>> = MutableLiveData()


    fun postShopping(userActivity: UserActivities): MutableLiveData<ApiResponse> {
        val call = apiClient.postShopping(userActivity)
        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    Log.d(TAG , "code: ${response.code()}, msg: ${response.message()}")
                    dataActivity2.value = response.body()
                }
            }
        })
        return dataActivity2
    }

    fun postActivityView(userActivity: UserActivities): MutableLiveData<ApiResponse> {
        val call = apiClient.postActivity(userActivity)
            call.enqueue(object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
                }

                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.body() != null) {
                        dataActivity.value = response.body()
                    }
                }
            })
        return dataActivity
    }

    fun getDataUtil(): MutableLiveData<MutableList<AppDataSet>> {
        val call = api.getDataUtil()
        call.enqueue(object : Callback<MutableList<AppDataSet>> {
            override fun onFailure(call: Call<MutableList<AppDataSet>>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }

            override fun onResponse(
                call: Call<MutableList<AppDataSet>>,
                response: Response<MutableList<AppDataSet>>
            ) {
                if (response.body() != null) {
                    dataSet.value = response.body()
                }
            }

        })
        return dataSet
    }
}