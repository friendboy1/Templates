package com.artofmainstreams.examples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.artofmainstreams.examples.ui.*

/**
 * Если предполагается Activity на Compose, то заменить наследование на ComponentActivity
 * и раскомментить setContent
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AnimationsScreen(
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun AnimationsScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "init"
    ) {
        composable("init") { ListOfAnimations(navController = navController) }
        composable("1") { AnimatedVisibilityPreview() }
        composable("2") { AnimatedSpecPreview() }
        composable("3") { AnimatedContentPreview() }
        composable("4") { AnimatedAsStatePreview() }
        composable("5") { UpdateTransitionPreview() }
        composable("6") { UpdateTransitionSecondSamplePreview() }
        composable("7") { AnimatablePreview() }
        composable("8") { AnimationPreview() }
        composable("9") { CoroutineBasedApproachPreview() }
        composable("10") { CanvasAnimationPreview() }
    }
}

@Composable
fun ListOfAnimations(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        ScreenTitle(title = "AnimatedVisibilityPreview") { navController.navigate("1") }
        ScreenTitle(title = "AnimatedSpecPreview") { navController.navigate("2") }
        ScreenTitle(title = "AnimatedContentPreview") { navController.navigate("3") }
        ScreenTitle(title = "AnimatedAsStatePreview") { navController.navigate("4") }
        ScreenTitle(title = "UpdateTransitionPreview") { navController.navigate("5") }
        ScreenTitle(title = "UpdateTransitionSecondSamplePreview") { navController.navigate("6") }
        ScreenTitle(title = "AnimatedAnimatablePreview") { navController.navigate("7") }
        ScreenTitle(title = "AnimatedAnimationPreview") { navController.navigate("8") }
        ScreenTitle(title = "CoroutineBasedApproachPreview") { navController.navigate("9") }
        ScreenTitle(title = "CanvasAnimationPreview") { navController.navigate("10") }
    }
}

@Composable
private fun ScreenTitle(
    title: String,
    onClick: () -> Unit = {}
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        text = title,
        style = MaterialTheme.typography.body1
    )
}
