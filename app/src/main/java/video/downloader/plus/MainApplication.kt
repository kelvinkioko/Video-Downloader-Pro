package video.downloader.plus

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Build
import android.webkit.WebView
import video.downloader.plus.webview.ApplicationGraph

/**
 * The [Application] of this project.
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupApplicationGraph()

        // Debuggable WebView
        if (BuildConfig.DEBUG) {
            enableDebuggableWebView()
        }
    }

    private fun setupApplicationGraph() {
        ApplicationGraph.init(this)
    }

    private fun enableDebuggableWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }
    }
}
