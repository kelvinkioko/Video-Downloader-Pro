package video.downloader.plus.domain.repository

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import video.downloader.plus.domain.PlusDatabase
import video.downloader.plus.domain.dao.BookmarksDao
import video.downloader.plus.domain.dao.DownloadsDao
import video.downloader.plus.domain.entity.BookmarksEntity
import video.downloader.plus.domain.entity.DownloadsEntity

class LocalRepository(application: Application) {

    private val db = PlusDatabase.getDatabase(application)
    private val downloadsDao: DownloadsDao = db.downloadsDao()
    private val bookmarkDao: BookmarksDao = db.bookmarksDao()

    @WorkerThread
    fun insertDownloads(downloads: DownloadsEntity) {
        downloadsDao.insertDownloads(downloads)
    }

    fun getDownloads(): LiveData<List<DownloadsEntity>> {
        return downloadsDao.getDownloads()
    }

    fun getDownloadsList(): List<DownloadsEntity> {
        return downloadsDao.getDownloadsList()
    }

    fun updateDownloads(downloaded: String, size: String, id: Int) {
        downloadsDao.updateDownloads(downloaded, size, id)
    }

    fun updateLocalURL(localurl: String, id: Int) {
        downloadsDao.updateLocalURL(localurl, id)
    }

    fun updateName(name: String, id: Int) {
        downloadsDao.updateName(name, id)
    }

    fun countDownload(url: String): Int {
        return downloadsDao.countDownload(url)
    }

    fun countDownloadsByUrl(local_url: String): Int {
        return downloadsDao.countDownloadsByUrl(local_url)
    }

    fun countDownloads(): Int {
        return downloadsDao.countDownloads()
    }

    fun getDownloadByUrl(url: String): DownloadsEntity {
        return downloadsDao.getDownloadByUrl(url)
    }

    fun deleteDownloadsByID(id: Int) {
        downloadsDao.deleteDownloadsByID(id)
    }

    fun deleteDownloads() {
        downloadsDao.deleteDownloads()
    }

    @WorkerThread
    fun insertBookmarks(bookmarksEntity: BookmarksEntity) {
        bookmarkDao.insertBookmarks(bookmarksEntity)
    }

    fun getBookmarks(): BookmarksEntity {
        return bookmarkDao.getBookmarks()
    }

    fun listBookmarks(): List<BookmarksEntity> {
        return bookmarkDao.listBookmarks()
    }

    fun countBookmarkByDomain(domain: String): Int {
        return bookmarkDao.countBookmarkByDomain(domain)
    }

    fun countBookmarks(): Int {
        return bookmarkDao.countBookmarks()
    }

    fun deleteBookmarkByDomain(domain: String) {
        bookmarkDao.deleteBookmarkByDomain(domain)
    }

    fun deleteAllBookmarks() {
        bookmarkDao.deleteAllBookmarks()
    }
}
