package com.example.lol_research_center.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lol_research_center.R
import com.example.lol_research_center.model.BuildInfo
import com.example.lol_research_center.model.ChampionInfo
import com.example.lol_research_center.model.ItemData
import com.example.lol_research_center.model.Lane
import com.example.lol_research_center.model.Skill
import com.example.lol_research_center.model.SkillDamageSet
import com.example.lol_research_center.model.Skills
import com.example.lol_research_center.model.Stats

class BuildViewModel : ViewModel() {

    private val _currentBuild = MutableLiveData<BuildInfo?>()
    val currentBuild: LiveData<BuildInfo?> = _currentBuild

    init {
        val defaultSkill = Skill(skillTitle = "default skill name", skillDrawable = R.drawable.leesin_p, skillLevel = 1, skillDamageAd = listOf(0,0,0,0,0),skillDamageAp = listOf(0,0,0,0,0),skillDamageFix = listOf(0,0,0,0,0), coolDown = listOf(10,10,10,10,10), cost = listOf(20,20,20,20,20), 0f,0f,0f,0f,0f,"Passive", skillInfo = "skill q info")
        val asheSkills = Skills(defaultSkill, defaultSkill, defaultSkill, defaultSkill, defaultSkill)
        val asheStats = Stats(
            attackdamage = 68,
            attackdamageperlevel = 3.5f,
            ap = 0,
            hp = 575,
            mp = 200,
            crit = 0,
            attackspeed = 0.651f,
            attackspeedperlevel = 3.0f,
            armor = 36,
            spellblock = 32,
            hpperlevel = 100,
            mpperlevel = 0,
            movespeed = 345,
            armorperlevel = 4.0f,
            spellblockperlevel = 1.5f,
            hpregen = 7.5f,
            hpregenperlevel = 0.7f,
            mpregen = 50f,
            mpregenperlevel = 0f,
            critperlevel = 0f,
            armorPenetration = 0f,
            armorPenetrationPercent = 0.5f,
            magicPenetration = 0f,
            magicPenetrationPercent = 0.5f
        )
        val asheChampionInfo = ChampionInfo(
            champDrawable = 0, // 실제 Drawable ID로 대체 필요
            name = "Ashe",
            lane = Lane.ADC,
            stats = asheStats,
            itemDrawables = emptyList(),
            skills = asheSkills,
            lore = "챔피언 역사에 대한ㅇㅇㅇ 재미난 이야기"
        )
        _currentBuild.value = BuildInfo(
            champion = asheChampionInfo,
            items = emptyList(),
            calcResult = SkillDamageSet(0, 0, 0, 0, 0)
        )
    }

    fun setChampion(champion: ChampionInfo) {
        _currentBuild.value = _currentBuild.value?.copy(champion = champion, items = emptyList())
    }

    fun addItem(item: ItemData) {
        val currentItems = _currentBuild.value?.items ?: emptyList()
        if (currentItems.size < 6) {
            _currentBuild.value = _currentBuild.value?.copy(items = currentItems + item)
        }
    }

    fun removeItem(item: ItemData) {
        val currentItems = _currentBuild.value?.items ?: emptyList()
        _currentBuild.value = _currentBuild.value?.copy(items = currentItems - item)
    }
}
