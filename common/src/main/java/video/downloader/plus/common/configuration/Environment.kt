package video.downloader.plus.common.configuration

sealed class Environment(val url: String) {
    object Production : Environment("http://64.227.64.76")
}