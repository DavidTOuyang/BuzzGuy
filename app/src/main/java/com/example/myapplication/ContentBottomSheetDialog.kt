package com.example.myapplication

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.myapplication.databinding.ContentDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class ContentBottomSheetDialog : BottomSheetDialogFragment() {
    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_CONTENT_TEXT = "content_text"

        fun newInstance(title: String, contentText: String): ContentBottomSheetDialog {
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_CONTENT_TEXT, contentText)
            }
            return ContentBottomSheetDialog().apply {
                arguments = args
            }
        }
    }

    private lateinit var binding: ContentDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ContentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        // Disable dismissing the dialog by clicking outside or pressing the back button
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialog.setOnShowListener { dialogInterface ->
            val d = dialogInterface as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from(bottomSheet)

                // Get screen height and calculate 85% of it
                val displayMetrics = resources.displayMetrics
                val screenHeight = displayMetrics.heightPixels
                val desiredHeight = (screenHeight * 0.90).toInt()

                // Calculate the top offset for the 90% expansion
                val expandedOffset = screenHeight - desiredHeight

                // Set the layout parameters to the desired height
                val layoutParams = bottomSheet.layoutParams
                layoutParams.height = desiredHeight
                bottomSheet.layoutParams = layoutParams

                // Manually set the expanded offset to anchor the sheet correctly
                behavior.expandedOffset = expandedOffset

                // Force the sheet to expand and ignore content height
                behavior.isFitToContents = false
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val titleTextView: TextView = view.findViewById(R.id.content_dialog_title)
//        val contentTextView: TextView = view.findViewById(R.id.content_dialog_text)

        // Retrieve arguments from the bundle
        val title = arguments?.getString(ARG_TITLE) ?: "Default Title"
        val contentText = arguments?.getString(ARG_CONTENT_TEXT) ?: "No content available."

//        // Set the content based on the arguments
//        binding.contentDialogTitle.text = title
//        binding.contentDialogText.text = contentText

        binding.closeButton.setOnClickListener {
            // Get the BottomSheetBehavior
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from(bottomSheet)

                // Trigger the smooth animation by setting the state to HIDDEN
                behavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                // As a fallback, call dismiss() directly if the behavior isn't found
                dismiss()
            }
        }
    }
}