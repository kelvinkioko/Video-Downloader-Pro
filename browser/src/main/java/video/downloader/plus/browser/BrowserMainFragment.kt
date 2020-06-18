package video.downloader.plus.browser

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import video.downloader.plus.browser.databinding.BrowserMainBinding
import video.downloader.plus.common.extensions.viewBinding

class BrowserMainFragment : Fragment(R.layout.browser_main) {

    private val binding by viewBinding(BrowserMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}
