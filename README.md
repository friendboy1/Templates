# Description
Миграция на Jetpack Compose

## Navigation
+ [up](https://github.com/friendboy1/Templates/tree/jetpack_compose)
+ [root](https://github.com/friendboy1/Templates/tree/master)

## Details
+ Возможности
+ Ограничения
+ Стратегии
+ Дополнительные кейсы

### Возможности

Compose-function может быть вставлена в</br>
+ Activity
+ Fragment
+ CustomView

Activity:</br>
```groovy
implementation 'androidx.activity:activity-compose'
```
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageCard(Message("Android", "JetPack Compose"))
        }
    }
}
```

Fragment:</br>
Пример: [Fragment01.kt](app/src/main/java/com/artofmainstreams/examples/ui/example01/Fragment01.kt)</br>
```kotlin
// onCreateView() { ViewCompositionStrategy + ComposeView() }
class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, 
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext())
        .apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { Hello(name = "Compose") }
        }
}
```
ViewCompositionStrategy:</br>
+ DisposeOnDetachedFromWindow - рекомпозиция привязывается к ЖЦ compose-view внутри окна
+ DisposeOnDetachedFromWindowOrReleasedFromPool = DisposeOnDetachedFromWindow + RecyclerView
+ DisposeOnLifecycleDestroyed - когда работаем с Activity
+ DisposeOnViewTreeLifecycleDestroyed - использует ЖЦ самого ближайшего lifecycle-owner'а (для Fragment)

CustomView:</br>
Пример: [Widgets.kt](app/src/main/java/com/artofmainstreams/examples/ui/Widgets.kt)</br>
```xml
<androidx.compose.ui.platform.ComposeView
        android:id="@+id/compose_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
```
```kotlin 
binding.composeView.setContent { Hello(name = "Compose") }
```
При нескольких compose-view нужно указывать id для правильной работы `onSaveInstanceState`. 
Пример: [Fragment02.kt](app/src/main/java/com/artofmainstreams/examples/ui/example02/Fragment02.kt)

View может быть вставлена в compose в случаях:
+ ещё не реализована в compose (например, AdView)
+ переиспользовать CustomView

AndroidView:</br>
Пример: [Widgets.kt](app/src/main/java/com/artofmainstreams/examples/ui/Widgets.kt)</br>
```kotlin
@Composable
fun CustomView() {
    var selectedItem by remember { mutableStateOf(0) }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        // создание
        factory = { context ->
            MyView(context).apply {
                setOnClickListener { selectedItem = 1 }
            }
        },
        // рекомпозиция 
        update = { view ->
            view.selectedItem - selectedItem
        }
    )
}
```

ViewBinding:</br>
Можно использовать binding для разметки. Пример: [Widgets.kt](app/src/main/java/com/artofmainstreams/examples/ui/Widgets.kt)</br>
```groovy
implementation 'androidx.compose.ui:ui-viewbinding:1.4.3'
```
```kotlin
@Composable
fun AndroidViewBindingExample() {
    AndroidViewBinding(Fragment01Binding::inflate) {
        buttonNext.setText("Some text")
        composeView.setContent { Hello(name = "Compose") }
    }
}
```
Можно использовать и для фрагментов. Пример: [Fragment02.kt](app/src/main/java/com/artofmainstreams/examples/ui/example02/Fragment02.kt)</br>
```xml
<androidx.fragment.app.FragmentContainerView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/fragment_container_view"
    android:name="com.artofmainstreams.examples.ui.example02.Fragment02"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
```kotlin
@Composable
fun Start() {
    AndroidViewBinding(FragmentContainerBinding::inflate) {
        val myFragment = fragmentContainerView.getFragment<Fragment02>()
    // ...
    }
}
```

View в Lazy-списках:</br>
```kotlin
@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun AndroidViewInLazyList() {
    LazyColumn {
        items(100) { index ->
            AndroidView(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(), 
                factory = { context ->
                    MyView(context)
                },
                update = { view ->
                    view.selectedItem = index
                },
                // компонент переиспользуется
                onReset = { view ->
                    view.clear()
                },
                // компонент больше не будет использоваться
                onRelease = {view ->
                    view.exit()
                }
            )
        }
    }
}
```

