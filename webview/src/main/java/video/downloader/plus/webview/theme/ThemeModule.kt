package video.downloader.plus.webview.theme

import android.content.Context
import video.downloader.plus.webview.theme.ThemeManager
import video.downloader.plus.webview.theme.ThemeManagerImpl

class ThemeModule(private val context: Context) {

    fun createThemeManager(): ThemeManager {
        val sharedPreferences = context.getSharedPreferences(
            ThemeManagerImpl.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        return ThemeManagerImpl(
            sharedPreferences
        )
    }
}
