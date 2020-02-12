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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.iptv.Models.Product

import com.example.iptv.R
import com.example.iptv.ViewModels.ProductsViewModel
import com.example.iptv.Views.Activities.SecondaryActivity
import com.example.iptv.Views.Adapters.ProductsAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel::class.java)
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

        product_carousel.setImageListener { position, imageView -> Picasso.get().load(bannerDummy()[position]).into(imageView) }
        product_carousel.pageCount = bannerDummy().size

        btn_buy_now.setOnClickListener{
//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.setData(Uri.parse("tel:$tel"))
//            startActivity(intent)
            openDialog()
        }
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

    private fun bannerDummy(): MutableList<String> {
        return mutableListOf(
            "https://dxclnrbvyw82b.cloudfront.net/images/di/upload/20191016/1f1822eb-1ca4-4502-a57b-333683d38961/88/billbord-page-ec-tv-home-shopping-best-seller.jpg",
            "https://dxclnrbvyw82b.cloudfront.net/images/di/upload/20191016/1f1822eb-1ca4-4502-a57b-333683d38961/88/billbord-page-ec-tv-home-shopping-best-seller.jpg"
        )
    }

    private fun detailActivity(position: Int) {
        val product: Product = list[position]
        val intent = Intent(this.activity, SecondaryActivity::class.java)
        intent.putExtra(AppKey.ACTIVITY_KEY().SEC_ACT, AppKey.FRAGMENT_KEY().PRODUCT_DETAIL_F)
        intent.putExtra("data_product", product)
        startActivity(intent)
    }

    private fun dummyData(): MutableList<Product> {
        val pdc: MutableList<Product> = mutableListOf()
        pdc.add(
            Product(
                "1",
                "https://alvaindopratama.com/eyeplus/image/Product/iwater.png",
                "",
                "eUZe0VNYSVM",
                1000000,
                40
            )
        )
        pdc.add(
            Product(
                "2",
                "https://alvaindopratama.com/eyeplus/image/Product/Kasur-web.png",
                "",
                "kB4UvComcEE",
                1200000,
                35
            )
        )
        pdc.add(
            Product(
                "2",
                "https://alvaindopratama.com/eyeplus/image/Product/thumbnail.jpg",
                "",
                "4VmFR3v64K0",
                1400000,
                20
            )
        )
        pdc.add(
            Product(
                "Smartphone Sale",
                "https://alvaindopratama.com/eyeplus/image/Product/thumbnail1.jpg",
                "",
                "0nuvRZ5rU40",
                1150000,
                25
            )
        )
        pdc.add(
            Product(
                "",
                "https://alvaindopratama.com/eyeplus/image/Product/colocasia.png",
                "",
                "0QwMbdhzQzw",
                1000000,
                30
            )
        )
        return pdc
    }

}
