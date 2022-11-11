package viewPager

import androidx.compose.ui.graphics.Color

interface ITabPage {
    val title: String
    val icon: String
    val backColor: Color
    val serial: Int
}