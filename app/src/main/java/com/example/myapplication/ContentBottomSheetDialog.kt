package com.example.myapplication

import android.app.Dialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.LeadingMarginSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.BundleCompat
import androidx.core.text.HtmlCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.myapplication.databinding.ContentDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class ContentBottomSheetDialog : BottomSheetDialogFragment() {
    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_CONTENT_ITEMS = "content_text"

        fun newInstance(title: String, contentItems: List<ContentItem>): ContentBottomSheetDialog {
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putParcelableArrayList(ARG_CONTENT_ITEMS, ArrayList(contentItems))
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

        // Retrieve arguments from the bundle
        val title = arguments?.getString(ARG_TITLE) ?: "Default Title"
        // Use BundleCompat.getParcelableArrayList for modern, type-safe retrieval
        val contentItems: List<ContentItem> = arguments?.let {
            BundleCompat.getParcelableArrayList(it, ARG_CONTENT_ITEMS, ContentItem::class.java)
        } ?: emptyList()

        // Set the content based on the arguments
        binding.dialogTitle.text = title

        // Build the content dynamically using SpannableStringBuilder
        val spannableBuilder = SpannableStringBuilder()
        val bulletIndent = 48 // Example: 48 pixels
        val bulletGapWidth = 16 // The gap between the bullet and the text

        contentItems.forEach { item ->
            when (item) {
                is ContentItem.Paragraph -> {
                    val htmlText = HtmlCompat.fromHtml(item.text, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    spannableBuilder.append(htmlText)
                }
                is ContentItem.BulletList -> {
                    item.items.forEach { bulletText ->
                        val start = spannableBuilder.length
                        spannableBuilder.append(bulletText).append("\n\n")
                        val end = spannableBuilder.length

                        // Apply the indentation for the entire paragraph, including the bullet.
                        spannableBuilder.setSpan(
                            LeadingMarginSpan.Standard(bulletIndent, 0),
                            start,
                            end,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )

                        // Apply the BulletSpan with the desired gap.
                        spannableBuilder.setSpan(
                            BulletSpan(bulletGapWidth),
                            start,
                            end,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        }
        binding.dialogContent.text = spannableBuilder

        binding.closeButton.setOnClickListener {
            dismiss() // Use dismiss() for BottomSheetDialogFragment
        }
    }
}