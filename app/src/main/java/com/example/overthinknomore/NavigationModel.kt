package com.example.overthinknomore

import androidx.compose.ui.graphics.Color

// 1. This must be an ENUM class, not an annotation
enum class MyResourceType {
    REGULAR,
    ABOUT,
    EMERGENCY
}

// 2. Data class with default values
data class NavigationModel(
    val title: String,
    val desc: String,
    val icon: String,
    val action: String = "",
    val type: MyResourceType = MyResourceType.REGULAR,
    val customColor: Color? = null
)