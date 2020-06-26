package video.downloader.plus.webview

import android.annotation.SuppressLint
import android.content.Context
import video.downloader.plus.webview.floating.FloatingModule
import video.downloader.plus.webview.main_thread.MainThreadModule
import video.downloader.plus.webview.network.NetworkModule
import video.downloader.plus.webview.search_engine.SearchEngineModule
import video.downloader.plus.webview.suggestion.SuggestionModule
import video.downloader.plus.webview.web_css.WebCssModule

class ApplicationGraph(private val context: Context) {

    private val floatingManagerInternal by lazy { FloatingModule(context).createFloatingManager() }
    private val mainThreadPostInternal by lazy { MainThreadModule().createMainThreadPost() }
    private val okHttpClientLazyInternal by lazy { NetworkModule(context).createOkHttpClientLazy() }
    private val networkManagerInternal by lazy { NetworkModule(context).createNetworkManager() }
    private val searchEngineManagerInternal by lazy { SearchEngineModule().createSearchEngineManager() }
    private val suggestionManagerInternal by lazy { SuggestionModule().createSuggestionManager() }
    private val webCssManagerInternal by lazy { WebCssModule(context).createWebCssManager() }

    companion object {
        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        private var graph: ApplicationGraph? = null

        fun getFloatingManager() = graph!!.floatingManagerInternal
        fun getMainThreadPost() = graph!!.mainThreadPostInternal
        fun getOkHttpClientLazy() = graph!!.okHttpClientLazyInternal
        fun getNetworkManager() = graph!!.networkManagerInternal
        fun getSearchEngineManager() = graph!!.searchEngineManagerInternal
        fun getSuggestionManager() = graph!!.suggestionManagerInternal
        fun getWebCssManager() = graph!!.webCssManagerInternal

        @JvmStatic
        fun init(context: Context) {
            if (graph == null) {
                graph =
                    ApplicationGraph(context.applicationContext)
            }
        }
    }
}