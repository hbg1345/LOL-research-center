package com.example.lol_research_center.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lol_research_center.converters.Converters
import com.example.lol_research_center.dao.BuildInfoDao
import com.example.lol_research_center.model.BuildInfo

@Database(entities = [BuildInfo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun buildInfoDao(): BuildInfoDao
}