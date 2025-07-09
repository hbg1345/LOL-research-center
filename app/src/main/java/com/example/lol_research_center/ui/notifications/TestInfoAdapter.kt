package com.example.lol_research_center.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lol_research_center.R
import com.example.lol_research_center.model.TestInfo

import com.example.lol_research_center.model.ChampionInfo
import com.example.lol_research_center.model.ItemData
import com.example.lol_research_center.model.Stats

class TestInfoAdapter(
    private var data: List<TestInfo>,
    private val calculateStats: (ChampionInfo, List<ItemData>, Int) -> Stats
) : RecyclerView.Adapter<TestInfoAdapter.TestViewHolder>() {

    fun updateData(newData: List<TestInfo>) {
        data = newData
        notifyDataSetChanged()
    }

    inner class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val champImg: ImageView = itemView.findViewById(R.id.cardChampImg)
        val itemsGrid: GridLayout = itemView.findViewById(R.id.cardItemsGrid)
        val statArmor: TextView = itemView.findViewById(R.id.statArmorText)
        val champLevel: TextView = itemView.findViewById(R.id.LevelText)
        val statHp: TextView = itemView.findViewById(R.id.statHpText)
        val statMr: TextView = itemView.findViewById(R.id.statMrText)
        val closeBtn: ImageButton = itemView.findViewById(R.id.closeButton)
        val text4: TextView = itemView.findViewById(R.id.textView4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_target_champ_frame, parent, false)
        return TestViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val info = data[position]

        // 챔피언 이미지
        holder.champImg.setImageResource(info.champion.champDrawable)

        // 아이템 6개
        for (i in 0 until holder.itemsGrid.childCount) {
            val iv = holder.itemsGrid.getChildAt(i) as ImageView
            info.items.getOrNull(i)?.let { item ->
                iv.setImageResource(item.imageResId)
            } ?: run {
                iv.setImageDrawable(null)
            }
        }

        // 스탯 5개
        val calculatedStats = calculateStats(info.champion, info.items, info.champion.level)
        holder.champLevel.text = "Lv. ${info.champion.level}"
        holder.statArmor.text = calculatedStats.armor.toString()
        holder.statHp.text = calculatedStats.hp.toString()
        holder.statMr.text = calculatedStats.spellblock.toString()
        // textView4: 예시로 TestInfo id 표시
        holder.text4.text = info.id.toString()

        // 닫기 버튼 (추가 기능 구현 가능)
        holder.closeBtn.setOnClickListener {
            // TODO: 항목 삭제 등 원하는 동작 구현
        }
    }
}
