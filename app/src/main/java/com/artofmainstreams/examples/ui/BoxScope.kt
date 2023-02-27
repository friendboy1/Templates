import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
inline fun MyTable(
    content: TableScope.() -> Unit
) {
    TableScopeInstance.content()
}

@Preview
@Composable
fun SomeFunction() {
    Column {
        MyTable {
            Text(text = "MyTable Some text", modifier = Modifier.matchParentSize())
        }
        /**
         * Смысл в том, что у каждого виджета есть свой Scope, который, например, может добавить
         * методы для Modifier у детей. Box в качестве примера. Достигается за счёт хитрого extension
         */
        Box(modifier = Modifier.size(300.dp)) {
            Text(text = "Box Some text", modifier = Modifier.align(Alignment.Center).background(Color.Yellow))
            someMethod()
        }
    }
}

/**
 * Примерно такая реализация в исходниках, всего лишь пример
 */
@LayoutScopeMarker
interface TableScope {
    fun Modifier.align(alignment: Alignment): Modifier
    fun Modifier.matchParentSize(): Modifier
}

/**
 * А вот полезное, если мы что-то хотим, чтоб выполнялось только для тела в Box
 */
fun BoxScope.someMethod() = ""

object TableScopeInstance : TableScope {
    override fun Modifier.align(alignment: Alignment): Modifier = this.then(
        Modifier.background(
            Color.Green
        )
    )

    override fun Modifier.matchParentSize(): Modifier = this.then(
        Modifier.background(
            Color.Gray
        )
    )
}
