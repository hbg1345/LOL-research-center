package com.example.lol_research_center.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lol_research_center.R
import com.example.lol_research_center.model.ChampionInfo   // ← 모델 import

class ImageGridAdapter(
    private val items: List<ChampionInfo>,                 // Int → ImageItem
    private val onClick: (ChampionInfo) -> Unit            // 클릭 콜백 전달
) : RecyclerView.Adapter<ImageGridAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val iv: ImageView = view.findViewById(R.id.imageView)
        private val tv  = view.findViewById<TextView>(R.id.tvName)

        fun bind(item: ChampionInfo) {
            iv.setImageResource(item.champDrawable)     // 모델 속성 사용
            tv.text = item.name
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])                    // 간결화

    override fun getItemCount(): Int = items.size
}
