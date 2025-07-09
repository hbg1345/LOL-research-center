package com.example.lol_research_center.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lol_research_center.dao.BuildInfoDao
import com.example.lol_research_center.model.BuildInfo
import com.example.lol_research_center.model.TestInfo

@Database(
    entities = [BuildInfo::class, TestInfo::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun buildInfoDao(): BuildInfoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lol_research_center.db"
                )
                    .fallbackToDestructiveMigration()  // 스키마 변경 시 기존 DB 삭제 후 재생성
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
