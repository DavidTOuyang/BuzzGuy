package com.example.myapplication

sealed class DrawerAction {
    object NewChat : DrawerAction()
    data class ShowContent(val title: String, val content: List<ContentItem>) : DrawerAction()
    // Define other actions here
}