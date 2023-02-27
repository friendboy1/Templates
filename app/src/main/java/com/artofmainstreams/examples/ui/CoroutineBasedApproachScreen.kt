package com.artofmainstreams.examples.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Preview
@Composable
fun CoroutineBasedApproachPreview() {
    CoroutineBasedApproach()
}

@Composable
fun CoroutineBasedApproach(
    onFinished: () -> Unit = {}
) {
    val size = remember { Animatable(0.dp, Dp.VectorConverter) }
    val verticalPosition = remember { Animatable(0.dp, Dp.VectorConverter) }

    LaunchedEffect(size, verticalPosition) {
        launch {
            size.animateTo(300.dp)
            val springSpec: SpringSpec<Dp> = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
            repeat(3) {
                size.animateTo(250.dp, springSpec)
                size.animateTo(300.dp, springSpec)
            }
            coroutineScope {
                launch { size.animateTo(0.dp, tween(800)) }
                launch { verticalPosition.animateTo((-500).dp, tween(800)) }
            }
            size.snapTo(0.dp)
            verticalPosition.snapTo(0.dp)
            onFinished()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(size.value)
                .offset(y = verticalPosition.value),
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Color.Red
        )
    }
}