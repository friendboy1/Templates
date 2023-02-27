# Description
Jetpack Compose - библиотека для декларативного описания UI

## Navigation
+ [up](../jetpack)
+ [root](../master)
+ jetpack/compose/[basic](../jetpack_compose_basic) - основы compose
+ jetpack/compose/[animation](../jetpack_compose_animation) - анимация
+ jetpack/compose/[migration](../jetpack_compose_migration) - миграция
+ jetpack/compose/[navigation](../jetpack_compose_navigation) - навигация

## Details
Подключение:
+ через шаблон
+ добавлением зависимостей (с проверкой совместимости на сайте)
+ bill of materials (BOM)

```groovy
android {
    buildFeatures {
        viewBinding true
        compose true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
}
dependencies {
    // Jetpack Compose
    def composeBom = platform('androidx.compose:compose-bom:2023.01.00')
    implementation composeBom
    androidTestImplementation composeBom
    // Compose Material Design (можно выбрать другое)
    implementation 'androidx.compose.material:material'
    // Animations
    implementation 'androidx.compose.animation:animation'
    // Tooling support (Previews, etc.)
    debugImplementation 'androidx.compose.ui:ui-tooling'
    implementation 'androidx.compose.ui:ui-tooling-preview'
    // UI Tests
    androidTestImplementation 'androidx.compose.ui:ui-test-junit4'
    debugImplementation 'androidx.compose.ui:ui-test-manifest'
    // Optional - Integration with activities
    implementation 'androidx.activity:activity-compose'
    // Optional - Integration with ViewModels
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1'
}
```

Для подключения Compose в Activity, пишем зависимость в gradle и используем следующий код:
```kotlin
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ...
        }
    }
}
```

Для подключения Compose во фрагменте, используем следующий код:
```kotlin
class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                // ...
            }
        }
    }
}
```

## Links
+ [BOM](https://developer.android.com/jetpack/compose/bom/bom)
+ [Курс от Android Academy, лекции](https://www.youtube.com/playlist?list=PLjLCGE4bVpHAGx8tW7aMx0q0RYH0HBVun)
+ [Курс от Android Academy, семинары](https://www.youtube.com/playlist?list=PLjLCGE4bVpHC9jREt1l-9MnbRMgdkNswk)
+ [Проверка совместимости с версией котлина](https://developer.android.com/jetpack/androidx/releases/compose-kotlin)
+ [Подключение зависимостей](https://developer.android.com/jetpack/compose/setup#bom-version-mapping)
+ [Соответствие зависимостей в BOM](https://developer.android.com/jetpack/compose/bom/bom-mapping)
