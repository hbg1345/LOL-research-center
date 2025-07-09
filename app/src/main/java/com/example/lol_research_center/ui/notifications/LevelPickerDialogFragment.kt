package com.example.lol_research_center.ui.notifications

import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult

class LevelPickerDialogFragment : DialogFragment() {

    interface LevelPickerListener {
        fun onLevelSelected(level: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val currentLevel = arguments?.getInt(ARG_CURRENT_LEVEL) ?: 1

        val numberPicker = NumberPicker(context).apply {
            minValue = 1
            maxValue = 18
            value = currentLevel
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Select Level")
            .setView(numberPicker)
            .setPositiveButton("OK") { _, _ ->
                val selectedLevel = numberPicker.value
                setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY_SELECTED_LEVEL to selectedLevel))
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create()
    }

    companion object {
        const val TAG = "LevelPickerDialogFragment"
        const val REQUEST_KEY = "level_picker_request"
        const val BUNDLE_KEY_SELECTED_LEVEL = "selected_level"
        private const val ARG_CURRENT_LEVEL = "current_level"

        fun newInstance(currentLevel: Int): LevelPickerDialogFragment {
            val fragment = LevelPickerDialogFragment()
            fragment.arguments = bundleOf(ARG_CURRENT_LEVEL to currentLevel)
            return fragment
        }
    }
}