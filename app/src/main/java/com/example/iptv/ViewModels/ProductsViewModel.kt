package com.example.iptv.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iptv.Models.DetailItem
import com.example.iptv.Models.Product
import com.example.iptv.api.Repository.ProductRepo


class ProductsViewModel: ViewModel() {
    private lateinit var listProduct: MutableLiveData<MutableList<Product>>
    private lateinit var listDetail: MutableLiveData<MutableList<DetailItem>>
    private lateinit var repo : ProductRepo

    fun init() {
        repo = ProductRepo.getInstance()
    }

    fun getAllProduct(): LiveData<MutableList<Product>> {
        listProduct = repo.getAllProduct()
        return listProduct
    }

    fun getDetailProduct(id: String): LiveData<MutableList<DetailItem>> {
        listDetail = repo.productDetail(id)
        return listDetail
    }
}