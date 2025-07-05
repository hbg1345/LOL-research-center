package com.example.lol_research_center.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lol_research_center.R
import com.example.lol_research_center.model.ChampionInfo
import com.example.lol_research_center.model.Lane               // enum class Lane { TOP, JUNGLE, MID, ADC, SUPPORT }

class ImageGridAdapter(
    private val source: List<ChampionInfo>,
    private val onClick: (ChampionInfo) -> Unit
) : RecyclerView.Adapter<ImageGridAdapter.VH>() {

    /* ── 동적 상태 ─────────────────────── */
    private var filtered = source.toMutableList()
    private var query: String = ""
    private var lane: Lane? = null

    /* ── ViewHolder ───────────────────── */
    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val iv: ImageView = view.findViewById(R.id.imageView)
        private val tv: TextView  = view.findViewById(R.id.tvName)
        fun bind(item: ChampionInfo) {
            iv.setImageResource(item.champDrawable)
            tv.text = item.name
            itemView.setOnClickListener { onClick(item) }
        }
    }

    /* ── 어댑터 필수 구현 ──────────────── */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(filtered[position])
    override fun getItemCount(): Int = filtered.size

    /* ── 검색어 + 라인 필터 동시 적용 ─── */
    fun updateFilter(newQuery: String? = null, newLane: Lane? = null) {
        newQuery?.let { query = it.lowercase().trim() }   // 변경된 값만 반영
        lane = newLane                                    // null = 모든 라인 허용

        filtered = source.filter { champ ->
            val matchName = champ.name.contains(query, true)
            val matchLane = lane?.let { champ.lane == it } ?: true
            matchName && matchLane
        }.toMutableList()

        notifyDataSetChanged()
    }
}
