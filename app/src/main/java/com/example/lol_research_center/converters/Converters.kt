package com.example.lol_research_center.converters

import androidx.room.TypeConverter
import com.example.lol_research_center.model.ChampionInfo
import com.example.lol_research_center.model.ItemData
import com.example.lol_research_center.model.SkillDamageSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromChampionInfo(championInfo: ChampionInfo): String {
        return Gson().toJson(championInfo)
    }

    @TypeConverter
    fun toChampionInfo(json: String): ChampionInfo {
        return Gson().fromJson(json, ChampionInfo::class.java)
    }

    @TypeConverter
    fun fromItemList(itemList: List<ItemData>): String {
        return Gson().toJson(itemList)
    }

    @TypeConverter
    fun toItemList(json: String): List<ItemData> {
        val type = object : TypeToken<List<ItemData>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromSkillDamageSet(skillDamageSet: SkillDamageSet): String {
        return Gson().toJson(skillDamageSet)
    }

    @TypeConverter
    fun toSkillDamageSet(json: String): SkillDamageSet {
        return Gson().fromJson(json, SkillDamageSet::class.java)
    }
}