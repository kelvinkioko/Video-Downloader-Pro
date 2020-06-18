package video.downloader.plus.webview.suggestion

import android.content.Context
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import video.downloader.plus.webview.ApplicationGraph
import video.downloader.plus.webview.R
import video.downloader.plus.webview.theme.Theme
import video.downloader.plus.webview.theme.ThemeManager

class SuggestionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_suggest, this)
    private val text: TextView = view.findViewById(R.id.view_suggestion_text)
    private val image: ImageView = view.findViewById(R.id.view_suggestion_image)
    private val themeManager by lazy { ApplicationGraph.getThemeManager() }
    private val themeListener = createThemeListener()
    private var listener: Listener? = null

    init {
        text.setOnClickListener {
            listener?.onSuggestionTestClicked()
        }
        image.setOnClickListener {
            listener?.onSuggestionImageClicked()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        themeManager.registerThemeListener(themeListener)
        updateTheme()
    }

    override fun onDetachedFromWindow() {
        themeManager.unregisterThemeListener(themeListener)
        super.onDetachedFromWindow()
    }

    fun setText(spanned: Spanned) {
        text.text = spanned
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun updateTheme(theme: Theme = themeManager.getTheme()) {
        text.setTextColor(ContextCompat.getColor(context, theme.textPrimaryColorRes))
        image.setColorFilter(ContextCompat.getColor(context, theme.textSecondaryColorRes))
    }

    private fun createThemeListener() = object : ThemeManager.ThemeListener {
        override fun onThemeChanged() {
            updateTheme()
        }
    }

    interface Listener {
        fun onSuggestionTestClicked()
        fun onSuggestionImageClicked()
    }
}
