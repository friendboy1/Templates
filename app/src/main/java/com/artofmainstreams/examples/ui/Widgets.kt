package com.artofmainstreams.examples.ui

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.theme.ComposeExampleTheme
import com.artofmainstreams.examples.theme.newColor
import kotlinx.coroutines.launch

/**
 * Класс для передачи различных значений в функцию для preview
 */
class PageParams : PreviewParameterProvider<Int> {
    override val values: Sequence<Int>
        get() = (1..12).asSequence()
}

class PageParamsSecondVariant : CollectionPreviewParameterProvider<Int>((1..12).toList())

/**
 * Box
 * Text
 * Surface
 * Image
 * AnnotatedString
 * ClickableText
 * Button
 * OutlinedButton
 * TextButton
 * IconButton
 * Column
 * Row
 * Spacer
 * TextField (и focusManager, focusRequester)
 * OutlinedTextField
 * BasicTextField
 * Card
 * Divider
 * LazyColumn
 * LazyGrid
 */
@DevicesPreview
@Composable
fun Preview(
    @PreviewParameter(PageParams ::class)
    page: Int
) {
    ComposeExampleTheme {
        when (page) {
            1 -> {
                LoaderComponent(modifier = Modifier.fillMaxSize())
                FunctionName("LoaderComponent()")
            }
            2 -> {
                LoaderComponent(
                    modifier = Modifier.size(200.dp, 400.dp),
                    backgroundColor = Color.Black.copy(alpha = 0.3f)
                )
                FunctionName("""LoaderComponent(
                        |modifier = Modifier.size(200.dp, 400.dp),
                        |backgroundColor = Color.Black.copy(alpha = 0.3f))""".trimMargin())
            }
            3 -> {
                Logo()
                FunctionName("Logo()")
            }
            4 -> {
                TextWithBoldSuffix()
                FunctionName("TextWithBoldSuffix()")
            }
            5 -> {
                ContactSupportText()
                FunctionName("ContactSupportText()")
            }
            6 -> {
                ButtonComponent()
                FunctionName("ButtonComponent()")
            }
            7 -> {
                RowComponent()
                FunctionName("RowComponent()")
            }
            8 -> {
                SearchField()
                FunctionName("SearchField()")
            }
            9 -> {
                CardComponent()
                FunctionName("CardComponent()")
            }
            10 -> {
                LazyColumnComponent()
                FunctionName("LazyColumnComponent()")
            }
            11 -> {
                LazyColumnMultiComponent()
                FunctionName("LazyColumnMultiComponent()")
            }
            12 -> {
                LazyColumnStickyHeaderComponent()
                FunctionName("LazyColumnStickyHeaderComponent()")
            }
            else -> FunctionName("Ошибка")
        }
    }
}

/**
 * Box - аналог FrameLayout
 * Text
 * Surface - для указания цвета, размера, формы, блокирует нажатия за собой
 */
@Composable
fun LoaderComponent(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.surface
) {
    Surface(modifier = modifier, color = backgroundColor) {
        Box(
            contentAlignment = Alignment.Center,
            propagateMinConstraints = false // передать компоненту свои размеры в качестве минимальных, у Surface - true
        ) {
            CircularProgressIndicator()
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.caption,
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(all = 32.dp)
            )
        }
    }
}

/**
 * Image - для картинок
 */
@Composable
fun Logo() {
    Column {
        Image(
            painter = painterResource(id = R.drawable.profile_picture),
            contentDescription = "Фотография профиля"
        )
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.profile_picture),
            contentDescription = "Фотография профиля"
        )
        Image(
            // можно прям в коде описать векторное изображение
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Фотография профиля",
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.Inside,
            alpha = 0.8f,
            colorFilter = ColorFilter.tint(Color.Magenta, BlendMode.ColorBurn),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .clip(RoundedCornerShape(percent = 25))
                .border(
                    BorderStroke(
                        width = 8.dp,
                        color = MaterialTheme.colors.primary
                    ),
                    RoundedCornerShape(percent = 25)
                )
        )
    }
}

/**
 * AnnotatedString для изменения части стиля текста
 */
@Composable
fun TextWithBoldSuffix(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    prefix: String = "Error code: ",
    suffix: String = "AA-31",
) {
    Row(modifier = modifier) {
        Text(
            text = buildAnnotatedString {
                append(prefix)
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(suffix)
                }
            },
            modifier = textModifier
        )
        Text(
            text = buildAnnotatedString {
                append(prefix)
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append(suffix)
                pop(0)
            },
            modifier = textModifier
        )
    }
}

