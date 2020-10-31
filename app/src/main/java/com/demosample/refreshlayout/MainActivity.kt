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
        currentUrl = "https://www.flipkart.com/"


        swipeRefresher = findViewById(R.id.swipeRefreshLayout)
        webDisplayer = findViewById(R.id.webview)
        swipeRefresher.isEnabled = false

        swipeRefresher.setOnRefreshListener {
            webDisplayer.reload()
        }
        loadMobilePage()


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
                    loadMobilePage()
                } else {
                    item.isChecked = true
                    loadDesktopPage()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadDesktopPage() {
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
                currentUrl = request.url.toString()
                view.loadUrl(request.url.toString())
                return true
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("TAG", "shouldOverrideUrlLoading: " + url.toString())
                swipeRefresher.isRefreshing = false

            }

            override fun onLoadResource(view: WebView?, url: String?) {
                view!!.evaluateJavascript(
                    "document.querySelector('meta[name=\"viewport\"]').setAttribute('content', 'width=1024px, initial-scale=' + (document.documentElement.clientWidth / 1024));",
                    null
                )
            }

        }
        webDisplayer.settings.loadsImagesAutomatically = true
        webDisplayer.settings.javaScriptEnabled = true
        webDisplayer.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webDisplayer.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
        webDisplayer.settings.loadWithOverviewMode = true
        webDisplayer.settings.useWideViewPort = true
        webDisplayer.settings.setSupportZoom(true)
        webDisplayer.settings.builtInZoomControls = true
        webDisplayer.setInitialScale(1)
        webDisplayer.settings.displayZoomControls = false
    }

    private fun loadMobilePage() {
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
                currentUrl = request.url.toString()
                view.loadUrl(request.url.toString())
                return true
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                currentUrl = url!!
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


}