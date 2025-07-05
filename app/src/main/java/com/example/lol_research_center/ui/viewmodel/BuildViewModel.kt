package com.example.lol_research_center.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
        val defaultSkill = Skill(0, emptyList(), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, "")
        val asheSkills = Skills(defaultSkill, defaultSkill, defaultSkill, defaultSkill, defaultSkill)
        val asheStats = Stats(0, 0, 0, 0, 0, 0, 0, 0) // 임시 스탯
        val asheChampionInfo = ChampionInfo(
            champDrawable = 0, // 실제 Drawable ID로 대체 필요
            name = "Ashe",
            lane = Lane.ADC,
            stats = asheStats,
            itemDrawables = emptyList(),
            skills = asheSkills
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
