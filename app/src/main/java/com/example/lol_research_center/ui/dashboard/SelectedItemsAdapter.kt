package com.example.lol_research_center.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.lol_research_center.R
import com.example.lol_research_center.model.ItemData

class SelectedItemsAdapter(private val onItemClick: (ItemData) -> Unit) : RecyclerView.Adapter<SelectedItemsAdapter.ViewHolder>() {

    private val items: MutableList<ItemData?> = MutableList(6) { null }

    fun updateItems(newItems: List<ItemData>) {
        items.clear()
        items.addAll(newItems)
        while (items.size < 6) {
            items.add(null)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_selected_grid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (item != null) {
            holder.imageView.setImageResource(item.imageResId)
            holder.itemView.setOnClickListener {
                onItemClick(item)
            }
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background) // Empty slot image
            holder.itemView.setOnClickListener(null) // No click listener for empty slots
        }
    }

    override fun getItemCount(): Int {
        return 6
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.selected_item_image)
    }
}
