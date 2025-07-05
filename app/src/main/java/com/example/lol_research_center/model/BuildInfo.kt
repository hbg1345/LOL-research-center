// model/BuildInfo.kt
package com.example.lol_research_center.model
import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BuildInfo(
    val champion: ChampionInfo,
    val items: List<ItemData>,        // 최대 6개
    val calcResult: SkillDamageSet    // 스킬별 데미지 계산 결과 (아래 참조)
) : Parcelable

@Parcelize
data class SkillDamageSet(
    val p: Int, val q: Int, val w: Int, val e: Int, val r: Int
) : Parcelable
