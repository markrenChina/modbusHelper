import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SpaceAroundRowDefaults {

    val TopElevation = 0.dp

    val BottomElevation = 8.dp

    val ContentPadding = PaddingValues(
        start = 4.dp,
        end = 4.dp
    )
}



@Composable
fun SpaceAroundRow(
    modifier: Modifier,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = SpaceAroundRowDefaults.TopElevation,
    contentPadding: PaddingValues = SpaceAroundRowDefaults.ContentPadding,
    shape: Shape = RectangleShape,
    content: @Composable() (RowScope.() -> Unit)
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        shape = shape,
        modifier = modifier
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}