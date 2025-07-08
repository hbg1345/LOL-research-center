package com.example.lol_research_center.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.lol_research_center.ui.viewmodel.BuildViewModel

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

        dashboardAdapter = DashboardAdapter(originalItems) {
            if (pickerMode) {
                if ((buildViewModel.currentBuild.value?.items?.size ?: 0) < 6) {
                    buildViewModel.addItem(it)
                } else {
                    Toast.makeText(requireContext(), "아이템은 최대 6개까지 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
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
}