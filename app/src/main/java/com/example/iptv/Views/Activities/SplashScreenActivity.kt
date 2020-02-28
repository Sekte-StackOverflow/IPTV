package com.example.iptv.Views.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProviders
import com.example.iptv.BuildConfig
import com.example.iptv.R
import com.example.iptv.ViewModels.SessionViewModel
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharePref = getSharedPreferences("My_DIALOG", Context.MODE_PRIVATE)
        sharePref.edit().putBoolean("INIT_START", true).apply()
        splash_appVer.text = "v ${BuildConfig.VERSION_NAME}"
        Handler().postDelayed({
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 500)
    }
}
