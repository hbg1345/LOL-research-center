// database/Converters.kt
package com.example.lol_research_center.database

import androidx.room.TypeConverter
import com.example.lol_research_center.database.DummyDataProvider.createDummyTestInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.lol_research_center.model.*

class Converters {
    private val gson = Gson()

    // BuildInfo.champion: ChampionInfo ↔ JSON
    @TypeConverter
    fun fromChampionInfo(champion: ChampionInfo): String =
        gson.toJson(champion)

    @TypeConverter
    fun toChampionInfo(json: String): ChampionInfo =
        gson.fromJson(json, ChampionInfo::class.java)

    // BuildInfo.items: List<ItemData> ↔ JSON
    @TypeConverter
    fun fromItemDataList(list: List<ItemData>): String =
        gson.toJson(list)

    @TypeConverter
    fun toItemDataList(json: String): List<ItemData> {
        val type = object : TypeToken<List<ItemData>>() {}.type
        return gson.fromJson(json, type)
    }

    // BuildInfo.calcResult: SkillDamageSet ↔ JSON
    @TypeConverter
    fun fromSkillDamageSet(set: SkillDamageSet): String =
        gson.toJson(set)

    @TypeConverter
    fun toSkillDamageSet(json: String): SkillDamageSet =
        gson.fromJson(json, SkillDamageSet::class.java)

    // **여기에 추가** TestInfo 리스트 ↔ JSON
    @TypeConverter
    fun fromTestInfoList(list: List<TestInfo>): String =
        gson.toJson(list)

    @TypeConverter
    fun toTestInfoList(json: String?): List<TestInfo> {
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            val type = object : TypeToken<List<TestInfo>>() {}.type
            gson.fromJson(json, type)
        }
    }
}
