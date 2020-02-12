package com.example.iptv.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.iptv.Models.SocialMedia
import com.example.iptv.Models.SosMed
import com.example.iptv.api.APIClient
import com.example.iptv.api.Repository.SosmedRepo
import com.example.iptv.api.service.SocialMediaService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SosMedViewModel: ViewModel() {
    private lateinit var socialMedia: MutableLiveData<MutableList<SocialMedia>>
    private lateinit var link: MutableLiveData<MutableList<SosMed>>
    private lateinit var repo: SosmedRepo

    fun init() {
        repo = SosmedRepo.getInstance()
        link = repo.getSocial()
    }

    fun getSocialMedia(): LiveData<MutableList<SosMed>> {
        return link
    }
}