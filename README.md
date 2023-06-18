# Description
Jetpack Compose. Навигация

## Navigation
+ [up](../jetpack_compose)
+ [root](../master)
 
## Details
### Подключение
```groovy
implementation 'androidx.navigation:navigation-compose'
// для связки viewModel через DI
implementation 'androidx.hilt:hilt-navigation-compose:1.0.0'
```

### Инициализация
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavGraph(
                navController = navController,
                modifier = Modifier
            )
        }
    }
}
```
```kotlin
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "firstScreen",
        modifier = modifier
    ) {
        composable(route = "firstScreen") {
            FirstScreen(
                onClick = {
                     navController.navigate("secondScreen")
                }
            )
        }
        composable(route = "secondScreen") {
            SecondScreen(
                onClick = {
                     navController.navigate("firstScreen")
                }
            )
        }
        dialog(route = "someDialog") { 
            // ... 
        }
        navigation(startDestination = "firstScreen", route = "someFeatureScenario") {
            composable(route = "firstScreen") { 
                //... 
            }
        }
    }
}
```
### navigate
```kotlin
// вперёд
navController.navigate("settings") {
    launchSingleTop = true // если destination уже лежит наверху, то не создавать такой же
    popUpTo("main") { // почистить стек до экрана main
        inclusive = true // включая экран main
    }
}
// назад
navController.popBackStack(
    route = "main", // возвращаемся до этого экрана 
    inclusive = true // этот экран тоже убираем из стека
)
```
### read state
```kotlin
// стек
val backQueue: ArrayDeque<NavBackStackEntry> = navController.backQueue
// flow текущего entry с верха стека
val currentBackStackEntryFlow: Flow<NavBackStackEntry> = navController.currentBackStackEntryFlow
// state текущего entry с верха стека (вариант для compose)
val currentBackStackEntry: NavBackStackEntry? = navController.currentBackStackEntry
```

### arguments
```kotlin
/**
 * @param movieId обязательный параметр
 * @param title необязательный параметр
 */
fun navigateToDetails(movieId: Long, title: String? = null) {
    navController.navigate("details/$movieId?title={$title}")
}
```
```kotlin
fun NavGraphBuilder.movieDetailsScreen(onNext: () -> Unit) {
    composable(
        route = "details/{movieId}/title={title}",
        arguments = listOf(
            navArgument("movieId") { type = NavType.LongType },
            navArgument("title") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getLong("movieId") ?: throw IllegalStateException()
        val title = backStackEntry.arguments?.getString("title").orEmpty()
        ScreenWithOneButtonDestination(
            text = "to main",
            onClickNext = onNext
        )
    }
}
```
Обрати внимание:
+ Слеш в аргументы строки не передавай. Если нужно, то можно через `Uri.encode("123/123")`, а затем через `Uri.decode("123%2F123")`
+ Пустая строка в аргументе. Не делай так
+ Большие данные не передавай
  + но можно обойти через превращение в json и затем в строку (не рекомендуется)
  + использовать библиотеку compose-destinations
  + `NavBackStackEntry.savedStateHandle`
    ```kotlin
    navigate("details")
    currentBackStackEntry?.let {
        it.savedStateHandle["title"] = title
        it.savedStateHandle["movieId"] = movieId
    }
    ```
    ```kotlin
    val movieId = backStackEntry.savedStateHandle.getLiveData<Long>("movieId").observeAsState().value
    val title = backStackEntry.savedStateHandle.get<String>("title").orEmpty()
    ```

### nested Navigation
```kotlin
fun NavGraphBuilder.loginGraph() { 
    navigation(
        startDestination = "username", 
        route = "login/{id}",
        arguments = listOf(navArgument("id") { type = NavType.StringType })) {
            composable(route = "username") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
            }
            composable(route = "password") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") // так нельзя
            }
            composable(route = "registration") {}
        }
}
```
```kotlin
navController.navigate("login")
```

### animation
Поможет библиотека navigation-animation из [accompanist](https://github.com/google/accompanist/tree/main/navigation-animation)
```groovy
implementation "com.google.accompanist:accompanist-navigation-animation:<version>"
```
```kotlin
// было
// стало
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

rememberNavController() 
rememberAnimatedNavController()

NavHost()
AnimatedNavHost()
```
```kotlin
/**
 * Анимация для всех экранов приложения 
 */
AnimatedNavHost(
        navController = navController,
        startDestination = Main.route,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        }
    ) { /*...*/ }

/**
 * Анимация для конкретного экрана
 */
composable("details/{id}",
  enterTransition = {
    if (initialState.destination.route == "") {
      slideInHorizontally(initialOffsetX = { 800 })
    } else {
      slideInVertically(initialOffsetY = { 1800 })
    }
  }) { /*...*/ }
```

### Deeplink 
Работает из коробки только если нет каких-нибудь экранов авторизации
```kotlin
composable(
        //...
        deepLinks = listOf(navDeepLink { uriPattern = "myapp://some/feature" }),
    )
```
```kotlin
/**
 * Если launchMode нестандартный
 */
override fun onNewIntent(intent: Intent?) {
  super.onNewIntent(intent)
  navController.handleDeepLink(intent)
}
```

## ToDo
+ Добавить DI через Hilt

## Links
+ [Курс от Android Academy, лекции](https://www.youtube.com/playlist?list=PLjLCGE4bVpHAGx8tW7aMx0q0RYH0HBVun)
+ [Курс от Android Academy, семинары](https://www.youtube.com/playlist?list=PLjLCGE4bVpHC9jREt1l-9MnbRMgdkNswk)
+ [navigation-animation](https://github.com/google/accompanist/tree/main/navigation-animation)
