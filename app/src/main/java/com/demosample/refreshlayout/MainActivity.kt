package com.demosample.refreshlayout

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.demosample.refreshlayout.MyCustomWebView.OnScrollChangedCallback


class MainActivity : AppCompatActivity() {

    private lateinit var webDisplayer: MyCustomWebView
    private lateinit var swipeRefresher: SwipeRefreshLayout
    private lateinit var currentUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentUrl = "https://www.amazon.com/"


        swipeRefresher = findViewById(R.id.swipeRefreshLayout)
        webDisplayer = findViewById(R.id.webview)
        swipeRefresher.isEnabled = false

        swipeRefresher.setOnRefreshListener {
            webDisplayer.reload()
        }
        loadPage()


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.desktopVersion -> {
                if (item.isChecked) {
                    item.isChecked = false
                    setDesktopMode(webDisplayer, false)
                } else {
                    item.isChecked = true
                    setDesktopMode(webDisplayer, true)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadPage() {
        webDisplayer.loadUrl(currentUrl)

        webDisplayer.onScrollChangedCallback = object : OnScrollChangedCallback {
            override fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int) {
                swipeRefresher.isEnabled = t == 0
            }
        }
        webDisplayer.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                view.loadUrl(request.url.toString())
                return true
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("TAG", "shouldOverrideUrlLoading: " + url.toString())
                swipeRefresher.isRefreshing = false

            }



        }
        webDisplayer.settings.loadsImagesAutomatically = true
        webDisplayer.settings.javaScriptEnabled = true

        webDisplayer.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webDisplayer.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
    }

    fun setDesktopMode(webView: WebView, enabled: Boolean) {
        var newUserAgent = webView.settings.userAgentString
        if (enabled) {
            try {
                newUserAgent =
                    "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0"

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            newUserAgent = null
        }
        webView.settings.userAgentString = newUserAgent
        webView.settings.useWideViewPort = enabled
        if (enabled) {
            webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        } else {
            webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        }
        webView.settings.loadWithOverviewMode = enabled
        webView.settings.setSupportZoom(true)
        webView.settings.loadWithOverviewMode = enabled
        webView.settings.builtInZoomControls = enabled
        webView.settings.displayZoomControls = false
        webView.reload()
    }


}