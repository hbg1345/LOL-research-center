package com.example.lol_research_center.model

import android.content.Context
import com.google.gson.Gson
import android.util.Log
import com.google.gson.reflect.TypeToken

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
            // Add this line to print the stats
            println("Item Stats for ${itemData.name}: ${itemData.stats}")
            itemData.copy(imageResId = imageResId)
        }
    }
}