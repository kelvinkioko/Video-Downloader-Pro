package video.downloader.plus

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import video.downloader.plus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        val url = intent?.extras?.getString(EXTRA_URL)
//        userAction.onNewIntent(url)
//    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {

            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
