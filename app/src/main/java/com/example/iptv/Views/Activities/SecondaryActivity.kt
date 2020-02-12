package com.example.iptv.Views.Activities

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.iptv.Models.Product
import com.example.iptv.R
import com.example.iptv.Views.Fragments.AboutFragment
import com.example.iptv.Views.Fragments.ProductDetailFragment
import com.example.iptv.api.service.AppKey
import kotlinx.android.synthetic.main.activity_secondary.*

class SecondaryActivity : AppCompatActivity(),
    ProductDetailFragment.OnFragmentInteractionListener
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)

        actionBar?.show()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        secondToolbar.setNavigationOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        when(intent.getStringExtra(AppKey.ACTIVITY_KEY().SEC_ACT)) {
            AppKey.FRAGMENT_KEY().ABOUT_F -> {
                fake_toolbar.visibility = View.GONE
                loadFragment(AboutFragment())
            }
            AppKey.FRAGMENT_KEY().PRODUCT_DETAIL_F -> {
                if (intent.getParcelableExtra<Product>("data_product") != null) {
                    fake_toolbar.visibility = View.VISIBLE
                    val mProduct = intent.getParcelableExtra<Product>("data_product")
                    val fragment = ProductDetailFragment()
                    fragment.dataProduct(mProduct!!)
                    loadFragment(fragment)
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
        second_fragment_container.layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
    }
}
