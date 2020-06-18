package video.downloader.plus.webview.suggestion

import video.downloader.plus.webview.ApplicationGraph

class SuggestionModule {

    fun createSuggestionManager(): SuggestionManager {
        val okHttpClientLazy = ApplicationGraph.getOkHttpClientLazy()
        val mainThreadPost = ApplicationGraph.getMainThreadPost()
        val suggestionApi = SuggestionApiImpl(okHttpClientLazy)
        return SuggestionManagerImpl(
            suggestionApi,
            mainThreadPost
        )
    }
}
