package video.downloader.plus.webview.web_css

import android.util.Base64
import java.io.InputStream

class WebCssManagerImpl(
    private val addOn: AddOn
) : WebCssManager {

    private val googleDarkThemeOnlyCss by createGoogleDarkThemeOnlyCss()
    private val googleCommonOnlyCss by createGoogleCommonOnlyCss()
    private val googleLightThemBase64Css by createGoogleLightThemBase64Css()
    private val youtubeMobileDarkThemeOnlyCss by createYoutubeMobileDarkThemeOnlyCss()
    private val youtubeMobileDarkThemBase64Css by createYoutubeMobileDarkThemBase64Css()

    override fun getCss(
        url: String
    ): String? {
        if (url.startsWith("https://www.google.")) {
            return googleLightThemBase64Css
        }
        if (url.startsWith("https://m.youtube.com")) {
            return youtubeMobileDarkThemBase64Css
        }
        return null
    }

    private fun createGoogleDarkThemeOnlyCss() = lazy(LazyThreadSafetyMode.NONE) {
        val inputStream = addOn.openAssets("dark-theme-google.css")
        inputStream.bufferedReader().use { it.readText() }
    }

    private fun createGoogleCommonOnlyCss() = lazy(LazyThreadSafetyMode.NONE) {
        val inputStream = addOn.openAssets("common-google.css")
        inputStream.bufferedReader().use { it.readText() }
    }

    private fun createGoogleLightThemBase64Css() = lazy(LazyThreadSafetyMode.NONE) {
        Base64.encodeToString(
            googleCommonOnlyCss.toByteArray(),
            Base64.NO_WRAP
        )
    }

    private fun createGoogleDarkThemBase64Css() = lazy(LazyThreadSafetyMode.NONE) {
        Base64.encodeToString(
            "$googleCommonOnlyCss\n$googleDarkThemeOnlyCss".toByteArray(),
            Base64.NO_WRAP
        )
    }

    private fun createYoutubeMobileDarkThemeOnlyCss() = lazy(LazyThreadSafetyMode.NONE) {
        val inputStream = addOn.openAssets("dark-theme-m-youtube.css")
        inputStream.bufferedReader().use { it.readText() }
    }

    private fun createYoutubeMobileDarkThemBase64Css() = lazy(LazyThreadSafetyMode.NONE) {
        Base64.encodeToString(
            youtubeMobileDarkThemeOnlyCss.toByteArray(),
            Base64.NO_WRAP
        )
    }

    interface AddOn {

        fun openAssets(
            fileName: String
        ): InputStream
    }
}
