package com.example.lol_research_center.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.lol_research_center.R

import android.widget.TextView
import com.example.lol_research_center.model.ItemData

class DashboardAdapter(private val items: List<ItemData>) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imageButton.setImageResource(item.imageResId)
        holder.textView.text = item.name
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageButton: ImageButton = itemView.findViewById(R.id.item_image_button)
        val textView: TextView = itemView.findViewById(R.id.item_text)
    }
}