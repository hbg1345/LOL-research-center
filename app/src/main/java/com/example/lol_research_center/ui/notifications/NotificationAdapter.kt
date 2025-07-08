package com.example.lol_research_center.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lol_research_center.R
import com.example.lol_research_center.model.BuildInfo

class ChampFrameAdapter(private val items: List<BuildInfo>) :
    RecyclerView.Adapter<ChampFrameAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // findViewById로 필요한 뷰 바인딩
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_target_champ_frame, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val champ = items[position].champion
        // holder.itemView.findViewById<…>(…).apply { … } 등으로 데이터 바인딩
    }

    override fun getItemCount() = items.size
}
