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
import com.example.lol_research_center.R
import com.example.lol_research_center.databinding.FragmentHomeBinding
import com.example.lol_research_center.databinding.FragmentHomeBottomSheetBinding
import com.example.lol_research_center.model.ChampionDataLoader
import com.example.lol_research_center.model.ChampionInfo
import com.example.lol_research_center.model.Lane
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

    private var _selectedChampionForConfirmation: ChampionInfo? = null

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

        

        val adapter = ImageGridAdapter(champs) { champ ->
            // 바텀 시트에서는 항상 pickerMode라고 가정
            _selectedChampionForConfirmation = champ
            binding.confirmButton.visibility = View.VISIBLE
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.adapter = adapter

        binding.confirmButton.setOnClickListener {
            _selectedChampionForConfirmation?.let { selectedChamp ->
                buildViewModel.setChampion(selectedChamp)
                dismiss()
            }
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                binding.recyclerView.getHitRect(rect)
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
                adapter.updateFilter(newQuery = text)
                return true
            }
            override fun onQueryTextSubmit(q: String?) = false
        })
        binding.btnTop.setOnClickListener     { adapter.updateFilter(newLane = Lane.TOP) }
        binding.btnJungle.setOnClickListener  { adapter.updateFilter(newLane = Lane.JUNGLE) }
        binding.btnMid.setOnClickListener     { adapter.updateFilter(newLane = Lane.MID) }
        binding.btnAdc.setOnClickListener     { adapter.updateFilter(newLane = Lane.ADC) }
        binding.btnSupport.setOnClickListener { adapter.updateFilter(newLane = Lane.SUPPORT) }
        binding.btnFill.setOnClickListener { adapter.updateFilter(newLane = null )}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
