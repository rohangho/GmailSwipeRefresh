package com.demosample.refreshlayout

import android.os.Bundle
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
        currentUrl = "https://www.gmail.com/"


        swipeRefresher = findViewById(R.id.swipeRefreshLayout)
        webDisplayer = findViewById(R.id.webview)
        swipeRefresher.isEnabled= false

        swipeRefresher.setOnRefreshListener {
            webDisplayer.reload()
        }
        loadWebPage()


    }

//    override fun onStop() {
//        swipeRefresher.viewTreeObserver.removeOnScrollChangedListener(mOnScrollChangedListener);
//
//        super.onStop()
//    }


    /**
     * Load of web view
     */

    private fun loadWebPage() {
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
                swipeRefresher.isRefreshing = false

            }


        }
        webDisplayer.settings.loadsImagesAutomatically = true
        webDisplayer.settings.javaScriptEnabled = true

        webDisplayer.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webDisplayer.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
    }




}