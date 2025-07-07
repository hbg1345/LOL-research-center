package com.example.lol_research_center.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import android.util.Log
import com.google.gson.reflect.TypeToken
import java.io.IOException

// 1) JSON 파싱용 DTO ─ drawable 이름(문자열)을 받기 위한 래퍼
private data class JsonChampionInfo(
    @SerializedName("champDrawable") val champDrawableName: String,
    val name: String,
    val lane: Lane,
    val stats: Stats,
    @SerializedName("itemDrawables") val itemDrawableNames: List<String>,
    val skills: Skills
)

object ChampionDataLoader {

    /**
     * @param context  호출한 컨텍스트 (보통 Activity·Fragment·Application)
     * @param fileName assets/ 이하의 JSON 파일명 (예: "champions.json")
     * @return 파싱 및 매핑을 마친 List<ChampionInfo>
     */
    fun loadChampionsFromAsset(
        context: Context,
        fileName: String
    ): List<ChampionInfo> {

        // 1️⃣ JSON 읽기
        val jsonString = try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }

        // 2️⃣ JSON → DTO(문자열) 변환
        val listType = object : TypeToken<List<JsonChampionInfo>>() {}.type
        val rawList: List<JsonChampionInfo> = Gson().fromJson(jsonString, listType)

        // 3️⃣ drawable 이름 → 실제 리소스 ID 매핑,
        //     itemDrawables 도 동일하게 매핑
        return rawList.map { raw ->
            val champResId = context.resources.getIdentifier(
                raw.champDrawableName,
                "drawable",
                context.packageName
            )

            val itemResIds = raw.itemDrawableNames.map { drawableName ->
                context.resources.getIdentifier(drawableName, "drawable", context.packageName)
            }

            ChampionInfo(
                champDrawable = champResId,
                name = raw.name,
                lane = raw.lane,
                stats = raw.stats,
                itemDrawables = itemResIds,
                skills = raw.skills
            )
        }
    }
}

// item.json의 최상위 구조를 위한 데이터 클래스
data class ItemJsonRoot(
    val data: Map<String, ItemData>
)

object ItemDataLoader {
    fun loadItemsFromAsset(context: Context, fileName: String): List<ItemData> {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: java.io.IOException) {
            ioException.printStackTrace()
            return emptyList()
        }

        // item.json의 최상위 구조에 맞게 ItemJsonRoot를 사용
        val rootType = object : TypeToken<ItemJsonRoot>() {}.type
        val itemRoot: ItemJsonRoot = Gson().fromJson(jsonString, rootType)

        return itemRoot.data.values.map { itemData ->
            val rawImageName = itemData.image?.full?.removeSuffix(".png") ?: ""
            val resourceName = "a" + rawImageName
            val imageResId = context.resources.getIdentifier(
                resourceName,
                "drawable",
                context.packageName
            )
            Log.d("ItemDataLoader", "Processing item: ${itemData.name}, rawImageName: $rawImageName, resourceName: $resourceName, imageResId: $imageResId")
            itemData.copy(imageResId = imageResId)
        }
    }
}