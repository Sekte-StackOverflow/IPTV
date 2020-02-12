package com.example.iptv.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iptv.Models.Product
import com.example.iptv.api.Repository.ProductRepo


class ProductsViewModel: ViewModel() {
    private lateinit var listProduct: MutableLiveData<MutableList<Product>>
    private lateinit var repo : ProductRepo

    fun init() {
        repo = ProductRepo.getInstance()
        listProduct = repo.getAllProduct()
    }

    fun getAllProduct(): LiveData<MutableList<Product>> {
        return listProduct
    }
}