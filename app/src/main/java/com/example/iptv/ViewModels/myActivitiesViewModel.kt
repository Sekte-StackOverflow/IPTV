package com.example.iptv.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iptv.Models.ApiResponse
import com.example.iptv.Models.AppDataSet
import com.example.iptv.Models.UserActivities
import com.example.iptv.api.Repository.ActivitiesRepo

class myActivitiesViewModel: ViewModel() {
    private lateinit var repo: ActivitiesRepo
    private lateinit var data: MutableLiveData<ApiResponse>
    private lateinit var data2: MutableLiveData<ApiResponse>
    private lateinit var dataUtil: MutableLiveData<MutableList<AppDataSet>>

    fun init() {
        repo = ActivitiesRepo()
    }

    fun postMyActivity(userActivities: UserActivities): LiveData<ApiResponse> {
        data = repo.postActivityView(userActivities)
        return data
    }

    fun postShopping(userActivities: UserActivities): LiveData<ApiResponse> {
        data2 = repo.postShopping(userActivities)
        return data2
    }

    fun getAppUtil(): LiveData<MutableList<AppDataSet>> {
        dataUtil = repo.getDataUtil()
        return dataUtil
    }


}