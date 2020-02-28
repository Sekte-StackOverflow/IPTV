package com.example.iptv.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iptv.Models.Banner
import com.example.iptv.api.Repository.BannerRepo

class BannerViewModel: ViewModel() {
    private lateinit var bannerList: MutableLiveData<MutableList<Banner>>
    private lateinit var dialogImage: MutableLiveData<MutableList<Banner>>
    private lateinit var repo: BannerRepo

    fun init() {
        repo = BannerRepo.newInstance()
    }

    fun getBanner(type: String): MutableLiveData<MutableList<Banner>> {
        bannerList = repo.getBanner(type)
        return bannerList
    }

    fun getImageDialog(): LiveData<MutableList<Banner>> {
        dialogImage = repo.getImagePopUp()
        return dialogImage
    }


}