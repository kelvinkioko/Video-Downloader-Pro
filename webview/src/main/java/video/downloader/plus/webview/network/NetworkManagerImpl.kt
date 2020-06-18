package video.downloader.plus.webview.network

import android.content.Context
import android.net.ConnectivityManager
import video.downloader.plus.webview.network.NetworkManager

class NetworkManagerImpl(private val context: Context) : NetworkManager {

    private val connectivityManager: ConnectivityManager by lazy(LazyThreadSafetyMode.NONE) {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun isNetworkAvailable() = connectivityManager.activeNetworkInfo != null &&
        connectivityManager.activeNetworkInfo.isConnected
}
