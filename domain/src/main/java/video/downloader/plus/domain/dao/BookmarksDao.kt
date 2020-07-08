package video.downloader.plus.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import video.downloader.plus.domain.entity.BookmarksEntity

@Dao
interface BookmarksDao {

    @Insert
    fun insertBookmarks(vararg bookmarksEntity: BookmarksEntity)

    @Query("SELECT * FROM bookmarks ORDER BY id DESC")
    fun getBookmarks(): BookmarksEntity

    @Query("SELECT * FROM bookmarks ORDER BY id DESC")
    fun listBookmarks(): List<BookmarksEntity>

    @Query("SELECT COUNT(id) FROM bookmarks WHERE domain =:domain")
    fun countBookmarkByDomain(domain: String): Int

    @Query("SELECT COUNT(id) FROM bookmarks")
    fun countBookmarks(): Int

    @Query("DELETE FROM bookmarks WHERE domain =:domain")
    fun deleteBookmarkByDomain(domain: String)

    @Query("DELETE FROM bookmarks")
    fun deleteAllBookmarks()
}
