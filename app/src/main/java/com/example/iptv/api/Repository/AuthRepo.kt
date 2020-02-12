package com.example.iptv.api.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.iptv.Models.User
import com.example.iptv.api.APIClient
import com.example.iptv.api.service.UsersApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepo {
    private var api = APIClient().getAPI().create(UsersApiService::class.java)
    private var userAuth: MutableLiveData<MutableList<User>> = MutableLiveData()

    companion object {
        fun newInstance() = AuthRepo()
    }

    fun userEmail(email: String): MutableLiveData<MutableList<User>> {
        val call = api.getUserViaEmail(email)
        call.enqueue(object : Callback<MutableList<User>> {
            override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                Log.d("AuthRepo", t.message.toString())
            }

            override fun onResponse(
                call: Call<MutableList<User>>,
                response: Response<MutableList<User>>
            ) {
                if (response.body() != null) {
                    val data = response.body() as MutableList<User>
                    userAuth.value = data
                }
            }
        })
        return userAuth
    }

    fun userPhone(number: String): MutableLiveData<MutableList<User>> {
        val call = api.getUserViaPhone(number)
        call.enqueue(object : Callback<MutableList<User>> {
            override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                Log.d("AuthRepo", t.message.toString())
            }

            override fun onResponse(
                call: Call<MutableList<User>>,
                response: Response<MutableList<User>>
            ) {
                if (response.body() != null) {
                    val data = response.body() as MutableList<User>
                    userAuth.value = data
                }
            }
        })
        return userAuth
    }

    fun userRegistation(user: User): MutableLiveData<MutableList<User>> {
        val call = api.newUser(user)
        call.enqueue(object : Callback<MutableList<User>> {
            override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                Log.d("AuthRepo", t.message.toString())
            }

            override fun onResponse(
                call: Call<MutableList<User>>,
                response: Response<MutableList<User>>
            ) {
                if (response.body() != null) {
                    val data = response.body() as MutableList<User>
                    userAuth.value = data
                }
            }
        })
        return userAuth
    }

}