// ui/builds/BuildsViewModel.kt
package com.example.lol_research_center.ui.builds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lol_research_center.model.BuildInfo

class BuildsViewModel : ViewModel() {

    private val _builds = MutableLiveData<List<BuildInfo>>(emptyList())
    val builds: LiveData<List<BuildInfo>> = _builds

    /** (+) 버튼 흐름이 끝나면 이 함수로 새 빌드를 등록 */
    fun addBuild(newBuild: BuildInfo) {
        _builds.value = _builds.value!! + newBuild
    }

    /** 필요 시 삭제·편집 함수도 추가 */
}
