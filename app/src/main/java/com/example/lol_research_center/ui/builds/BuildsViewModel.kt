// ui/builds/BuildsViewModel.kt
package com.example.lol_research_center.ui.builds

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.lol_research_center.database.AppDatabase
import com.example.lol_research_center.model.BuildInfo

class BuildsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val builds: LiveData<List<BuildInfo>> = db.buildInfoDao().getAll().asLiveData()
}
