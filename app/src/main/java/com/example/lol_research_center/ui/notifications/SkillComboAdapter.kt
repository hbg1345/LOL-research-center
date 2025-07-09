package com.example.lol_research_center.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lol_research_center.R
import com.example.lol_research_center.model.SkillCombo

class SkillComboAdapter(
    var items: MutableList<SkillCombo>
) : RecyclerView.Adapter<SkillComboAdapter.SkillComboViewHolder>() {
    fun addCombo(combo: SkillCombo) {
        (items as MutableList).add(combo)
        notifyItemInserted(items.size - 1)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillComboViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.skill_combo_ui, parent, false)
        return SkillComboViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillComboViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class SkillComboViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvComboName: TextView = itemView.findViewById(R.id.tvComboName)
        private val tvDamageTextView: TextView = itemView.findViewById(R.id.tvDamageTextView)
        private val tvComboDescription: TextView = itemView.findViewById(R.id.tvComboDescription)

        // 1~10번 스킬 아이콘 & 키를 각각 배열로 관리
        private val skillImgViews: List<View> = listOf(
            itemView.findViewById(R.id.skillImg1),
            itemView.findViewById(R.id.skillImg2),
            itemView.findViewById(R.id.skillImg3),
            itemView.findViewById(R.id.skillImg4),
            itemView.findViewById(R.id.skillImg5),
            itemView.findViewById(R.id.skillImg6),
            itemView.findViewById(R.id.skillImg7),
            itemView.findViewById(R.id.skillImg8),
            itemView.findViewById(R.id.skillImg9),
            itemView.findViewById(R.id.skillImg10)
        )

        fun bind(combo: SkillCombo) {
            tvComboName.text = combo.name
            tvDamageTextView.text = combo.damage
            tvComboDescription.text = combo.description

            // 10개 스킬 아이콘 및 키 셋업
            for (i in 0 until 10) {
                val skillView = skillImgViews[i]
                // view_skill_icon_qwer의 ImageView, TextView id가 imgSkillIcon, tvSkillKey라고 가정
                val icon = skillView.findViewById<ImageView>(R.id.imgSkill)
                val key = skillView.findViewById<TextView>(R.id.tvSkillKey)
                icon.setImageResource(combo.skillDrawables.getOrNull(i) ?: R.drawable.empty_icon)
                key.text = combo.skillKeys.getOrNull(i) ?: ""
            }
        }
    }
}
