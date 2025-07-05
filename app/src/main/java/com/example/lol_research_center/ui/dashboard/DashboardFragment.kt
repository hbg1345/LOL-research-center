package com.example.lol_research_center.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lol_research_center.databinding.FragmentDashboardBinding
import com.example.lol_research_center.model.ItemDataLoader

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 3)

        val items = context?.let {
            ItemDataLoader.loadItemsFromAsset(it, "items.json")
        } ?: emptyList() // context가 null일 경우 빈 리스트 반환

        val adapter = DashboardAdapter(items)
        recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}