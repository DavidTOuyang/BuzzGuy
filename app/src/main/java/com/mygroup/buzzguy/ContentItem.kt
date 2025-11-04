package com.mygroup.buzzguy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ContentItem : Parcelable {
    @Parcelize
    data class Paragraph(val text: String) : ContentItem()

    @Parcelize
    data class BulletList(val items: List<String>) : ContentItem()
}