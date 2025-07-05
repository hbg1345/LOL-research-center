// ui/builds/BuildListAdapter.kt
package com.example.lol_research_center.ui.builds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.lol_research_center.R
import com.example.lol_research_center.databinding.RowBuildBinding
import com.example.lol_research_center.model.BuildInfo

class BuildListAdapter(
    private val onClick: (BuildInfo) -> Unit
) : ListAdapter<BuildInfo, BuildListAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<BuildInfo>() {
            override fun areItemsTheSame(a: BuildInfo, b: BuildInfo) =
                a.champion.name == b.champion.name && a.items == b.items   // 필요 시 UUID 사용
            override fun areContentsTheSame(a: BuildInfo, b: BuildInfo) = a == b
        }
    }

    inner class VH(val vb: RowBuildBinding) : RecyclerView.ViewHolder(vb.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vb = RowBuildBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(vb)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val build = getItem(position)
        with(holder.vb) {
            champIcon.setImageResource(build.champion.champDrawable)
            champName.text = build.champion.name

            /* 아이템 6칸 (ImageView 6개) */
            val itemViews = listOf(item1, item2, item3, item4, item5, item6)
            itemViews.forEachIndexed { idx, img ->
                if (idx < build.items.size) {
                    img.setImageResource(build.items[idx].imageResId)
                } else {
                    img.setImageResource(R.drawable.ashe)  // 빈칸 placeholder
                }
            }

            /* 스킬 총합/미리보기 넣고 싶다면 여기에 */

            root.setOnClickListener { onClick(build) }
        }
    }
}
