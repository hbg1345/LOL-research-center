package com.example.lol_research_center.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.lol_research_center.R
import com.example.lol_research_center.databinding.FragmentHomeBottomSheetBinding
import com.example.lol_research_center.model.ChampionDataLoader
import com.example.lol_research_center.model.ChampionInfo
import com.example.lol_research_center.model.ItemData
import com.example.lol_research_center.model.ItemDataLoader
import com.example.lol_research_center.model.Lane
import com.example.lol_research_center.ui.dashboard.DashboardAdapter
import com.example.lol_research_center.ui.dashboard.SelectedItemsAdapter
import com.example.lol_research_center.ui.viewmodel.BuildViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.google.android.material.bottomsheet.BottomSheetBehavior
import android.app.Dialog
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isHideable = true // 아래로 스와이프하여 숨길 수 있도록 허용

                Log.d("HomeBottomSheetFragment", "BottomSheetBehavior state: ${behavior.state}")
            }
        }
        return dialog
    }

    private var _binding: FragmentHomeBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val buildViewModel: BuildViewModel by activityViewModels()

    private val champs: List<ChampionInfo> by lazy {
        ChampionDataLoader.loadChampionsFromAsset(requireContext(), "champions.json")
    }

    private var originalItems: List<ItemData> = emptyList()
    private var _selectedChampionForConfirmation: ChampionInfo? = null
    private val _selectedItems: MutableList<ItemData> = mutableListOf()

    private lateinit var itemDashboardAdapter: DashboardAdapter
    private lateinit var selectedItemsAdapter: SelectedItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val pickerMode = arguments?.getBoolean("pickerMode") ?: false
        Log.d("HomeBottomSheetFragment", "pickerMode: $pickerMode")
        Log.d("HomeBottomSheetFragment", "champs size: ${champs.size}")

        context?.let {
            originalItems = ItemDataLoader.loadItemsFromAsset(it, "item_unique_by_id.json")
        }

        val championAdapter = ImageGridAdapter(champs) { champ ->
            _selectedChampionForConfirmation = champ
            binding.confirmButton.visibility = View.VISIBLE
        }

        binding.championRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.championRecyclerView.adapter = championAdapter

        binding.confirmButton.setOnClickListener {
            _selectedChampionForConfirmation?.let { selectedChamp ->
                
                // Hide champion selection UI
                binding.championRecyclerView.visibility = View.GONE
                binding.searchView.visibility = View.GONE
                binding.laneGroup.visibility = View.GONE
                binding.confirmButton.visibility = View.GONE

                // Show item selection UI
                binding.itemSelectionLayout.root.visibility = View.VISIBLE

                // Force bottom sheet to re-expand
                val bottomSheetDialog = dialog as? BottomSheetDialog
                val bottomSheet = bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.let { BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED }

                // Setup item RecyclerView
                itemDashboardAdapter = DashboardAdapter(originalItems) { item ->
                    if (_selectedItems.size < 6) {
                        _selectedItems.add(item)
                        selectedItemsAdapter.updateItems(_selectedItems)
                    } else {
                        Toast.makeText(requireContext(), "아이템은 최대 6개까지 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.itemSelectionLayout.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
                binding.itemSelectionLayout.recyclerView.adapter = itemDashboardAdapter

                selectedItemsAdapter = SelectedItemsAdapter() {
                    _selectedItems.remove(it)
                    selectedItemsAdapter.updateItems(_selectedItems)
                }
                binding.itemSelectionLayout.selectedItemsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
                binding.itemSelectionLayout.selectedItemsRecyclerView.adapter = selectedItemsAdapter

                // Initialize selected items display
                selectedItemsAdapter.updateItems(_selectedItems)

                // Item search functionality
                binding.itemSelectionLayout.searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        filterItems(s.toString())
                    }
                    override fun afterTextChanged(s: Editable?) {}
                })

                binding.itemSelectionLayout.searchButton.setOnClickListener {
                    filterItems(binding.itemSelectionLayout.searchEditText.text.toString())
                }
            }
        }

        binding.itemSelectionLayout.nextButton.setOnClickListener {
            _selectedChampionForConfirmation?.let { selectedChamp ->
                buildViewModel.setTestInfo(selectedChamp, _selectedItems)
                dismiss()
            }
        }

        binding.championRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    binding.confirmButton.visibility = View.GONE
                    _selectedChampionForConfirmation = null
                }
            }
        })

        binding.root.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val rect = android.graphics.Rect()
                binding.championRecyclerView.getHitRect(rect)
                if (!rect.contains(v.left + event.x.toInt(), v.top + event.y.toInt())) {
                    binding.confirmButton.visibility = View.GONE
                    _selectedChampionForConfirmation = null
                }
            }
            false
        }

        /* SearchView */
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(text: String?): Boolean {
                championAdapter.updateFilter(newQuery = text)
                return true
            }
            override fun onQueryTextSubmit(q: String?) = false
        })
        binding.btnTop.setOnClickListener     { championAdapter.updateFilter(newLane = Lane.TOP) }
        binding.btnJungle.setOnClickListener  { championAdapter.updateFilter(newLane = Lane.JUNGLE) }
        binding.btnMid.setOnClickListener     { championAdapter.updateFilter(newLane = Lane.MID) }
        binding.btnAdc.setOnClickListener     { championAdapter.updateFilter(newLane = Lane.ADC) }
        binding.btnSupport.setOnClickListener { championAdapter.updateFilter(newLane = Lane.SUPPORT) }
        binding.btnFill.setOnClickListener { championAdapter.updateFilter(newLane = null )}
    }

    private fun filterItems(query: String) {
        val filteredList = if (query.isBlank()) {
            originalItems
        } else {
            originalItems.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.description?.contains(query, ignoreCase = true) == true
            }
        }
        itemDashboardAdapter.updateItems(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
