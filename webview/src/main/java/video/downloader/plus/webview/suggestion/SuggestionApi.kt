package video.downloader.plus.webview.suggestion

interface SuggestionApi {

    fun getSuggestionsJson(
        search: String
    ): String?

    fun getSuggestionsJson(
        search: String,
        block: (String) -> Unit
    )
}
