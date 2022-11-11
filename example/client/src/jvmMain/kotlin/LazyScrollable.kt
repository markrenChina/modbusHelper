import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun <T> LazyScrollable(
    modifier: Modifier,
    list: List<T>,
    background: Color = MaterialTheme.colors.background,
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(background)
    ) {
        val state = rememberLazyListState()
        //Text(text = "批号",modifier = modifier.height(20.dp).align(Alignment.Center))
        LazyColumn(Modifier.fillMaxSize().padding(end = 12.dp), state) {
            itemsIndexed(items = list, itemContent = itemContent)
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            )
        )
    }
}