package com.example.lol_research_center.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lol_research_center.model.BuildInfo

@Dao
interface BuildInfoDao {
    @Insert
    suspend fun insertBuildInfo(buildInfo: BuildInfo)

    @Query("SELECT * FROM build_info")
    suspend fun getAllBuildInfo(): List<BuildInfo>
}