/**
 * ClickableText - на текст можно нажать и выполнится действие
 */
@OptIn(ExperimentalTextApi::class)
@Composable
fun ContactSupportText(
    modifier: Modifier = Modifier,
    onContactClicked: () -> Unit = {
        Log.i("ContactSupportText", "Clicked")
    }
) {
    val annotatedString = buildAnnotatedString {
        append("Нажмите на ссылку: ")
        withStyle(SpanStyle(textDecoration = TextDecoration.Underline, color = Color.Blue)) {
            withAnnotation(tag = "help_tag", annotation = "https://artofmainstreams.ru") {
                append("tech support")
            }
        }
    }
    ClickableText(
        text = annotatedString, onClick = { offset -> // отступ, где кликнули
            annotatedString.getStringAnnotations("help_tag", offset, offset)
                .firstOrNull()?.let { annotation ->
                    annotation.item // https://artofmainstreams.ru
                    onContactClicked()
                }
            Log.i("ContactSupportText", "Clicked")
        }, style = TextStyle.Default.merge(
            TextStyle(textAlign = TextAlign.Center)
        )
    )
}

/**
 * Button
 * OutlinedButton
 * TextButton
 * IconButton
 */
@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
) {
    Column {
        Button(
            onClick = { },
            shape = RoundedCornerShape(topStart = 32.dp, bottomEnd = 32.dp),
            contentPadding = PaddingValues(
                horizontal = 32.dp,
                vertical = 20.dp
            ),   // отступы для контента
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Cyan
            ),
            modifier = modifier
        ) {   // content: @Composable RowScope.() -> Unit
            ContactSupportText()
            Icon(
                painter = painterResource(id = R.drawable.ic_refresh_24),
                contentDescription = "Обновить",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        OutlinedButton(onClick = { }) {
            Text(text = "OutlinedButton")
        }
        TextButton(onClick = { }) {
            Text(text = "Clickable text button")
        }
        IconButton(onClick = { }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_refresh_24),
                contentDescription = "Обновить"
            )
        }
        // если текст длинный, он выкинет за пределы иконку, решается через Modifier.weight()
        // сначала заполняются элементы без веса, затем с весом
        Button(onClick = { }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = "Refresh a very long text"
                val stringBuilder = StringBuilder()
                for (i in 1..3) {
                    stringBuilder.append(text)
                    stringBuilder.append(" ")
                }
                Text(modifier = Modifier.weight(1f), text = stringBuilder.toString())
                Icon(
                    painter = painterResource(id = R.drawable.ic_refresh_24),
                    contentDescription = "Обновить"
                )
            }
        }
    }
}

/**
 * Column
 * Row
 * Spacer - нужен для создания пустого пространства
 */
@Composable
fun RowComponent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            RedSquare()
            GreenSquare()
            BlueSquare()
        }
        Row(    // по краям половина расстояния относительно расстояния между
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically, // по вертикали можно как угодно выровнять
            modifier = Modifier.fillMaxWidth()
        ) {
            RedSquare()
            GreenSquare()
            BlueSquare()
        }
        Row(    // равномерное расстояние
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            RedSquare()
            GreenSquare()
            BlueSquare(modifier = Modifier.align(Alignment.Top))    // конкретный элемент можно выровнять отдельно
        }
        Row(    // по краям объекты у стенки
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            RedSquare()
            GreenSquare()
            BlueSquare()
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            RedSquare()
            GreenSquare()
            BlueSquare()
        }
        Row(    // расстояние можно регулировать через padding или spacer
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            RedSquare()
            GreenSquare(modifier = Modifier.padding(start = 20.dp))
            Spacer(modifier = Modifier.width(40.dp))
            BlueSquare()
        }
    }
}

/**
 * TextField - поле для ввода текста
 * при вводе текста состояние не меняется, т.к. декларативненько всё, нужна подписка на изменение
 * используется паттерн Slot API (возможность передать любую compose-функцию)
 * OutlinedTextField
 * BasicTextField - более базовый компонент, который можно настроить под свои нужды
 */
