package com.example.lol_research_center.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lol_research_center.R
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lol_research_center.databinding.FragmentDashboardBinding
import com.example.lol_research_center.model.ItemData
import com.example.lol_research_center.model.ItemDataLoader
import com.example.lol_research_center.model.ItemStats
import com.example.lol_research_center.ui.viewmodel.BuildViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val buildViewModel: BuildViewModel by activityViewModels()
    private lateinit var selectedItemsAdapter: SelectedItemsAdapter
    private lateinit var dashboardAdapter: DashboardAdapter
    private var originalItems: List<ItemData> = emptyList()

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

        context?.let {
            originalItems = ItemDataLoader.loadItemsFromAsset(it, "item_unique_by_id.json")
        }

        val pickerMode = arguments?.getBoolean("pickerMode") ?: false

        if (pickerMode) {
            binding.exitButton.visibility = View.VISIBLE
        } else {
            binding.exitButton.visibility = View.GONE
        }

        binding.exitButton.setOnClickListener {
            findNavController().navigate(R.id.buildsFragment)
        }

        dashboardAdapter = DashboardAdapter(originalItems) { item ->
            if (pickerMode) {
                // 기존 아이템 선택 로직
                if ((buildViewModel.currentBuild.value?.items?.size ?: 0) < 6) {
                    buildViewModel.addItem(item)
                } else {
                    Toast.makeText(requireContext(), "아이템은 최대 6개까지 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // pickerMode가 아닐 때 모달 띄우기
                showItemDetailModal(item)
            }
        }
        recyclerView.adapter = dashboardAdapter

        selectedItemsAdapter = SelectedItemsAdapter() {
            buildViewModel.removeItem(it)
        }
        binding.selectedItemsRecyclerView.adapter = selectedItemsAdapter

        if (pickerMode) {
            binding.selectedItemsContainer.visibility = View.VISIBLE
            binding.selectedItemsRecyclerView.visibility = View.VISIBLE
            binding.nextButton.visibility = View.VISIBLE
        } else {
            binding.selectedItemsContainer.visibility = View.GONE
            binding.selectedItemsRecyclerView.visibility = View.GONE
            binding.nextButton.visibility = View.GONE
        }

        buildViewModel.currentBuild.observe(viewLifecycleOwner) { buildInfo ->
            buildInfo?.items?.let {
                selectedItemsAdapter.updateItems(it)
            }
        }

        binding.nextButton.setOnClickListener {
            buildViewModel.currentBuild.value?.let {
                val bundle = Bundle().apply {
                    putParcelable("build", it)
                    putBoolean("pickerMode", pickerMode)
                }
                findNavController().navigate(R.id.action_selectItems_to_notifications, bundle)
            } ?: run {
                Toast.makeText(requireContext(), "빌드 정보가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterItems(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.searchButton.setOnClickListener {
            filterItems(binding.searchEditText.text.toString())
        }
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
        dashboardAdapter.updateItems(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun formatItemStats(itemStats: ItemStats?): String {
        if (itemStats == null) return ""

        val statsList = mutableListOf<String>()

        itemStats.flatPhysicalDamageMod?.let { if (it != 0.0) statsList.add("공격력: ${it.toInt()}") }
        itemStats.flatMagicDamageMod?.let { if (it != 0.0) statsList.add("주문력: ${it.toInt()}") }
        itemStats.flatAttackSpeedMod?.let { if (it != 0.0) statsList.add("공격 속도: ${String.format("%.2f", it)}") }
        itemStats.flatArmorMod?.let { if (it != 0.0) statsList.add("방어력: ${it.toInt()}") }
        itemStats.flatSpellBlockMod?.let { if (it != 0.0) statsList.add("마법 저항력: ${it.toInt()}") }
        itemStats.flatHPPoolMod?.let { if (it != 0.0) statsList.add("체력: ${it.toInt()}") }
        itemStats.flatMPPoolMod?.let { if (it != 0.0) statsList.add("마나: ${it.toInt()}") }
        itemStats.flatMovementSpeedMod?.let { if (it != 0.0) statsList.add("이동 속도: ${it.toInt()}") }
        itemStats.percentAttackSpeedMod?.let { if (it != 0.0) statsList.add("공격 속도 %: ${String.format("%.0f", it * 100)}%") }
        itemStats.percentHPPoolMod?.let { if (it != 0.0) statsList.add("체력 %: ${String.format("%.0f", it * 100)}%") }
        itemStats.percentMPPoolMod?.let { if (it != 0.0) statsList.add("마나 %: ${String.format("%.0f", it * 100)}%") }
        itemStats.flatHPRegenMod?.let { if (it != 0.0) statsList.add("체력 재생: ${it.toInt()}") }
        itemStats.flatMPRegenMod?.let { if (it != 0.0) statsList.add("마나 재생: ${it.toInt()}") }
        itemStats.flatCritChanceMod?.let { if (it != 0.0) statsList.add("치명타 확률: ${String.format("%.0f", it * 100)}%") }
        itemStats.flatCritDamageMod?.let { if (it != 0.0) statsList.add("치명타 피해: ${it.toInt()}") }
        itemStats.percentLifeStealMod?.let { if (it != 0.0) statsList.add("생명력 흡수 %: ${String.format("%.0f", it * 100)}%") }
        itemStats.percentSpellVampMod?.let { if (it != 0.0) statsList.add("주문 흡혈 %: ${String.format("%.0f", it * 100)}%") }
        itemStats.rPercentCooldownMod?.let { if (it != 0.0) statsList.add("스킬 가속 %: ${String.format("%.0f", it * 100)}%") }
        itemStats.rFlatArmorPenetrationMod?.let { if (it != 0.0) statsList.add("물리 관통력: ${it.toInt()}") }
        itemStats.rFlatMagicPenetrationMod?.let { if (it != 0.0) statsList.add("마법 관통력: ${it.toInt()}") }
        itemStats.rPercentArmorPenetrationMod?.let { if (it != 0.0) statsList.add("물리 관통력 %: ${String.format("%.0f", it * 100)}%") }
        itemStats.rPercentMagicPenetrationMod?.let { if (it != 0.0) statsList.add("마법 관통력 %: ${String.format("%.0f", it * 100)}%") }


        return statsList.joinToString("\n")
    }
    private fun showItemDetailModal(item: ItemData) {
        // 1) 커스텀 다이얼로그 레이아웃 인플레이트
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_item_details, null)

        // 2) 뷰 찾기
        val imageView     = dialogView.findViewById<ImageView>(R.id.itemImageView)
        val titleView     = dialogView.findViewById<TextView>(R.id.itemTitleTextView)
        val goldView      = dialogView.findViewById<TextView>(R.id.itemGoldTextView)
        val descView      = dialogView.findViewById<TextView>(R.id.itemDescTextView)

        // 3) 이미지·이름·골드 세팅
        imageView.setImageResource(item.imageResId)
        titleView.text = item.name
        goldView.text  = "조합 가격 : ${item.gold?.base ?: 0}"

        // 4) 오직 스탯 문자열만 가져오기
        val statsDesc = formatItemStats(item.stats)

        // 5) 아무 스탯도 없으면 기본 메시지
        descView.text = if (statsDesc.isNotBlank()) {
            statsDesc
        } else {
            "표시할 스탯 정보가 없습니다."
        }

        // 6) 다이얼로그 띄우기
        MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setPositiveButton("확인", null)
            .show()
    }

}