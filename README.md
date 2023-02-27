# Description

Jetpack Compose. Введение. За основу взята 1-я и 2-я лекция Android Academy

## Navigation

+ [up](../jetpack_compose)
+ [root](../master)

## Details

### Preview

Для preview можно воспользоваться следующим, `PreviewParameterProvider` даёт возможность передать несколько значений для просмотра:

```kotlin
class HelloPreviewParameterProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf(
        "Compose", "Android", "World"
    )
}
// ...
@Preview(
    showBackground = true,
    fontScale = 2f,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true
)
@Composable
fun HelloPreview(
    @PreviewParameter(HelloPreviewParameterProvider::class)
    hello: String
) {
    Hello(hello)
}
```
Можно настраивать отображение, пример аннотации: [DevicesPreview.kt](./app/src/main/java/com/artofmainstreams/examples/ui/DevicesPreview.kt)

Получение размеров из ресурсов:
```kotlin
stringResource(id = R.string.hello, name)
dimensionResource(id = R.dimen.some_padding)
with(LocalDensity.current) { dimensionResource(id = R.dimen.font_size_32).toSp() } // sp получать нетривиально
painterResource(id = R.drawable.logo)
colorResource(R.color.purple_700) // на время переходного периода, а  так - из темы
```

### Темы

Можно использовать Material2, Material3 или свою</br>
Подробнее в коде ([Theme.kt](./app/src/main/java/com/artofmainstreams/examples/theme/Theme.kt)) и при генерации проекта

### Scope

У каждого виджета есть свой Scope, который расширяет возможности в теле лямбды 
(подробнее в файле [BoxScope](./app/src/main/java/com/artofmainstreams/examples/ui/BoxScope.kt))

### Accessibility

Подробнее в файле [Fragment01.kt](./app/src/main/java/com/artofmainstreams/examples/ui/example01/Fragment01.kt)

```kotlin
Modifier.clickable(enabled = true, onClickLabel = "Обычный текст") // если элемент интерактивный
Modifier.semantics { 
    onClick(label = "Некоторое действие") { true }
    stateDescription = if (selected) "Выбран контакт, с которым делимся контентом"
    else "Этот контакт не выбран. С ним подкастом не делимся"
    heading() // для основной информации
} // если у элемента есть метод onClick, ещё для тестов
Icon(imageVector = Icons.Filled.Share, contentDescription = "Поделить подкастом")   // иконка
Box(modifier = Modifier.semantics {
    customActions = listOf(
        CustomAccessibilityAction(label = "Поделиться контактом", action = { true }),
        CustomAccessibilityAction(label = "Добавить в избранное", action = { true })
    )
}) {
    Button(onClick = { }, modifier = Modifier.clearAndSetSemantics { }) { }
}
```

### Widgets

Находятся в файле [Widgets.kt](./app/src/main/java/com/artofmainstreams/examples/ui/Widgets.kt). Вложенность для compose допускается, т.к. вычисления происходят только один раз
+ Box - аналог FrameLayout
+ Text - текст, через buildAnnotatedString можно менять стиль у частей текста
+ Surface - как Box, только можно указать цвет/размер/форму/etc
+ Image - картинка
+ ClickableText - кликабельный текст с возможностью отследить места нажатия
+ Button - кнопка
+ OutlinedButton - кнопка
+ TextButton - кликабельный текст
+ IconButton - кликабельная иконка
+ Column - столбец
+ Row - ряд
+ Spacer - для пустого пространства
+ TextField (и focusManager, focusRequester) - поле ввода текста
+ OutlinedTextField - поле ввода текста
+ BasicTextField - поле ввода текста базовое
+ Card - карточка
+ Divider - разделитель
+ LazyColumn - аналог RecyclerView
+ LazyGrid - аналог RecyclerView

### State
Примеры виджета в файле [StatefulWidgets.kt](./app/src/main/java/com/artofmainstreams/examples/ui/StatefulWidgets.kt)</br>
Примеры теста виджета с состоянием в файле [WidgetsKtTest.kt](app/src/androidTest/java/com/artofmainstreams/examples/ui/WidgetsKtTest.kt)

