import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.artofmainstreams.examples.Main
import com.artofmainstreams.examples.loginGraph
import com.artofmainstreams.examples.mainScreen
import com.artofmainstreams.examples.movieDetailsScreen
import com.artofmainstreams.examples.navigateToMain
import com.artofmainstreams.examples.navigateToMovieDetails
import com.google.accompanist.navigation.animation.AnimatedNavHost
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
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
    ) {
        mainScreen(onNext = {
            navController.navigateToMovieDetails(movieId = 123L, title = "123/123")
        })
        movieDetailsScreen(onNext = navController::navigateToMain)
        loginGraph()
    }
}

fun NavController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(                // почистить стек до экрана main
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
            inclusive = false   // не включая указанный экран
        }
        launchSingleTop = true  // если destination уже лежит наверху, то не создавать такой же
        restoreState = true
    }.also { readBackStack(this) }

fun NavHostController.navigateUp(route: String) =
    this.popBackStack(
        route = route,
        inclusive = true // этот экран тоже убираем из стека
    )

/**
 * Чтение данных стека
 */
fun readBackStack(navController: NavController) {
    // стек в версии 2.6.0 private
    //val backQueue: ArrayDeque<NavBackStackEntry> = navController.backQueue
    // flow текущего entry с верха стека
    val currentBackStackEntryFlow: Flow<NavBackStackEntry> = navController.currentBackStackEntryFlow
    // state текущего entry с верха стека (вариант для compose)
    val currentBackStackEntry: NavBackStackEntry? = navController.currentBackStackEntry
    val listInfo = StringBuilder()
//    backQueue.forEach {
//        listInfo.append("${it.destination.route} ")
//    }
//    Log.i("Stack", "size: ${backQueue.size} list: $listInfo}")
}