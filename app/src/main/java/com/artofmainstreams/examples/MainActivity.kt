package com.artofmainstreams.examples

import AppNavGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * Пример навигации
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //val navController = rememberNavController()
            val navController = rememberAnimatedNavController()
            AppNavGraph(
                navController = navController,
                modifier = Modifier
            )
        }
    }
}


