package video.downloader.plus.domain.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import video.downloader.plus.domain.entity.BookmarksEntity
import video.downloader.plus.domain.entity.DownloadsEntity
import video.downloader.plus.domain.repository.LocalRepository

class localViewModel(application: Application) : AndroidViewModel(application) {

    private val localRepository: LocalRepository = LocalRepository(application)

    fun insertDownloads(downloads: DownloadsEntity) {
        localRepository.insertDownloads(downloads)
    }

    fun getDownloads(): LiveData<List<DownloadsEntity>> {
        return localRepository.getDownloads()
    }

    fun getDownloadsList(): List<DownloadsEntity> {
        return localRepository.getDownloadsList()
    }

    fun updateDownloads(downloaded: String, size: String, id: Int) {
        localRepository.updateDownloads(downloaded, size, id)
    }

    fun updateLocalURL(localurl: String, id: Int) {
        localRepository.updateLocalURL(localurl, id)
    }

    fun updateName(name: String, id: Int) {
        localRepository.updateName(name, id)
    }

    fun countDownload(url: String): Int {
        return localRepository.countDownload(url)
    }

    fun countDownloadsByUrl(local_url: String): Int {
        return localRepository.countDownloadsByUrl(local_url)
    }

    fun countDownloads(): Int {
        return localRepository.countDownloads()
    }

    fun getDownloadByUrl(url: String): DownloadsEntity {
        return localRepository.getDownloadByUrl(url)
    }

    fun deleteDownloadsByID(id: Int) {
        localRepository.deleteDownloadsByID(id)
    }

    fun deleteDownloads() {
        localRepository.deleteDownloads()
    }

    fun insertBookmarks(bookmarksEntity: BookmarksEntity) {
        localRepository.insertBookmarks(bookmarksEntity)
    }

    fun getBookmarks(): BookmarksEntity {
        return localRepository.getBookmarks()
    }

    fun listBookmarks(): List<BookmarksEntity> {
        return localRepository.listBookmarks()
    }

    fun countBookmarkByDomain(domain: String): Int {
        return localRepository.countBookmarkByDomain(domain)
    }

    fun countBookmarks(): Int {
        return localRepository.countBookmarks()
    }

    fun deleteBookmarkByDomain(domain: String) {
        localRepository.deleteBookmarkByDomain(domain)
    }

    fun deleteAllBookmarks() {
        localRepository.deleteAllBookmarks()
    }
}