+ Композиция (composition) - процесс, когда наш код превращается во view на экране
+ Начальная композиция (initial composition) - когда впервые запускаем
+ Рекомпозиция (recomposition) - когда нужно view перерисовать ещё раз, стирает состояние функций

Composable-функции автоматически подписываются на `MutableState` для обновления . Но при этом нужно обернуть в
`remember`, чтобы сохранять состояние между рекомпозицией  (чтоб из стека переменную в кучу перекинуть и значение не потерять)</br>
Для сохранения состояния при смене конфигурации (поворот экрана, например) есть `rememberSaveable`, использовать аккуратно,
значения должны быть parcelable (чтоб можно было положить в bundle при onSaveInstanceState)

Можно использовать паттерн state hoisting, когда state выносим на уровень выше (например, во viewmodel), пример будет позже

### ConstraintLayout

Если вдруг хотим использовать ConstraintLayout, то сначала его нужно подключить:
```groovy
// Constraint Layout
implementation 'androidx.constraintlayout:constraintlayout-compose:1.0.1'
```
Более подробный пример экрана в файле [ConstraintExample.kt](./app/src/main/java/com/artofmainstreams/examples/ui/ConstraintExample.kt)</br>
Т.к. id-шников у виджетов нет, то определяем и затем присваиваем их через `createRef`:
```kotlin
val button = createRef()
val (logo, text) = createRefs()
LogoWidget(modifier = Modifier
    .constrainAs(logo) {
        centerHorizontallyTo(parent)
        top.linkTo(parent.top, margin = 16.dp)
        bottom.linkTo(parent.bottom, margin = 16.dp)
        height = Dimension.fillToConstraints
    }
)
```
Можно создавать то же, что и через разметку, в т.ч. цепи, барьеры и т.д.

## Stable State
Для проверки, что рекомпозиция проходит хорошо, можно подключить compose compiler

```groovy
subprojects {
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).configureEach {
        kotlinOptions {
            if (project.findProperty("myapp.enableComposeCompilerReports") == "true") {
                freeCompilerArgs += [
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                        project.buildDir.absolutePath + "/compose_metrics"
                ]
                freeCompilerArgs += [
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                        project.buildDir.absolutePath + "/compose_metrics"
                ]
            }
        }
    }
}
```

Для указания, что свойство неизменяемо, можно использовать `@Immutable`, `@Stable`, Immutable Collections</br>
Для лямбд можно использовать kotlin reference `viewModel::onChange`</br>
Если state редко обновляется, то лучше использовать `derivedStateOf`

### Side Effects
Это когда из compose-функции идёт обращение наружу и неизвестно, на что влияет
```kotlin
SideEffect { // нужен когда хотим менять state на каждую рекомпозицию
    // вызывается после успешной рекомпозиции, во время неё state менять нельзя
}
val current by rememberUpdatedState(outValue) // для передачи обновлённых значений в SideEffect без перезапуска самого эффекта
DisposableEffect(key1 = key) {
    // вызывается когда сменился ключ
    check(current)
    onDispose {
        // когда меняем ключ или отменяем, тут очистка происходит и перезапуск
    }
}
LaunchedEffect(key1 = key) {
    // внутри корутины, отменяет и перезапускает корутину при смене ключа
}
val scope = rememberCoroutineScope()
```
`snapShotFlow` для превращения из StateFlow в обычный и `productState` наоборот

## Links

+ [Курс от Android Academy, лекции](https://www.youtube.com/playlist?list=PLjLCGE4bVpHAGx8tW7aMx0q0RYH0HBVun)
+ [Курс от Android Academy, семинары](https://www.youtube.com/playlist?list=PLjLCGE4bVpHC9jREt1l-9MnbRMgdkNswk)
+ [Примеры от гугла](https://github.com/android/compose-samples)
+ [Полезности, которые ещё не завезли](https://github.com/google/accompanist)
+ [Статья про профилирование](https://habr.com/ru/articles/701422/)
+ [Статья про лямбды](https://multithreaded.stitchfix.com/blog/2022/08/05/jetpack-compose-recomposition/)
+ [Объяснение collectAsStateWithLifecycle](https://medium.com/androiddevelopers/consuming-flows-safely-in-jetpack-compose-cde014d0d5a3)
