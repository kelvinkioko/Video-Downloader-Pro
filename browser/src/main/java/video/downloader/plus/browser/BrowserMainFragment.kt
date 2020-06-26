package video.downloader.plus.browser

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.browser_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import video.downloader.plus.browser.databinding.BrowserMainBinding
import video.downloader.plus.common.extensions.viewBinding
import video.downloader.plus.webview.ApplicationGraph
import video.downloader.plus.webview.keyboard.KeyboardUtils
import video.downloader.plus.webview.suggestion.SuggestionAdapter
import video.downloader.plus.webview.web.MainWebView

@ExperimentalCoroutinesApi
@FlowPreview
class BrowserMainFragment : Fragment(R.layout.browser_main), BrowserMainContract.Screen {

    private val binding by viewBinding(BrowserMainBinding::bind)

    private val videoSearchViewListener = createVideoSearchViewListener()
    private val browserWebViewListener = createBrowserWebViewListener()
    private val suggestionsAdapter = createSuggestionAdapter()
    private val userAction = createUserAction()
    private var forceDestroy = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        browserWebView.videoSearchViewListener = videoSearchViewListener
        browserWebView.browserWebViewListener = browserWebViewListener
        browserWebView.setBackgroundColor(Color.TRANSPARENT)
        browserSearch.setOnEditorActionListener(createOnEditorActionListener())
        browserSearch.addTextChangedListener(createTextWatcher())
        browserClearInput.setOnClickListener { userAction.onInputClearClicked() }
        browserRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        browserRecyclerView.adapter = suggestionsAdapter
        userAction.onCreate(savedInstanceState)

        binding.downloadBackground.clicks().debounce(350).onEach { findNavController().navigate(R.id.toBrowserAvailableDownloads) }.launchIn(lifecycleScope)

        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()

