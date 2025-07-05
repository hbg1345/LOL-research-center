package com.example.lol_research_center.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lol_research_center.R
import com.example.lol_research_center.databinding.FragmentDashboardBinding
import com.example.lol_research_center.model.ItemDataLoader
import com.example.lol_research_center.ui.viewmodel.BuildViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.math.ceil

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val buildViewModel: BuildViewModel by activityViewModels()
    private lateinit var selectedItemsAdapter: SelectedItemsAdapter

    // Constants for item dimensions
    private val ITEM_HEIGHT_DP = 48f
    private val ITEM_PADDING_DP = 4f
    private val COLUMNS = 3

    // Helper to convert dp to px
    private fun Float.dpToPx(context: Context): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)

    private fun adjustRecyclerViewHeight(itemCount: Int) {
        val context = context ?: return
        val guideline = binding.guidelineSelectedItemsTop
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)

        val navViewHeightPx = navView?.height ?: 0
        val totalHeightPx = binding.root.height.toFloat()

        // Handle case where totalHeightPx might be 0 to prevent division by zero
        if (totalHeightPx == 0f) {
            return // Or handle appropriately, perhaps log an error or set a default
        }

        val selectedItemsRecyclerViewContentHeightPx = if (itemCount == 0) {
            0f // When no items, the RecyclerView itself should have 0 height
        } else {
            val rows = ceil(itemCount / COLUMNS.toDouble()).toInt().coerceAtLeast(1)
            rows * (ITEM_HEIGHT_DP + 2 * ITEM_PADDING_DP).dpToPx(context)
        }

        // The target position of the guideline (from the top of the screen)
        // This is when the selectedItemsRecyclerView has its required content height
        // and its bottom is at the top of the nav bar.
        val targetGuidelinePositionPx = totalHeightPx - navViewHeightPx - selectedItemsRecyclerViewContentHeightPx
        val targetGuidePercent = targetGuidelinePositionPx / totalHeightPx

        // The absolute lowest the guideline can go (highest percentage) is when the selectedItemsRecyclerView has 0 height
        // and its bottom is at the top of the nav bar.
        // Ensure this percentage is never negative.
        val absoluteMinGuidelinePercent = (totalHeightPx - navViewHeightPx).coerceAtLeast(0f) / totalHeightPx

        val clampedGuidePercent = targetGuidePercent.coerceIn(0.0f, absoluteMinGuidelinePercent)

        val layoutParams = guideline.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.guidePercent = clampedGuidePercent
        guideline.layoutParams = layoutParams
    }

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
        selectedItemsAdapter = SelectedItemsAdapter(emptyList()) {
            buildViewModel.removeItem(it)
        }
        binding.selectedItemsRecyclerView.adapter = selectedItemsAdapter
        binding.selectedItemsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
        }

        // Adjust bottom margin of selected_items_recycler_view to be above nav_view
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.post { // Ensure navView is measured
            val layoutParams = binding.selectedItemsRecyclerView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin = navView.height
            binding.selectedItemsRecyclerView.layoutParams = layoutParams

            // Initial height adjustment after navView is measured
            adjustRecyclerViewHeight(buildViewModel.currentBuild.value?.items?.size ?: 0)
        }

        // Implement drag to resize selected items RecyclerView
        val guideline = binding.guidelineSelectedItemsTop
        binding.dragHandle.setOnTouchListener { v, event ->
            val layoutParams = guideline.layoutParams as ConstraintLayout.LayoutParams
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    val newGuideLinePos = (event.rawY - binding.root.y) / binding.root.height

                    val navViewHeightPx = activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.height ?: 0
                    val totalHeightPx = binding.root.height.toFloat()

                    // Handle case where totalHeightPx might be 0 to prevent division by zero
                    if (totalHeightPx == 0f) {
                        return@setOnTouchListener true // Or handle appropriately
                    }

                    // The absolute lowest the guideline can go (highest percentage) is when the selectedItemsRecyclerView has 0 height
                    // and its bottom is at the top of the nav bar.
                    // Ensure this percentage is never negative.
                    val absoluteMinGuidelinePercent = (totalHeightPx - navViewHeightPx).coerceAtLeast(0f) / totalHeightPx

                    val clampedGuidePercent = newGuideLinePos.coerceIn(0.0f, absoluteMinGuidelinePercent)
                    layoutParams.guidePercent = clampedGuidePercent
                    guideline.layoutParams = layoutParams
                }
            }
            true
        }

        // Observe changes in selected items from BuildViewModel
        buildViewModel.currentBuild.observe(viewLifecycleOwner) { buildInfo ->
            buildInfo?.items?.let {
                selectedItemsAdapter.updateItems(it)
                adjustRecyclerViewHeight(it.size)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

