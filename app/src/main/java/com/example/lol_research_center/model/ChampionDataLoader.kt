package com.example.lol_research_center.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.IOException

/*───────────────────────────*
 * 1)  JSON → DTO 선언부
 *───────────────────────────*/

/** 스킬 하나(아이콘 = 문자열) */
private data class JsonSkill(
    @SerializedName("skillDrawable") val skillDrawableName: String,
    val skillLevel: Int,
    val coolDown : List<Int>,
    val cost : List<Int>,
    val skillDamageAd: List<Int>,
    val skillDamageAp: List<Int>,
    val skillDamageFix: List<Int>,
    val skillApCoeff: Float,
    val skillAdCoeff: Float,
    val skillArCoeff: Float,
    val skillMrCoeff: Float,
    val skillHpCoeff: Float,
    val skillType: String,
    val skillInfo: String
)

/** 5개 스킬 묶음 */
private data class JsonSkills(
    val p: JsonSkill,
    val q: JsonSkill,
    val w: JsonSkill,
    val e: JsonSkill,
    val r: JsonSkill
)

/** 챔피언 전체(JSON용) */
private data class JsonChampionInfo(
    @SerializedName("champDrawable") val champDrawableName: String,
    val name: String,
    val lane: Lane,
    val stats: Stats,
    @SerializedName("itemDrawables") val itemDrawableNames: List<String>,
    val skills: JsonSkills,
    val lore: String
)

/*───────────────────────────*
 * 2)  변환 헬퍼
 *───────────────────────────*/

/** drawable 문자열 → ID 로 치환하여 Skill 생성 */
private fun JsonSkill.toSkill(ctx: Context): Skill {
    val resId = ctx.resources.getIdentifier(
        skillDrawableName, "drawable", ctx.packageName
    )
    return Skill(
        skillDrawable = resId,
        skillLevel    = skillLevel,
        skillDamageAd   = skillDamageAd,
        skillDamageAp   = skillDamageAp,
        skillDamageFix   = skillDamageFix,
        skillApCoeff  = skillApCoeff,
        skillAdCoeff  = skillAdCoeff,
        skillArCoeff  = skillArCoeff,
        skillMrCoeff  = skillMrCoeff,
        skillHpCoeff  = skillHpCoeff,
        skillType     = skillType,
        skillInfo     = skillInfo,
        coolDown = coolDown,
        cost = cost
    )
}

/*───────────────────────────*
 * 3)  ChampionDataLoader
 *───────────────────────────*/

object ChampionDataLoader {

    /**
     * @param context  호출 컨텍스트
     * @param fileName assets/ 이하 JSON (예: "champions.json")
     */
    fun loadChampionsFromAsset(
        context: Context,
        fileName: String
    ): List<ChampionInfo> {

        /* 1️⃣ assets 에서 JSON 문자열 읽기 */
        val jsonString = try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace(); return emptyList()
        }

        /* 2️⃣ 문자열 → JsonChampionInfo 리스트 파싱 */
        val listType   = object : TypeToken<List<JsonChampionInfo>>() {}.type
        val rawList: List<JsonChampionInfo> =
            Gson().fromJson(jsonString, listType)

        /* 3️⃣ drawable·스킬 매핑 후 ChampionInfo 반환 */
        return rawList.map { raw ->

            /* 챔피언 초상화 */
            val champResId = context.resources.getIdentifier(
                raw.champDrawableName, "drawable", context.packageName
            )

            /* 아이템 아이콘 */
            val itemResIds = raw.itemDrawableNames.map {
                context.resources.getIdentifier(it, "drawable", context.packageName)
            }

            /* 스킬 5개 매핑 */
            val mappedSkills = Skills(
                p = raw.skills.p.toSkill(context),
                q = raw.skills.q.toSkill(context),
                w = raw.skills.w.toSkill(context),
                e = raw.skills.e.toSkill(context),
                r = raw.skills.r.toSkill(context)
            )

            /* 최종 ChampionInfo 생성 */
            ChampionInfo(
                champDrawable = champResId,
                name          = raw.name,
                lane          = raw.lane,
                stats         = raw.stats,
                itemDrawables = itemResIds,
                skills        = mappedSkills,
                lore          = raw.lore
            )
        }
    }
}
