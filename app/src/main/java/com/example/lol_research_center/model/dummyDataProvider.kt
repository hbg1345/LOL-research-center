// DummyDataProvider.kt
package com.example.lol_research_center.database

import com.example.lol_research_center.R
import com.example.lol_research_center.model.*

object DummyDataProvider {

    /** 더미 ChampionInfo 생성 */
    fun createDummyChampionInfo(): ChampionInfo {
        // 기본 스탯
        val stats = Stats(
            armorPenetration            = 0f,
            armorPenetrationPercent     = 0f,
            magicPenetration            = 0f,
            magicPenetrationPercent     = 0f,
            attackdamage                = 50,
            attackdamageperlevel        = 3f,
            ap                          = 0,
            hp                          = 500,
            mp                          = 300,
            crit                        = 0,
            attackspeed                 = 0.625f,
            attackspeedperlevel         = 2f,
            armor                       = 20,
            spellblock                  = 30,
            hpperlevel                  = 85,
            mpperlevel                  = 50,
            movespeed                   = 340,
            armorperlevel               = 3f,
            spellblockperlevel          = 5f,
            hpregen                     = 5f,
            hpregenperlevel             = 0.5f,
            mpregen                     = 6f,
            mpregenperlevel             = 0.8f,
            critperlevel                = 0f
        )

        // 더미 스킬 하나
        val dummySkill = Skill(
            skillTitle   = "DummySkill",
            skillDrawable= 0,
            skillLevel   = 1,
            skillDamageAd= listOf(0),
            skillDamageAp= listOf(0),
            skillDamageFix= listOf(0),
            coolDown     = listOf(0),
            cost         = listOf(0),
            skillApCoeff = 0f,
            skillAdCoeff = 0f,
            skillArCoeff = 0f,
            skillMrCoeff = 0f,
            skillHpCoeff = 0f,
            skillType    = "None",
            skillInfo    = "This is a dummy skill."
        )

        return ChampionInfo(
            champDrawable = R.drawable.bot,
            name          = "ahri",
            lane          = Lane.MID,
            stats         = stats,
            itemDrawables = listOf(0, 0, 0),
            skills        = Skills(
                p = dummySkill,
                q = dummySkill,
                w = dummySkill,
                e = dummySkill,
                r = dummySkill
            ),
            lore          = "A champion used only for testing.",
            level         = 1
        )
    }

    /** 더미 ItemData 리스트 생성 (최대 6개까지 자유롭게 늘리세요) */
    fun createDummyItems(): List<ItemData> {
        return listOf(
            ItemData(imageResId = R.drawable.bf_sword, name = "DummyItem1"),
            ItemData(imageResId = R.drawable.doran_blade, name = "DummyItem2")
        )
    }

    /** 더미 BuildInfo 생성 */
    fun createDummyBuildInfo(): BuildInfo {
        return BuildInfo(
            champion   = createDummyChampionInfo(),
            items      = createDummyItems(),
            calcResult = SkillDamageSet(p = 10, q = 20, w = 30, e = 40, r = 50)
        )
    }

    /** 더미 TestInfo 생성 */
    fun createDummyTestInfo(): TestInfo {
        return TestInfo(
            champion = createDummyChampionInfo(),
            items    = createDummyItems()
        )
    }
}
