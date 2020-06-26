package video.downloader.plus.browser

import android.os.Bundle
import android.util.Log
import video.downloader.plus.webview.floating.FloatingManager
import video.downloader.plus.webview.searchengine.SearchEngineManager
import video.downloader.plus.webview.suggestion.SuggestionManager
import video.downloader.plus.webview.suggestion.Suggestions

internal class BrowserMainPresenter(
    private val screen: BrowserMainContract.Screen,
    private val searchEngineManager: SearchEngineManager,
    private val suggestionManager: SuggestionManager,
    private val floatingManager: FloatingManager
) : BrowserMainContract.UserAction {

    private val suggestionListener = createSuggestionListener()
    private var webViewVisible = false
    private var videoRadioButtonChecked = false
    private var search: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        suggestionManager.registerSuggestionListener(suggestionListener)
        val firstActivityLaunch = savedInstanceState == null
        if (firstActivityLaunch) {
            screen.showKeyboard()
            screen.hideClearInput()
            setWebViewVisible(webViewVisible)
        }
    }

    override fun onDestroy() {
        suggestionManager.unregisterSuggestionListener(suggestionListener)
    }

    override fun onNewIntent(url: String?) {
        if (url != null) {
            performSearch(url, true)
        }
    }

    override fun onResume() {
        if (!webViewVisible) {
            screen.showKeyboard()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("webViewVisible", webViewVisible)
    }

    override fun onRestoreInstanceState(outState: Bundle) {
        webViewVisible = outState.getBoolean("webViewVisible")
        setWebViewVisible(webViewVisible)
    }

    override fun onSearchInputChanged(search: String) {
        this.search = search
        if (search == "") {
            screen.hideClearInput()
            screen.hideSuggestions()
        } else {
            screen.showClearInput()
            suggestionManager.getSuggestion(search)
        }
    }

    override fun onSearchPerformed(search: String) {
        performSearch(search, false)
    }

    override fun onHomeClicked() {
        val url = searchEngineManager.getHomeUrl()
        performSearch(url, false)
    }

    override fun onClearDataClicked() {
        screen.showUrl(searchEngineManager.getHomeUrl())
        screen.clearData()
        screen.showClearDataMessage()
        setWebViewVisible(false)
    }

    override fun onSettingsClicked() {
        screen.navigateSettings()
    }

    override fun onPageLoadProgressChanged(progressPercent: Int) {
        if (progressPercent >= 100) {
            screen.hideLoader()
        } else {
            screen.showLoader(progressPercent)
        }
    }

    override fun onPageTouched() {
        screen.hideKeyboard()
    }

    override fun onBackPressed(emptyViewVisible: Boolean) {
        Log.e("Back click response", emptyViewVisible.toString())
        if (emptyViewVisible) {
            screen.quit()
            return
        }
        if (screen.webViewCanGoBack()) {
            screen.webViewBack()
            return
        } else {
            setWebViewVisible(false)
        }
    }

    override fun onFabClearClicked() {
        screen.showUrl(searchEngineManager.getHomeUrl())
        screen.clearData()
        screen.showClearDataMessage()
        setWebViewVisible(false)
    }

    override fun onInputClearClicked() {
        screen.setInput("")
        screen.hideSuggestions()
        screen.hideClearInput()
        screen.showKeyboard()
    }

    override fun onVideoCheckedChanged(checked: Boolean) {
        videoRadioButtonChecked = checked
    }

    override fun onQuitClicked() {
        screen.quit()
    }

    override fun onSuggestionClicked(suggestion: String) {
        val search = suggestion.replace("<b>", "").replace("</b>", "")
        val url = convertSearchToUrl(search)
        screen.showUrl(url)
        screen.resetSearchInput()
        setWebViewVisible(true)
    }

    override fun onSuggestionImageClicked(suggestion: String) {
        val search = suggestion.replace("<b>", "").replace("</b>", "")
        screen.setInput(search)
    }

    private fun performSearch(search: String, forceNonFloating: Boolean) {
        val url = convertSearchToUrl(search)
        screen.resetSearchInput()
        if (!forceNonFloating && screen.isFloatingWindowChecked()) {
            floatingManager.start(FloatingManager.Configuration.default(url))
            return
        }
        screen.showUrl(url)
        setWebViewVisible(true)
    }

    private fun convertSearchToUrl(search: String) = if (videoRadioButtonChecked) {
        searchEngineManager.createSearchVideoUrl(search)
    } else {
        searchEngineManager.createSearchUrl(search)
    }

    private fun setWebViewVisible(visible: Boolean) {
        webViewVisible = visible
        if (visible) {
            screen.hideToolbar()
            screen.showWebView()
            screen.hideEmptyView()
            screen.showLoader(0)
            screen.hideKeyboard()
            screen.hideSuggestions()
        } else {
            screen.showToolbar()
            screen.hideLoader()
            screen.hideWebView()
            screen.showEmptyView()
            screen.showKeyboard()
            if (search == "") {
                screen.hideSuggestions()
            }
        }
    }

    private fun createSuggestionListener() = object : SuggestionManager.SuggestionListener {
        override fun onSuggestionEnded(suggestions: Suggestions) {
            if (suggestions.searchInput != search) {
                return
            }
            screen.showSuggestions(suggestions.suggestions)
        }
    }
}
