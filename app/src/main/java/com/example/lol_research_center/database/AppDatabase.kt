package com.example.lol_research_center.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.lol_research_center.converters.Converters
import com.example.lol_research_center.dao.BuildInfoDao
import com.example.lol_research_center.dao.TestInfoDao
import com.example.lol_research_center.model.BuildInfo
import com.example.lol_research_center.model.TestInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// 1) DummyDataProvider import 추가
import com.example.lol_research_center.database.DummyDataProvider

@Database(
    entities = [BuildInfo::class, TestInfo::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun buildInfoDao(): BuildInfoDao
    abstract fun testInfoDao(): TestInfoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lol-research-db"
                )
                    // 2) onCreate 시점에 콜백 추가
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // IO 스레드에서 더미 데이터 삽입
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getDatabase(context)
                                // 2) DAO는 항상 database 인스턴스에서 꺼냅니다
                                val buildDao = database.buildInfoDao()
                                val testDao  = database.testInfoDao()

                                // 3) 더미 데이터 삽입
                                val dummyBuild = DummyDataProvider.createDummyBuildInfo()
                                val dummyTest  = DummyDataProvider.createDummyTestInfo()
                                buildDao.insertBuildInfo(dummyBuild)
                                testDao.insertTestInfo(dummyTest)
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
