package com.example.lol_research_center.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.example.lol_research_center.model.BuildInfo
import com.example.lol_research_center.model.TestInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildInfoDao {
    @Insert
    suspend fun insertBuildInfo(buildInfo: BuildInfo)

    @Query("SELECT * FROM build_info ORDER BY timestamp DESC")
    fun getAll(): Flow<List<BuildInfo>>

    @Query("SELECT * FROM build_info")
    suspend fun getAllBuilds(): List<BuildInfo>

    @Delete
    suspend fun delete(build: BuildInfo)

    @Query("SELECT * FROM build_info ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestBuild(): BuildInfo?

    @androidx.room.Update
    suspend fun updateBuildInfo(buildInfo: BuildInfo)
}