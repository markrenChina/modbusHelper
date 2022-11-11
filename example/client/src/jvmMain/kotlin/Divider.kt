import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A divider is a thin line that groups content in lists and layouts
 *
 * @author markrenChina
 * @param color color of the divider line
 * @param thickness thickness of the divider line, 1 dp is used by default
 * @param startIndent start offset of this line, no offset by default
 */
@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onSurface.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp,
    topIndent: Dp = 0.dp,
    bottomIndent: Dp = 0.dp
) {
    val indentMod = if (topIndent.value != 0f || bottomIndent.value != 0f) {
        Modifier.padding(top = topIndent,bottom = bottomIndent)
    } else {
        Modifier
    }
    Box(
        modifier.then(indentMod)
            .fillMaxHeight()
            .width(thickness)
            .background(color = color)
    )
}


@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onSurface.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp,
    endIndent: Dp = 0.dp
){
    val indentMod = if (startIndent.value != 0f || endIndent.value != 0f) {
        Modifier.padding(start = startIndent,end = endIndent)
    } else {
        Modifier
    }
    Box(
        modifier.then(indentMod)
            .fillMaxWidth()
            .height(thickness)
            .background(color = color)
    )
}

private const val DividerAlpha = 0.12f