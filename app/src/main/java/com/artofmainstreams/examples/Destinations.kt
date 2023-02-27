package com.artofmainstreams.examples

import android.net.Uri
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.artofmainstreams.examples.ui.ScreenWithOneButton
import com.artofmainstreams.examples.ui.ScreenWithOneButtonDestination
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import navigateSingleTopTo

// region Подход от Гугла
interface AppDestination {
    val icon: ImageVector
    val route: String
}

object Main : AppDestination {
    override val icon = Icons.Filled.PieChart
    override val route = "main"
}

object MovieDetails : AppDestination {
    override val icon = Icons.Filled.AttachMoney
    override val route = "movie_details"
    const val movieIdKey = "movieId"
    const val titleKey = "title"
    val routeWithArgs = "${route}/{${movieIdKey}}?title={$titleKey}"
}

object Bills : AppDestination {
    override val icon = Icons.Filled.MoneyOff
    override val route = "bills"
}

object SingleAccount : AppDestination {
    override val icon = Icons.Filled.Money
    override val route = "single_account"
    const val accountTypeArg = "account_type"
    val routeWithArgs = "${route}/{${accountTypeArg}}"
    val deepLinks = listOf(
        navDeepLink { uriPattern = "myapp://$route/{$accountTypeArg}" }
    )
    val arguments = listOf(
        navArgument(accountTypeArg) { type = NavType.StringType }
    )
}

// Screens to be displayed in the top RallyTabRow
val appTabRowScreens = listOf(Main, MovieDetails, Bills)
// endregion

// region Extensions для навигации, лучше в отдельных файлах для каждого экрана

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.mainScreen(onNext: () -> Unit) {
    composable(Main.route,
        enterTransition = {
            if (initialState.destination.route == "") {
                slideInHorizontally(initialOffsetX = { 800 })
            } else {
                slideInVertically(initialOffsetY = { 1800 })
            }
        }) {
        // TODO ScreenWithOneButtonDestination for Hilt
        ScreenWithOneButton(
            text = MovieDetails.route,
            onClickNext = onNext
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.movieDetailsScreen(onNext: () -> Unit) {
    composable(
        route = MovieDetails.routeWithArgs,
        arguments = listOf(
            navArgument(MovieDetails.movieIdKey) { type = NavType.LongType },
            navArgument(MovieDetails.titleKey) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        ),
        deepLinks = listOf(navDeepLink { uriPattern = "myapp://${MovieDetails.routeWithArgs}" }),
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getLong(MovieDetails.movieIdKey)
            ?: throw IllegalStateException()
        val title = backStackEntry.arguments?.getString(MovieDetails.titleKey).orEmpty()
        Log.i("Arguments", "$id $title")
        //TODO ScreenWithOneButtonDestination for Hilt
        ScreenWithOneButton(
            text = Main.route,
            onClickNext = onNext
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.loginGraph() {
    navigation(startDestination = "username", route = "login/{id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
            }
        )) {
        composable(route = "username") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
        }
        composable(route = "password") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") // так нельзя
        }
        composable(route = "registration") {}
    }
}

fun NavController.navigateToMain() {
    navigateSingleTopTo(Main.route)
}

fun NavController.navigateToMovieDetails(movieId: Long, title: String? = null) {
    val titleEncoded = Uri.encode(title)
    navigateSingleTopTo("${MovieDetails.route}/$movieId?title={$titleEncoded}")
}

fun NavController.navigateToLoginGraph(id: String) {
    navigate("login/$id")
}

// endregion