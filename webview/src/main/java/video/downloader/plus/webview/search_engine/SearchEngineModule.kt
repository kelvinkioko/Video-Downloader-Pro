@file:Suppress("PackageName")

/* ktlint-disable package-name */
package video.downloader.plus.webview.search_engine

import video.downloader.plus.webview.search_engine.SearchEngineManager
import video.downloader.plus.webview.search_engine.SearchEngineManagerImpl

class SearchEngineModule {

    fun createSearchEngineManager(): SearchEngineManager {
        return SearchEngineManagerImpl()
    }

}