        requireView().setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                Toast.makeText(requireContext(), "Back Pressed", Toast.LENGTH_SHORT).show()
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(requireContext(), "Back Pressed", Toast.LENGTH_SHORT).show()
                    return true
                }
                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (forceDestroy) {
            return
        }
        browserWebView.browserWebViewListener = null
        userAction.onDestroy()
        Toast.makeText(requireContext(), "View destroyed", Toast.LENGTH_LONG).show()
    }

    // override
    fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
        val url = intent?.extras?.getString(EXTRA_URL)
        userAction.onNewIntent(url)
    }

    override fun onResume() {
        super.onResume()
        userAction.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (forceDestroy) {
            return
        }
        userAction.onSaveInstanceState(outState)
        browserWebView.saveState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            userAction.onRestoreInstanceState(savedInstanceState)
            browserWebView.restoreState(savedInstanceState)
        }
    }

    // override
    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            userAction.onBackPressed(browserIntroImage.visibility == View.VISIBLE)
            return true
        }
        return false
    }

    override fun showUrl(url: String) {
        browserSearch.setText(url)
        browserWebView.load(url)
    }

    override fun reload() {
        browserWebView.reload()
    }

    override fun webViewCanGoBack() = browserWebView.canGoBack()

    override fun webViewBack() {
        browserWebView.goBack()
    }

    override fun quit() {
        requireActivity().finish()
    }

    override fun navigateSettings() {}

    override fun clearData() {
        browserWebView.clearData()
    }

    override fun showClearDataMessage() {}

    override fun showLoader(progressPercent: Int) {
        browserProgress.visibility = View.VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            browserProgress.setProgress(progressPercent, true)
        } else {
            browserProgress.progress = progressPercent
        }
    }

    override fun hideLoader() {
        browserProgress.visibility = View.GONE
    }

    override fun showKeyboard() {
        browserSearch.postDelayed({
            browserSearch.isFocusableInTouchMode = true
            browserSearch.requestFocus()
            KeyboardUtils.showSoftInput(browserSearch)
        }, 200)
    }

    override fun hideKeyboard() {
        KeyboardUtils.hideSoftInput(browserSearch)
    }

    override fun resetSearchInput() {
        browserSearch.setText("")
    }

    override fun setWindowBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(requireContext(), colorRes)
        requireActivity().window.setBackgroundDrawable(ColorDrawable(color))
        browserRecyclerView.setBackgroundColor(color)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun setStatusBarBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(requireContext(), colorRes)
        requireActivity(). window.statusBarColor = color
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun setStatusBarDark(statusBarDark: Boolean) {
        val flags = requireActivity().window.decorView.systemUiVisibility
        requireActivity().window.decorView.systemUiVisibility = if (statusBarDark)
            flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        else
            flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    override fun setToolbarBackgroundColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(requireContext(), colorRes)
        toolbar.setBackgroundColor(color)
    }

    override fun setPrimaryTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(requireContext(), colorRes)
        browserSearch.setTextColor(color)
    }

    override fun setSecondaryTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(requireContext(), colorRes)
        browserRefresh.setColorFilter(color)
        browserClearInput.setColorFilter(color)
    }

    override fun setAccentTextColorRes(@ColorRes colorRes: Int) {
        val color = ContextCompat.getColor(requireContext(), colorRes)
    }

    override fun showToolbar() {
        toolbar.visibility = View.VISIBLE
        browserToolbarShadow.visibility = View.VISIBLE
    }

    override fun hideToolbar() {
        toolbar.visibility = View.VISIBLE
        browserToolbarShadow.visibility = View.VISIBLE
    }

    override fun showSuggestions(suggestions: List<String>) {
        browserRecyclerView.visibility = View.VISIBLE
//        suggestionsAdapter.populate(suggestions)
    }

    override fun hideSuggestions() {
        browserRecyclerView.visibility = View.GONE
    }

    override fun showClearInput() {
        browserClearInput.visibility = View.VISIBLE
    }

    override fun hideClearInput() {
        browserClearInput.visibility = View.GONE
    }

    override fun showWebView() {
        browserWebView.visibility = View.VISIBLE
        downloadBackground.visibility = View.VISIBLE
        downloadCounter.visibility = View.VISIBLE
        downloadLoader.visibility = View.VISIBLE
    }

    override fun hideWebView() {
        browserWebView.visibility = View.GONE
        downloadBackground.visibility = View.GONE
        downloadCounter.visibility = View.GONE
        downloadLoader.visibility = View.GONE
    }

    override fun showEmptyView() {
        browserIntroImage.visibility = View.VISIBLE
        browserIntroMessage.visibility = View.VISIBLE
        browserSearch.setText("")
    }

    override fun hideEmptyView() {
        Toast.makeText(requireContext(), "This view has been hidden", Toast.LENGTH_SHORT).show()
        browserIntroImage.visibility = View.GONE
        browserIntroMessage.visibility = View.GONE
    }

    override fun isFloatingWindowChecked() = false

    override fun setInput(inputString: String) {
        browserSearch.setText(inputString)
        browserSearch.setSelection(browserSearch.text.length)
    }

    override fun showFloatingWindowCheckbox() {
//        emptyViewFloatingCheckBox.visibility = View.VISIBLE
    }

    override fun hideFloatingWindowCheckbox() {
//        emptyViewFloatingCheckBox.visibility = View.GONE
    }

    private fun createBrowserWebViewListener() = object : MainWebView.BrowserWebViewListener {
        override fun onPageFinished() {
            userAction.onPageLoadProgressChanged(100)
        }

        override fun onProgressChanged() {
            userAction.onPageLoadProgressChanged(browserWebView.progress)
            browserRecyclerView.visibility = View.GONE
        }

        override fun onPageTouched() {
            userAction.onPageTouched()
        }
    }

    private fun createVideoSearchViewListener() = object : MainWebView.VideoSearchViewListener {
        override fun onStartInspectingURL() {
            Handler(Looper.getMainLooper()).post {
                downloadLoader.visibility = View.VISIBLE
            }
        }

        override fun onFinishedInspectingURL(finishedAll: Boolean) {
            if (finishedAll) {
                Handler(Looper.getMainLooper()).post {
                    downloadLoader.visibility = View.GONE
//                    downloadCounter.text = downloadsEntity.size.toString()
                }
            }
        }

        override fun onVideoFound(size: String?, type: String?, link: String?, name: String?, page: String?, chunked: Boolean, website: String?) {
            if (!size.equals("0")) {
//                val download = DownloadsEntity(0, name!!, link!!, "", "1", size!!, "")
//                if (!downloadsEntity.contains(download)) {
//                    downloadsEntity.add(download)
//                }
            }
            Handler(Looper.getMainLooper()).post {}
        }
    }

    private fun createOnEditorActionListener() = TextView.OnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
            event.action == KeyEvent.ACTION_DOWN &&
            event.keyCode == KeyEvent.KEYCODE_ENTER) {
            userAction.onSearchPerformed(v!!.text.toString())
            return@OnEditorActionListener true
        }
        false
    }

    private fun createTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            userAction.onSearchInputChanged(browserSearch.text.toString())
        }
    }

    private fun createSuggestionAdapter() = SuggestionAdapter(object : SuggestionAdapter.SuggestionClickListener {
        override fun onSuggestionClicked(suggestion: String) {
            userAction.onSuggestionClicked(suggestion)
        }

        override fun onSuggestionImageClicked(suggestion: String) {
            userAction.onSuggestionImageClicked(suggestion)
        }
    })

    private fun createUserAction(): BrowserMainContract.UserAction {
        val searchEngineManager = ApplicationGraph.getSearchEngineManager()
        val suggestionManager = ApplicationGraph.getSuggestionManager()
        val floatingManager = ApplicationGraph.getFloatingManager()
        return BrowserMainPresenter(
            this,
            searchEngineManager,
            suggestionManager,
            floatingManager
        )
    }

    companion object {
        private const val EXTRA_URL = "EXTRA_URL"

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, BrowserMainFragment::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }

        @JvmStatic
        fun start(context: Context, url: String) {
            val intent = Intent(context, BrowserMainFragment::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            intent.putExtra(EXTRA_URL, url)
            context.startActivity(intent)
        }
    }
}
