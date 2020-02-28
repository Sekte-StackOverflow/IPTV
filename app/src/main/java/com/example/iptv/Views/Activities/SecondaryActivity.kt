package com.example.iptv.Views.Activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.GradientDrawable
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.iptv.Models.Product
import com.example.iptv.R
import com.example.iptv.ViewModels.ProductsViewModel
import com.example.iptv.Views.Fragments.AboutFragment
import com.example.iptv.Views.Fragments.ProductDetailFragment
import com.example.iptv.api.service.AppKey
import kotlinx.android.synthetic.main.activity_secondary.*

class SecondaryActivity : AppCompatActivity(),
    ProductDetailFragment.OnFragmentInteractionListener
{
    private lateinit var productsViewModel: ProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)

        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel::class.java)
        productsViewModel.init()
        actionBar?.show()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        secondToolbar.setNavigationOnClickListener{
            val intent2 = Intent(this, MainActivity::class.java)
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (intent.getParcelableExtra<Product>("data_product") != null) {
                intent2.putExtra("PHASE", 1)
            }
            intent.removeExtra("data_product")
            startActivity(intent2)
            finish()
        }

        when(intent.getStringExtra(AppKey.ACTIVITY_KEY().SEC_ACT)) {
            AppKey.FRAGMENT_KEY().ABOUT_F -> {
                loadFragment(AboutFragment())
            }
            AppKey.FRAGMENT_KEY().PRODUCT_DETAIL_F -> {
                if (intent.getParcelableExtra<Product>("data_product") != null) {
                    val mProduct = intent.getParcelableExtra<Product>("data_product")
                    val fragment = ProductDetailFragment()
                    productsViewModel.getDetailProduct(mProduct.id.toString()).observe(this, Observer {
                        data ->
                        fragment.dataProduct(data)
                        loadFragment(fragment)
                    })

                }
            }
        }
    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.second_fragment_container, fragment)
            .commit()
    }

    override fun fullscreen() {
        appBarLayout2.visibility = View.GONE
        second_fragment_container.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT

    }

    override fun normalScreen() {
        appBarLayout2.visibility = View.VISIBLE
        second_fragment_container.layoutParams.height = 0

    }
}
