package video.downloader.plus.webview.floating

import video.downloader.plus.webview.searchengine.SearchEngineManager

class FloatingPresenter(
    private val screen: FloatingContract.Screen,
    private val searchEngineManager: SearchEngineManager
) : FloatingContract.UserAction {

    override fun onAttachedToWindow() {}

    override fun onDetachedFromWindow() {}

    override fun onQuitClicked() {
        screen.removeFromWindowManager()
    }

    override fun onFullscreenClicked(url: String) {
        screen.navigateToMainActivity(url)
        screen.removeFromWindowManager()
    }

    override fun onCollapseClicked() {
        if (screen.isCollapsed()) {
            screen.expand()
            screen.showStatusBarTitle()
        } else {
            screen.collapse()
            screen.hideStatusBarTitle()
        }
    }

    override fun onHomeClicked() {
        val homeUrl = searchEngineManager.getHomeUrl()
        screen.loadUrl(homeUrl)
    }

    override fun onLoad(configuration: FloatingManager.Configuration) {
        screen.loadUrl(configuration.url)
        if (configuration.fullscreenButtonVisible) {
            screen.showFullscreenButton()
        } else {
            screen.hideFullscreenButton()
        }
    }
}
