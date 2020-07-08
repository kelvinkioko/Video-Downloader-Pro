package video.downloader.plus.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarksEntity(

    @PrimaryKey(autoGenerate = true) var id: Int,

    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "domain") var domain: String,
    @ColumnInfo(name = "time") var time: Long
)
