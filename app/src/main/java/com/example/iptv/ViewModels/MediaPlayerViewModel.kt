package com.example.iptv.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iptv.Models.Channel
import com.example.iptv.api.Repository.ChannelRepo

class MediaPlayerViewModel : ViewModel() {
    private lateinit var data: MutableLiveData<MutableList<Channel>>
    private lateinit var repo: ChannelRepo

    fun init() {
        repo = ChannelRepo.getInstance()
        data = repo.getChannels()
    }

    fun getAllChannel(): LiveData<MutableList<Channel>> {
        return data
    }
}
