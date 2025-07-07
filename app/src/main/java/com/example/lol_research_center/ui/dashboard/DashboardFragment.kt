package com.example.lol_research_center.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lol_research_center.R
import com.example.lol_research_center.databinding.FragmentDashboardBinding
import com.example.lol_research_center.model.ItemDataLoader
import com.example.lol_research_center.ui.viewmodel.BuildViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val buildViewModel: BuildViewModel by activityViewModels()
    private lateinit var selectedItemsAdapter: SelectedItemsAdapter

    // Constants for item dimensions
    

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        val items = context?.let {
            ItemDataLoader.loadItemsFromAsset(it, "items.json")
        } ?: emptyList()

        val pickerMode = arguments?.getBoolean("pickerMode") ?: false

        val adapter = DashboardAdapter(items) {
            if (pickerMode) {
                if ((buildViewModel.currentBuild.value?.items?.size ?: 0) < 6) {
                    buildViewModel.addItem(it)
                } else {
                    Toast.makeText(requireContext(), "아이템은 최대 6개까지 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        recyclerView.adapter = adapter

        // Setup for selected items RecyclerView
        selectedItemsAdapter = SelectedItemsAdapter() {
            buildViewModel.removeItem(it)
        }
        binding.selectedItemsRecyclerView.adapter = selectedItemsAdapter

        if (pickerMode) {
            binding.selectedItemsRecyclerView.visibility = View.VISIBLE
        } else {
            binding.selectedItemsRecyclerView.visibility = View.GONE
        }

        // Observe changes in selected items from BuildViewModel
        buildViewModel.currentBuild.observe(viewLifecycleOwner) { buildInfo ->
            buildInfo?.items?.let {
                selectedItemsAdapter.updateItems(it)
            }
        }

        binding.nextButton.setOnClickListener {
            Toast.makeText(requireContext(), "다음 화면으로 이동", Toast.LENGTH_SHORT).show()
            // TODO: Implement navigation to the next screen
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

