package com.example.iptv.Views.Activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.iptv.R
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.media_player_fragment.*

class WebViewActivity : AppCompatActivity() {
    private lateinit var STR_URL: String

    companion object {
        val KEY_URL = "URL_DATA"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        if (intent.getStringExtra("URL_DATA").isNullOrEmpty()) {
            Toast.makeText(applicationContext, "404 NOT_FOUND", Toast.LENGTH_SHORT).show()
        } else {
            STR_URL = intent.getStringExtra(KEY_URL)
        }

        webView_toolbar.setNavigationOnClickListener {
            val intent = Intent(this@WebViewActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        webView.webViewClient = MyWebClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = false
        webView.settings.displayZoomControls = false
        webView.loadUrl(STR_URL)

    }

    inner class MyWebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view!!.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }
    }
}
