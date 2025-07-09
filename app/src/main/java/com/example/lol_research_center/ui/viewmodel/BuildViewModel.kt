package com.example.lol_research_center.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.lol_research_center.database.AppDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lol_research_center.R
import com.example.lol_research_center.model.BuildInfo
import com.example.lol_research_center.model.ChampionInfo
import com.example.lol_research_center.model.ItemData
import com.example.lol_research_center.model.Lane
import com.example.lol_research_center.model.Skill
import com.example.lol_research_center.model.SkillDamageSet
import com.example.lol_research_center.model.Skills
import com.example.lol_research_center.model.Stats
import com.example.lol_research_center.model.TestInfo

class BuildViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentBuild = MutableLiveData<BuildInfo?>()
    val currentBuild: LiveData<BuildInfo?> = _currentBuild

    private val db = AppDatabase.getDatabase(application)

    init {
        viewModelScope.launch {
            val latestBuild = db.buildInfoDao().getLatestBuild()
            _currentBuild.value = latestBuild
        }
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

    fun setTestInfo(champion: ChampionInfo, items: List<ItemData>) {
        val currentTestInfoList = _currentBuild.value?.testInfoList?.toMutableList() ?: mutableListOf()
        currentTestInfoList.add(TestInfo(champion = champion, items = items))
        _currentBuild.value = _currentBuild.value?.copy(testInfoList = currentTestInfoList)
    }

    fun setCurrentBuild(buildInfo: BuildInfo) {
        _currentBuild.value = buildInfo
    }
}
