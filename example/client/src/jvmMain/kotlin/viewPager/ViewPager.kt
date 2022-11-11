package viewPager

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ViewPager(
    currentTabPage: ITabPage,
    tabPagesList: List<ITabPage>,
    backgroundColor: Color = MaterialTheme.colors.background,
    onTabSelected: (currentTabPage: ITabPage) -> Unit
) {
    TabRow(
        selectedTabIndex = currentTabPage.serial,
        backgroundColor = backgroundColor,
        indicator = { tabPositions ->
            HomeTabIndicator(tabPositions, currentTabPage)
        }
    ) {
        tabPagesList.forEach { tab ->
            TabItem(
                //icon = painterResource(tab.icon),
                null,
                title = tab.title, onClick = {
                    onTabSelected(tab)
                })
        }

    }
}

/**
 * 显示选项卡的指示器。
 *
 * @param tabPositions The list of [ITabPage]s from a [TabRow].
 * @param settingTabPage The [ITabPage] that is currently selected.
 */
@Composable
private fun HomeTabIndicator(
    tabPositions: List<TabPosition>,
    settingTabPage: ITabPage
) {
    val transition = updateTransition(
        settingTabPage,
        label = "Tab indicator"
    )
    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            if (this.initialState.serial < this.targetState.serial) {
                spring(stiffness = Spring.StiffnessVeryLow)
            } else {
                spring(stiffness = Spring.StiffnessLow)
            }
        },
        label = "Indicator left"
    ) { page ->
        tabPositions[page.serial].left
    }
    val indicatorRight by transition.animateDp(
        transitionSpec = {
            if (this.initialState.serial < this.targetState.serial) {
                spring(stiffness = Spring.StiffnessLow)
            } else {
                spring(stiffness = Spring.StiffnessVeryLow)
            }
        },
        label = "Indicator right"
    ) { page ->
        tabPositions[page.serial].right
    }

    val color by transition.animateColor(
        label = "Border color"
    ) { page ->
        Color.Black
    }
    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .padding(4.dp)
            .fillMaxSize()
            .background(color = color.copy(alpha = 0.3f))
            .border(
                BorderStroke(2.dp, color),
                RoundedCornerShape(4.dp)
            )
    )
}

/**
 * 显示一个标签。
 *
 * @param icon The icon to be shown on this tab.
 * @param title The title to be shown on this tab.
 * @param onClick Called when this tab is clicked.
 * @param modifier The [Modifier].
 */
@Composable
private fun TabItem(
    icon: Painter?,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Image(
                painter = icon,
                modifier = Modifier.size(30.dp),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title)
    }
}