CompositionLocal:</br>
Предоставляет неявно данные. Есть смысл использовать для сквозных параметров, о которых не нужно знать. 
Можно получить `LocalContext`, `LocalConfiguration` и `LocalView`.</br>
Пример: [CompositionLocalExample.kt](app/src/main/java/com/artofmainstreams/examples/ui/CompositionLocalExample.kt)</br>
```kotlin
@Composable
fun ToastGreetingButton(greeting: String) {
    val context = LocalContext.current
    Button(onClick = {
        Toast.makeText(context, greeting, Toast.LENGTH_SHORT).show()
    }) {
        Text("Greet")
    }
}
```

### Ограничения

+ Версия компилятора должна соответствовать версии котлина
+ Зависит от корутин, хотя и есть адаптеры для rxjava2/3, livedata
+ UDF (unidirectional data flow) рекомендуется (MVICore, MVIKotlin и т.д.)

### Стратегии миграции
+ Новые фичи на компоуз
+ Переиспользуемые компоненты достаём и выносим в библиотеку
+ Заменяем существующие фичи по одному экрану за раз

Для чего:
+ Ускоряет скорость сборки
  + убрали DataBinding + Epoxy (kapt)
+ Уменьшается размер apk
  + убрали библиотеку AppCompat
+ Лучшая производительность на старте
  + убрали inflate
  + compose тащит профиль, в новых версиях андроида позволяет прекомпилировать код
+ Игра в долгую

Подход снизу вверх:
+ Добавляем ComposeView в UI-иерархию (снизу вверх)
+ Убираем фрагменты и делаем навигацию через Navigation component

Подход сверху-вниз
+ Можно обернуть в AndroidView и дождаться разработчика, который всё перепишет

В промежуточном состоянии (view in compose or compose in view): 
+ Данные лучше инкапсулировать во ViewModel
+ Если не хотим, то поближе к корневому
+ Если View - root, то данные обернуть в mutableStateOf

Архитектура. Миграция (примеры кода есть в [видео](https://www.youtube.com/live/HtK-Jp0Hwxg?feature=share&t=2186))
#### MVVM -> MVI
SingleState from LiveData:
+ Публичные методы VM с логикой превращаем в Action
+ Методы навигации превращаем в подклассы sealed interface OneTimeEvent
+ State -> immutable state
+ Все поля State изменяются через copy()
+ UI не должен напрямую менять State через VM, получает его целиком

Multiple LiveData:
+ Если несвязанные потоки, пробуем разделить на отдельные mvi-features
+ Или объединить в один

#### MVP -> MVI
+ Публичные методы Presenter с логикой превращаем в Action
+ Методы навигации превращаем в подклассы sealed interface OneTimeEvent
+ Методы (действия) из интерфейса view превращаем в Action
+ Создаём неизменяемый класс State 
+ Оставшиеся методы из интерфейса view превращаем в Action
+ Все поля State изменяются через copy()
+ Очистить UI от синхронизации Presenter (attach/detach/etc)


### Дополнительные кейсы

+ Для тем (appcompat, material, material3) есть адаптеры (accompanist)
+ Для разных размеров экранов есть [material3-window-size-class](https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes) вместо квалификаторов 
+ Для вложенных списков при переходе есть [решение](https://developer.android.com/jetpack/compose/touch-input/pointer-input/gestures#nested-scrolling-interop) для правильного скролла

## Links
+ [Курс от Android Academy, лекции](https://www.youtube.com/playlist?list=PLjLCGE4bVpHAGx8tW7aMx0q0RYH0HBVun)
+ [Курс от Android Academy, семинары](https://www.youtube.com/playlist?list=PLjLCGE4bVpHC9jREt1l-9MnbRMgdkNswk)
+ [Compose in views](https://developer.android.com/jetpack/compose/migrate/interoperability-apis/compose-in-views)
+ [Views in compose](https://developer.android.com/jetpack/compose/migrate/interoperability-apis/views-in-compose)
+ [Если котлин не новый, а compose хочется поновее](https://androidx.dev/storage/compose-compiler/repository)
+ [CodeLabs](https://developer.android.com/codelabs/jetpack-compose-migration)
+ [Пример перехода на Compose](https://github.com/phansier/AFProject)
