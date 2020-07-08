package video.downloader.plus.domain

import android.app.Application
import android.content.Context
import video.downloader.plus.domain.dao.BookmarksDao
import video.downloader.plus.domain.dao.DownloadsDao

class PlusApp : Application() {

    private lateinit var instance: PlusApp
    private lateinit var bookmarksDao: BookmarksDao
    private lateinit var downloadsDao: DownloadsDao

    override fun onCreate() {
        super.onCreate()
        instance = this
        bookmarksDao = PlusDatabase.getDatabase(this).bookmarksDao()
        downloadsDao = PlusDatabase.getDatabase(this).downloadsDao()
    }

    @Synchronized
    fun getBookmarksDao(context: Context): BookmarksDao {
        bookmarksDao = PlusDatabase.getDatabase(context.applicationContext).bookmarksDao()
        return bookmarksDao
    }

    @Synchronized
    fun getHistoryDao(context: Context): DownloadsDao {
        downloadsDao = PlusDatabase.getDatabase(context.applicationContext).downloadsDao()
        return downloadsDao
    }
}
