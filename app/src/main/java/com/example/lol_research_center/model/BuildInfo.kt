// model/BuildInfo.kt
package com.example.lol_research_center.model
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lol_research_center.database.DummyDataProvider.createDummyTestInfo
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "build_info")
data class BuildInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val champion: ChampionInfo,
    val items: List<ItemData>,        // 최대 6개
    val calcResult: SkillDamageSet,    // 스킬별 데미지 계산 결과 (아래 참조)
    val timestamp: Long = System.currentTimeMillis(), // 빌드 저장 시간
    val testInfoList: List<TestInfo> = listOf(
        createDummyTestInfo(),
    )
) : Parcelable

@Parcelize
@Entity(tableName = "test_info")
data class TestInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val champion: ChampionInfo,
    val items: List<ItemData>,
    val timestamp: Long = System.currentTimeMillis(), // 빌드 저장 시간
) : Parcelable

@Parcelize
data class SkillDamageSet(
    val p: Int, val q: Int, val w: Int, val e: Int, val r: Int
) : Parcelable
