package video.downloader.plus.webview.web_css

import android.content.Context
import video.downloader.plus.webview.theme.ThemeModule
import video.downloader.plus.webview.web_css.WebCssManager
import video.downloader.plus.webview.web_css.WebCssManagerImpl

class WebCssModule(private val context: Context) {

    private val themeManagerInternal by lazy { ThemeModule(context).createThemeManager() }

    fun createWebCssManager(): WebCssManager {
        val themeManager = themeManagerInternal
        val addOn = createWebCssManagerAddOn()
        return WebCssManagerImpl(
            themeManager,
            addOn
        )
    }

    private fun createWebCssManagerAddOn() = object : WebCssManagerImpl.AddOn {
        override fun openAssets(fileName: String) = context.assets.open(fileName)
    }
}
