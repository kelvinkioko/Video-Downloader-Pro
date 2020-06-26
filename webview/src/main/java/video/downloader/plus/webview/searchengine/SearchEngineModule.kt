package video.downloader.plus.webview.searchengine

class SearchEngineModule {

    fun createSearchEngineManager(): SearchEngineManager {
        return SearchEngineManagerImpl()
    }
}
