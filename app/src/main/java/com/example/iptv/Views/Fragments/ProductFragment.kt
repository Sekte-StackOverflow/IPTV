package com.example.iptv.Views.Fragments


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.iptv.Models.Banner
import com.example.iptv.Models.Product

import com.example.iptv.R
import com.example.iptv.ViewModels.BannerViewModel
import com.example.iptv.ViewModels.ProductsViewModel
import com.example.iptv.Views.Activities.SecondaryActivity
import com.example.iptv.Views.Adapters.ProductsAdapter
import com.example.iptv.api.APIClient
import com.example.iptv.api.service.AppKey
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.fragment_product.*

/**
 * A simple [Fragment] subclass.
 */
class ProductFragment : Fragment() {

    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var tel: String
    private lateinit var list: MutableList<Product>
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var bannerViewModel: BannerViewModel

    private lateinit var banner: MutableList<Banner>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel::class.java)

        bannerViewModel = ViewModelProviders.of(this).get(BannerViewModel::class.java)
        bannerViewModel.init()
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tel = "02129926226"

        productsViewModel.init()
        productsViewModel.getAllProduct().observe(this, Observer {
            t ->
            showList(t)
            list = t
        })

        bannerViewModel.getBanner("produk").observe(this, Observer {
            img -> bannerConfig(img)
        })

        btn_buy_now.setOnClickListener{
            openDialog()
        }
    }

    private fun bannerConfig(list: MutableList<Banner>) {
        banner = mutableListOf()
        banner = list
        product_carousel.setImageListener { position, imageView ->
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            val url = APIClient.IMAGE_PATH + banner[position].img
            Picasso.get().load(url).into(imageView)
        }
        product_carousel.pageCount = banner.size
    }

    private fun openDialog() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog)
        val whatsApp = dialog.findViewById<LinearLayout>(R.id.dialog_whatsapp)
        val phone = dialog.findViewById<LinearLayout>(R.id.dialog_phone)
        whatsApp!!.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=$tel"))
            startActivity(
                intent
            )
            dialog.dismiss()
        }
        phone!!.setOnClickListener{
            startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:$tel")))
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showList(mutableList: MutableList<Product>) {
        productsAdapter = ProductsAdapter(mutableList)
        productsAdapter.openLoadAnimation()
        productsAdapter.setOnItemChildClickListener{
                adapter, view, position ->
            detailActivity(position)
        }
        productsAdapter.notifyDataSetChanged()
        rv_products.adapter = productsAdapter
        rv_products.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_products.Recycler()
    }

    private fun detailActivity(position: Int) {
        val product: Product = list[position]
        val intent = Intent(this.activity, SecondaryActivity::class.java)
        intent.putExtra(AppKey.ACTIVITY_KEY().SEC_ACT, AppKey.FRAGMENT_KEY().PRODUCT_DETAIL_F)
        intent.putExtra("data_product", product)
        intent.putExtra("videoId", product.videoId)
        startActivity(intent)
    }

}
