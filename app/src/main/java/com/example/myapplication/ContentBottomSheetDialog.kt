package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.myapplication.databinding.ContentDialogBinding

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
    }
}