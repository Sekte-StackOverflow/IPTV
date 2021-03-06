package com.example.iptv.Views.Activities

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.iptv.Models.AppDataSet
import com.example.iptv.Models.Product
import com.example.iptv.Models.User
import com.example.iptv.R
import com.example.iptv.ViewModels.SessionViewModel
import com.example.iptv.ViewModels.myActivitiesViewModel
import com.example.iptv.Views.Adapters.PagerAdapter
import com.example.iptv.Views.Fragments.ProductDetailFragment
import com.example.iptv.Views.Fragments.ProductFragment
import com.example.iptv.api.APIClient
import com.example.iptv.api.service.AppKey
import com.example.iptv.api.SSLconfig
import com.example.iptv.api.SessionManagement
import com.google.android.material.tabs.TabLayout
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome

import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    TabLayout.OnTabSelectedListener
{
    private lateinit var pagerAdapter: PagerAdapter
    private var isLogin: Boolean = false
    private lateinit var user: User
    private lateinit var profile: AppDataSet
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var myActivities: myActivitiesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sessionViewModel = ViewModelProviders.of(this).get(SessionViewModel::class.java)
        myActivities = ViewModelProviders.of(this).get(myActivitiesViewModel::class.java)
        myActivities.init()
        sessionViewModel.init(applicationContext)

        myActivities.getAppUtil().observe(this, Observer {
            profile = it[0]
            Picasso.get()
                .load(APIClient.IMAGE_PATH + profile.logoapp)
                .into(my_logo)
        })

        sessionViewModel.getUser().observe(this, Observer {
            t ->
            if (t.email != "" && t.phone !="") {
                user = t
            } else {
                user = t
            }
        })
        sessionViewModel.isLoginLive().observe(this, Observer {
                t -> isLogin = t
            navDrawer(user, isLogin)
        })
        sessionViewModel.getUser().observe(this, Observer {
                t ->
            if (t.email != "" && t.phone !="") {
                user = t
            } else {
                user = t
            }
        })

        pagerAdapter = PagerAdapter(supportFragmentManager, tabs.tabCount)
        view_pager.adapter = pagerAdapter
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(this)
        if (intent.getIntExtra("PHASE", 0) == 1) {
            view_pager.currentItem = 1
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        view_pager.currentItem = tab.position
    }

    private fun navDrawer(user: User, isLogin: Boolean) {
        val liveMovie = PrimaryDrawerItem()
            .withIcon(R.drawable.ic_live_tv_black_24dp)
            .withIdentifier(1)
            .withName("Live Movie")
            .withSelectable(true)
        val shopping = PrimaryDrawerItem()
            .withIcon(R.drawable.ic_local_grocery_store_black_24dp)
            .withIdentifier(2)
            .withName("Shopping")
            .withSelectable(true)
        val sosMed = PrimaryDrawerItem()
            .withIcon(R.drawable.ic_chat_bubble_outline_black_24dp)
            .withIdentifier(3)
            .withName("Social Media")
            .withSelectable(true)
        val info = PrimaryDrawerItem()
            .withIcon(R.drawable.ic_account_circle_black_24dp)
            .withIdentifier(4)
            .withName("Information")
            .withSelectable(true)


        val loginStatus =
            if (isLogin) {
                SecondaryDrawerItem()
                    .withIcon(FontAwesome.getIcon("faw_sign_out_alt"))
                    .withName("Sign out")
                    .withIdentifier(5)
                    .withSelectable(true)
            } else {
                SecondaryDrawerItem()
                    .withIcon(FontAwesome.getIcon("faw_sign_in_alt"))
                    .withName("Sign in")
                    .withIdentifier(5)
                    .withSelectable(true)
            }

        val changePass = SecondaryDrawerItem()
            .withIcon(R.drawable.ic_lock_black_24dp)
            .withName("Change Password")
            .withIdentifier(6)
            .withSelectable(true)


        val about = SecondaryDrawerItem()
            .withIcon(R.drawable.ic_info_black_24dp)
            .withIdentifier(7)
            .withName("About")

        val result = DrawerBuilder()
            .withActivity(this)
            .withHeader(R.layout.nav_header_main)
            .addDrawerItems(
                liveMovie, shopping, sosMed, info,
                DividerDrawerItem(),
                loginStatus, changePass ,about
            )
            .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when(position) {
                        in 1..4 -> view_pager.currentItem = position-1
                        6 -> {
                            if (isLogin) {
                                sessionViewModel.logout()
                            } else {
                                val intent = Intent(this@MainActivity, AuthActivity::class.java)
                                intent.putExtra(AppKey.ACTIVITY_KEY().AUTH_ACT, AppKey.FRAGMENT_KEY().LOGIN_F)
                                startActivity(intent)
                            }
                        }
                        7 -> {
                            if (isLogin) {
                                val intent = Intent(this@MainActivity, WebViewActivity::class.java)
                                intent.putExtra(WebViewActivity.KEY_URL, "https://eyeplus.co.id/login/")
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)
                            } else {
                                Toast.makeText(applicationContext, "Please Login First", Toast.LENGTH_SHORT).show()
                            }
                        }
                        8 -> {
                            val intent = Intent(this@MainActivity, SecondaryActivity::class.java)
                            intent.putExtra(AppKey.ACTIVITY_KEY().SEC_ACT, AppKey.FRAGMENT_KEY().ABOUT_F)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }
                    }
                    return false
                }

            })
            .build()

        val myHeader: View = result.header!!
        val navNm = myHeader.findViewById<TextView>(R.id.nav_profile_name)
        if (isLogin) {
            navNm.text = if (user.email != "") user.email else user.phone
            navNm.setOnClickListener{
                val intent = Intent(this@MainActivity, AuthActivity::class.java)
                intent.putExtra(AppKey.ACTIVITY_KEY().AUTH_ACT, AppKey.FRAGMENT_KEY().CHANG_PASS_F)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        } else {
            navNm.text = "My Profile"
        }
        val navImg = myHeader.findViewById<CircularImageView>(R.id.nav_header_profile)
        if (isLogin) {
            if (user.profile.toLowerCase() == "null" || user.profile == "" || user.profile == null
                || isChange().profile == "null" || isChange().profile == ""
            ) {
                Picasso.get()
                    .load(Uri.parse("https://cdn.iconscout.com/icon/free/png-256/account-profile-avatar-man-circle-round-user-30452.png"))
                    .into(navImg)
            } else {
                Picasso.get()
                    .load(APIClient.IMAGE_PATH + user.profile)
                    .into(navImg)
            }
        } else {
            Picasso.get()
                .load(Uri.parse("https://cdn.iconscout.com/icon/free/png-256/account-profile-avatar-man-circle-round-user-30452.png"))
                .into(navImg)
        }
        navImg.setImageDrawable(getDrawable(R.drawable.eyeplus24))
        // toolbar
//        my_logo.setImageResource(R.drawable.eyeplus65)
        mToolbar.setNavigationOnClickListener{
            if (result.isDrawerOpen) {
                result.closeDrawer()
            } else {
                result.openDrawer()
            }
        }

    }

    private fun isChange(): User {
        sessionViewModel.getUser().observe(this, Observer {
            this.user = it
        })
        return user
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) { }

    override fun onTabSelected(tab: TabLayout.Tab) {
        view_pager.currentItem = tab.position
    }
}
