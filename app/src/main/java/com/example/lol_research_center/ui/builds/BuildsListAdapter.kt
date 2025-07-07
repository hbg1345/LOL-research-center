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
    private val onClick: (BuildInfo) -> Unit,
    private val onDeleteClick: (BuildInfo) -> Unit
) : ListAdapter<BuildInfo, BuildListAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<BuildInfo>() {
            override fun areItemsTheSame(a: BuildInfo, b: BuildInfo) =
                a.id == b.id
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
            build.champion?.let {
                champIcon.setImageResource(it.champDrawable ?: R.drawable.ic_launcher_foreground)
                champName.text = it.name ?: "챔피언 정보 없음"
            } ?: run {
                champIcon.setImageResource(R.drawable.ic_launcher_foreground)
                champName.text = "챔피언 정보 없음"
            }

            val itemViews = listOf(item1, item2, item3, item4, item5, item6)
            build.items?.let {
                itemViews.forEachIndexed { idx, img ->
                    if (idx < it.size) {
                        img.setImageResource(it[idx].imageResId ?: R.drawable.ic_launcher_foreground)
                    } else {
                        img.setImageResource(R.drawable.ic_launcher_foreground)  // 빈칸 placeholder
                    }
                }
            } ?: run {
                itemViews.forEach { it.setImageResource(R.drawable.ic_launcher_foreground) }
            }

            root.setOnClickListener { onClick(build) }
            button3.setOnClickListener { onDeleteClick(build) }
        }
    }
}