@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    searchQuery: String = "",   // можно поменять и посмотреть как отображается
    onQueryChanged: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current    // перемещаемся по полям
    val focusRequester = remember { FocusRequester() }
    // вызываем фокус для появление клавиатуры в нужный момент (не в этот)
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

        TextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .then(modifier),
            value = searchQuery,
            onValueChange = onQueryChanged,
            textStyle = MaterialTheme.typography.body2,
            singleLine = true,
            placeholder = { Text(text = "search...") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search_24),
                    contentDescription = "Поиск"
                )
            },
            trailingIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_24),
                        contentDescription = "Добавить"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,   // тип клавиатуры
                capitalization = KeyboardCapitalization.None,   // большие буквы
                autoCorrect = false,    // автоисправление
                imeAction = ImeAction.Search    // основное действие на клавиатуре
            ),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.moveFocus(FocusDirection.Next)
                //focusManager.clearFocus(force = true) // скрыть клаиватуру
            }),
            visualTransformation = remember {   // изменить визуал отображения
                PasswordVisualTransformation()
            }
        )
        OutlinedTextField(value = "Some text", onValueChange = {})
        BasicTextField(value = "Some text", onValueChange = {})
    }
}

/**
 * Card - обёртка над Surface, но с переопределёнными некоторыми параметрами, например, высотой
 * Divider
 */
@Preview
@Composable
fun CardComponent(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.newColor,
        elevation = 8.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(IntrinsicSize.Min)   // узнаём у детей высоту
            ) {
                Text(
                    text = "Text",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = R.drawable.ic_add_24),
                    contentDescription = "Добавить",
                )
            }
            Divider()
            Text(
                text = "Some text",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

/**
 * LazyColumn - аналог RecyclerView
 */
@Composable
fun LazyColumnComponent() {
    Column {
        SearchField()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),   // отступ между элементами
            contentPadding = PaddingValues(16.dp)   // отступ всего списка от краёв
        ) {
            // пример с числами, но в целом через коллекцию можно
            // можно так объявить
            repeat((1..15).count()) {   // если коллекция, то через forEach
                item {  // каждый элемент оборачиваем в item
                    CardComponent()
                }
            }
            // можно так объявить
            items(15) {
                CardComponent()
            }
        }
    }
}

/**
 * LazyColumn - аналог RecyclerView
 * rememberLazyListState для управлением состояния скролла
 */
@Composable
fun LazyColumnComponentScrollState() {
    val listState = rememberLazyListState()
    if (listState.isScrollInProgress) {
        Log.i("LazyColumnComponent", "Прокручиваем")
    }
    val coroutineScope = rememberCoroutineScope()
    Column {
        Button(onClick = {
            coroutineScope.launch {
                listState.animateScrollToItem(10)
            }
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Нажми, чтоб перейти к элементу 10")
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            state = listState
        ) {
            repeat((1..15).count()) {   // если коллекция, то через forEach
                item {  // каждый элемент оборачиваем в item
                    CardComponent()
                }
            }
        }
    }
}

/**
 * LazyColumn с мультивью
 */
@Composable
fun LazyColumnMultiComponent() {
    Column {
        SearchField()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            repeat((1..15).count()) {
                item { ContactSupportText() }
                items(5) {
                    CardComponent()
                }
            }
        }
    }
}

/**
 * LazyColumn stickyHeader (+ animation для перемещения, но нужны уникальные id; добавление/удаление недоступно)
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyColumnStickyHeaderComponent() {
    Column {
        SearchField()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            (1..15).forEach {
                stickyHeader { TextWithBoldSuffix(suffix = it.toString()) }
                items(
                    5,
                    //    key = { it }  // для анимации
                ) {
                    CardComponent()
                    //     CardComponent(modifier = Modifier.animateItemPlacement())    // для анимации
                }
            }
        }
    }
}


/**
 * LazyGrid
 */
@Composable
fun LazyGridComponent() {
    Column {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(16.dp),   // отступ между элементами
            horizontalArrangement = Arrangement.spacedBy(16.dp),   // отступ между элементами
            contentPadding = PaddingValues(16.dp)   // отступ всего списка от краёв
        ) {
            repeat((1..15).count()) {
                item {
                    CardComponent()
                }
            }
            items(15) {
                CardComponent()
            }
        }
    }
}

@Composable
private fun RedSquare(modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = Color.Red) {
        Box(modifier = Modifier.size(size = 80.dp))
    }
}

@Composable
private fun GreenSquare(modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = Color.Green) {
        Box(modifier = Modifier.size(size = 80.dp))
    }
}

@Composable
private fun BlueSquare(modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = Color.Blue) {
        Box(modifier = Modifier.size(size = 50.dp))
    }
}

@Preview
@Composable
private fun FunctionName(name: String = "") {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = name, modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(8.dp))
    }
}