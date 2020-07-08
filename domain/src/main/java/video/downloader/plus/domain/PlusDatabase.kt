package video.downloader.plus.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import video.downloader.plus.domain.dao.BookmarksDao
import video.downloader.plus.domain.dao.DownloadsDao
import video.downloader.plus.domain.entity.BookmarksEntity
import video.downloader.plus.domain.entity.DownloadsEntity

@Database(entities = arrayOf(BookmarksEntity::class, DownloadsEntity::class), version = 1)
abstract class PlusDatabase : RoomDatabase() {

    abstract fun bookmarksDao(): BookmarksDao
    abstract fun downloadsDao(): DownloadsDao

    companion object {
        @Volatile
        private var INSTANCE: PlusDatabase? = null

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `trip` ADD COLUMN `number_plate` TEXT")
            }
        }

        fun getDatabase(context: Context): PlusDatabase {
            var tempInstance = INSTANCE
            if (tempInstance == null) {
                tempInstance = Room.databaseBuilder<PlusDatabase>(context.applicationContext, PlusDatabase::class.java!!, "plus_database")
                    .allowMainThreadQueries()
                    .build()
            }
            return tempInstance
        }
    }
}
