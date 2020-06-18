package video.downloader.plus.buildsrc

object Versioning {
    const val major = 0
    const val minor = 0
    const val path = 0

    @JvmStatic
    fun generateVersionName() = "$major.$minor.$path"
}