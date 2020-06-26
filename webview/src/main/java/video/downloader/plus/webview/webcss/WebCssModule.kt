package video.downloader.plus.webview.webcss

import android.content.Context

class WebCssModule(private val context: Context) {

    fun createWebCssManager(): WebCssManager {
        val addOn = createWebCssManagerAddOn()
        return WebCssManagerImpl(
            addOn
        )
    }

    private fun createWebCssManagerAddOn() = object : WebCssManagerImpl.AddOn {
        override fun openAssets(fileName: String) = context.assets.open(fileName)
    }
}
