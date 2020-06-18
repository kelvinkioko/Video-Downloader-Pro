package video.downloader.plus.webview.web

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebStorage
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebChromeClient
import android.webkit.ConsoleMessage
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient
import android.webkit.WebResourceResponse
import androidx.annotation.AttrRes
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocketFactory
import video.downloader.plus.common.R as CommonResources
import video.downloader.plus.webview.ApplicationGraph
import video.downloader.plus.webview.VideoContentSearch
import video.downloader.plus.webview.web_css.WebCssModule

class MainWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : WebView(context, attrs, defStyleAttr) {

    var defaultSSLSF: SSLSocketFactory? = null
    var browserWebViewListener: BrowserWebViewListener? = null
    var videoSearchViewListener: VideoSearchViewListener? = null
    private val attributes = extractAttributes(context, attrs, defStyleAttr)

    init {
        if (!isInEditMode) {
            isFocusableInTouchMode = true
            isHorizontalScrollBarEnabled = true
            isVerticalScrollBarEnabled = true
            scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                isScrollbarFadingEnabled = true
            }
            isScrollContainer = true
            isClickable = true

            val settings = settings
            settings.mediaPlaybackRequiresUserGesture = false
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = false

            defaultSSLSF = HttpsURLConnection.getDefaultSSLSocketFactory()

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    Log.d("jm/debug", consoleMessage.message() + " @ " + consoleMessage.lineNumber())
                    return true
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    url?.let {
                        injectCSS(it)
                    }
                    if (browserWebViewListener != null) {
                        browserWebViewListener!!.onProgressChanged()
                    }
                }
            }
            webViewClient = object : WebViewClient() {

                private val loadedUrls = HashMap<String, Boolean>()

                override fun onPageFinished(view: WebView, url: String) {
                    if (browserWebViewListener != null) {
                        browserWebViewListener!!.onPageFinished()
                    }
                }

                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    // The Error Page doesn't work inside API 17 et 18
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        return
                    }
                }

                override fun onLoadResource(view: WebView?, url: String?) {
                    super.onLoadResource(view, url)
                    val page = view!!.url
                    val title = view.title
                    if ((!url.toString().startsWith("https://www.youtube.com/") || !url.toString().startsWith("https://m.youtube.com/")) && url!!.isNotEmpty()) {
                        object : VideoContentSearch(context, url, page, title) {
                            override fun onStartInspectingURL() {
                                if (videoSearchViewListener != null) {
                                    Log.e("listener isn't", "Empty")
                                    videoSearchViewListener!!.onStartInspectingURL()
                                }else{
                                    Log.e("listener is", "Empty")
                                }
                            }

                            override fun onFinishedInspectingURL(finishedAll: Boolean) {
                                HttpsURLConnection.setDefaultSSLSocketFactory(defaultSSLSF)
                                if (finishedAll) {
                                    if (videoSearchViewListener != null) {
                                        videoSearchViewListener!!.onFinishedInspectingURL(finishedAll)
                                    }
                                }
                            }

                            override fun onVideoFound(size: String?, type: String?, link: String?, name: String?, page: String?, chunked: Boolean, website: String?) {
                                if (videoSearchViewListener != null) {
                                    videoSearchViewListener!!.onVideoFound(size!!, type!!, link!!, name!!, page!!, chunked, website!!)
                                }
                                Log.e("video download url", link!!)
                            }
                        }.start()
                    }
                }

                override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                    return super.shouldInterceptRequest(view, request)
                }
            }

            attributes.sourceUrl?.let {
                load(it)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (browserWebViewListener != null) {
            browserWebViewListener!!.onPageTouched()
        }
        return super.onTouchEvent(event)
    }

    fun clearData() {
        clearHistory()
        clearCache(true)
        clearMatches()
        clearCookies()
        WebStorage.getInstance().deleteAllData()
    }

    fun load(url: String) {
        loadUrl(url)
    }

    interface BrowserWebViewListener {
        fun onProgressChanged()
        fun onPageFinished()
        fun onPageTouched()
    }

    interface VideoSearchViewListener {
        fun onStartInspectingURL()
        fun onFinishedInspectingURL(finishedAll: Boolean)
        fun onVideoFound(size: String?, type: String?, link: String?, name: String?, page: String?, chunked: Boolean, website: String?)
    }

    private fun clearCookies() {
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()
    }

    private val webCssManagerInternal by lazy { WebCssModule(context).createWebCssManager() }

    private fun injectCSS(url: String) {
        val webCssManager = ApplicationGraph.getWebCssManager()
        val css = webCssManager.getCss(url) ?: return
        loadCss(css)
    }

    private fun loadCss(cssContent: String) {
        loadUrl("javascript:(function() {" +
            "var parent = document.getElementsByTagName('head').item(0);" +
            "var style = document.createElement('style');" +
            "style.type = 'text/css';" +
            "style.innerHTML = window.atob('" + cssContent + "');" +
            "parent.appendChild(style)" +
            "})()"
        )
    }

    private fun extractAttributes(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int): Attributes {
        val typedArray = context.obtainStyledAttributes(attrs, CommonResources.styleable.MainWebView, defStyleAttr, 0)
        val sourceUrl = typedArray.getString(CommonResources.styleable.MainWebView_src_url)
        typedArray.recycle()
        return Attributes(
            sourceUrl
        )
    }

    private inner class Attributes internal constructor(
        internal val sourceUrl: String?
    )
}
