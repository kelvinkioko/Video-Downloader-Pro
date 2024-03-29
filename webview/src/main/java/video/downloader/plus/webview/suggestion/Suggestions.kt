package video.downloader.plus.webview.suggestion

import org.json.JSONArray
import org.json.JSONException

data class Suggestions(
    val searchInput: String,
    val suggestions: List<String>
) {

    companion object {

        fun createFromGoogle(searchInput: String, jsonStr: String): Suggestions {
            val suggestions = ArrayList<String>()
            val json = try {
                JSONArray(jsonStr)
            } catch (jsonException: JSONException) {
                return Suggestions(
                    searchInput,
                    suggestions
                )
            }
            val suggestionsJsonArray = json.getJSONArray(1)
            for (i in 0 until suggestionsJsonArray.length()) {
                suggestions.add(
                    suggestionsJsonArray.getJSONArray(i).getString(0)
                )
            }
            return Suggestions(
                json.getString(0),
                suggestions

            )
        }
    }
}
