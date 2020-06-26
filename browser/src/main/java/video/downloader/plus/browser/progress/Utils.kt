package video.downloader.plus.browser.progress

import android.content.res.Resources

fun dpToPx(dp: Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()