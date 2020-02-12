package com.example.iptv.api.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.iptv.Models.Product
import com.example.iptv.api.APIClient
import com.example.iptv.api.service.ProductApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepo{
    private var liveData: MutableLiveData<MutableList<Product>> = MutableLiveData()
    private var api: ProductApiService = APIClient().getAPI().create(ProductApiService::class.java)

    companion object {
        fun getInstance() = ProductRepo()
    }

    fun getAllProduct(): MutableLiveData<MutableList<Product>> {
        val call = api.getProducts()
        call.enqueue(object : Callback<MutableList<Product>> {
            override fun onFailure(call: Call<MutableList<Product>>, t: Throwable) {
                Log.d("Product Repo", t.message.toString())
            }

            override fun onResponse(
                call: Call<MutableList<Product>>,
                response: Response<MutableList<Product>>
            ) {
                if (response.body() != null) {
                    val tmp = response.body() as MutableList<Product>
                    liveData.value = tmp
                } else {
                    Log.d("Product Repo", response.message())
                }
            }
        })
        return liveData
    }
}