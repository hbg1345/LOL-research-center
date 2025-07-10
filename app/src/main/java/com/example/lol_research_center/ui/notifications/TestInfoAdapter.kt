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
import com.example.lol_research_center.model.Skill

class TestInfoAdapter(
    private var data: List<TestInfo>,
    private val calculateStats: (ChampionInfo, List<ItemData>, Int) -> Stats,
    private val calculatePhysicalDamage: (Int, Int, Stats) -> Float,
    private val calculateMagicDamage: (Int, Int, Stats) -> Float,
    private var selectedSkill: Skill?, // Add selectedSkill parameter
    private val onItemRemove: (TestInfo) -> Unit,
    private val onItemSelected: (TestInfo) -> Unit,
    private var attackerStats: Stats // 추가: 공격 챔피언의 스탯
) : RecyclerView.Adapter<TestInfoAdapter.TestViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    fun updateData(newData: List<TestInfo>) {
        data = newData
        notifyDataSetChanged()
    }

    fun updateSelectedSkill(newSkill: Skill?) {
        selectedSkill = newSkill
        notifyDataSetChanged()
    }

    fun updateAttackerStats(newAttackerStats: Stats) {
        this.attackerStats = newAttackerStats
        notifyDataSetChanged() // Refresh to reflect new attacker stats in damage calculations
    }

    inner class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val champImg: ImageView = itemView.findViewById(R.id.cardChampImg)
        val itemsGrid: GridLayout = itemView.findViewById(R.id.cardItemsGrid)
        val statArmor: TextView = itemView.findViewById(R.id.statArmorText)
        val champLevel: TextView = itemView.findViewById(R.id.LevelText)
        val statHp: TextView = itemView.findViewById(R.id.statHpText)
        val statMr: TextView = itemView.findViewById(R.id.statMrText)
        val closeBtn: ImageButton = itemView.findViewById(R.id.closeButton)
        val hitDamage: TextView = itemView.findViewById(R.id.hit)
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
        // 데미지 계산 및 표시 (현재 선택된 스킬 사용)
        selectedSkill?.let { skill ->
            // calcDamageByType에 공격 챔피언의 스탯과 대상 챔피언의 방어 스탯을 전달
            val damage = calcDamageByType(skill, attackerStats, calculatedStats.armor, calculatedStats.spellblock)
            holder.hitDamage.text = damage.toString()
        } ?: run {
            holder.hitDamage.text = "N/A" // No skill selected
        }

        // 닫기 버튼 (추가 기능 구현 가능)
        holder.closeBtn.setOnClickListener {
            onItemRemove(info)
        }

        holder.itemView.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                if (currentPosition == selectedPosition) {
                    // If the clicked item is already selected, deselect it
                    data[currentPosition].clicked = false
                    selectedPosition = RecyclerView.NO_POSITION
                    notifyItemChanged(currentPosition)
                } else {
                    // Deselect previously selected item if any
                    if (selectedPosition != RecyclerView.NO_POSITION) {
                        data[selectedPosition].clicked = false
                        notifyItemChanged(selectedPosition)
                    }
                    // Select the new item
                    data[currentPosition].clicked = true
                    selectedPosition = currentPosition
                    notifyItemChanged(currentPosition)
                    onItemSelected(data[currentPosition])
                }
            }
        }

        // Set initial background based on clicked state
        if (info.clicked) {
            holder.itemView.setBackgroundResource(R.drawable.gold_outline)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.lol_textpanel_bg)
        }
    }

    /**
     * 스킬 데미지 계산
     * base: skillDamageX[level - 1]
     * bonus: stats * coeff
     */
    private fun calcDamage(skill: Skill, attackerStats: Stats): Int { // attackerStats 매개변수 추가
        val lvl = skill.skillLevel
        if (lvl <= 0) return 0
        val baseList = when (skill.skillType) {
            "ad" -> skill.skillDamageAd
            "ap" -> skill.skillDamageAp
            "fix" -> skill.skillDamageFix
            else -> emptyList()
        }
        val base = baseList.getOrNull(lvl - 1) ?: 0
        // 여기서 attackerStats를 사용
        val bonus = attackerStats.attackdamage * skill.skillAdCoeff +
                attackerStats.ap * skill.skillApCoeff +
                attackerStats.armor * skill.skillArCoeff +
                attackerStats.spellblock * skill.skillMrCoeff +
                attackerStats.hp * skill.skillHpCoeff
        return (base + bonus).toInt()
    }

    private fun calcDamageByType(skill: Skill, attackerStats: Stats, targetArmor: Int, targetMR: Int): Int{
        var damage = 0
        if(skill.skillType == "fix"){
            damage = calcDamage(skill, attackerStats) // attackerStats 전달
        }
        else if(skill.skillType == "ad"){
            damage = calculatePhysicalDamage(calcDamage(skill, attackerStats), targetArmor, attackerStats).toInt() // attackerStats 전달
        }
        else if(skill.skillType == "ap"){
            damage = calculateMagicDamage(calcDamage(skill,attackerStats), targetMR, attackerStats).toInt() // attackerStats 전달
        }
        return damage
    }
}
