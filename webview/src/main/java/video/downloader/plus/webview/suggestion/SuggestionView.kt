package video.downloader.plus.webview.suggestion

import android.content.Context
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import video.downloader.plus.webview.R

class SuggestionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_suggest, this)
    private val text: TextView = view.findViewById(R.id.view_suggestion_text)
    private val image: ImageView = view.findViewById(R.id.view_suggestion_image)
    private var listener: Listener? = null

    init {
        text.setOnClickListener {
            listener?.onSuggestionTestClicked()
        }
        image.setOnClickListener {
            listener?.onSuggestionImageClicked()
        }
    }

    fun setText(spanned: Spanned) {
        text.text = spanned
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun onSuggestionTestClicked()
        fun onSuggestionImageClicked()
    }
}
