package com.example.lol_research_center.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.lol_research_center.model.TestInfo
import kotlinx.coroutines.flow.Flow


@Dao
interface TestInfoDao {
    @Insert
    suspend fun insertTestInfo(testInfo: TestInfo)

    @Query("SELECT * FROM test_info ORDER BY id DESC")
    fun getAllTestInfos(): Flow<List<TestInfo>>

    @Delete
    suspend fun deleteTestInfo(testInfo: TestInfo)
}