package com.artofmainstreams.examples.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AnimationPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimationPresentationSample(
            animation = ANIMATION
        )
        Spacer(modifier = Modifier.height(32.dp))
        AnimationPresentationSample(
            animation = DECAY_ANIMATION
        )
    }
}

private val ANIMATION: Animation<Dp, AnimationVector1D> = TargetBasedAnimation(
    animationSpec = tween(1000),
    typeConverter = Dp.VectorConverter,
    initialValue = 0.dp,
    targetValue = 100.dp
)

/**
 * Плавно угасает с какой-то начальной скоростью
 */
private val DECAY_ANIMATION: Animation<Dp, AnimationVector1D> = DecayAnimation(
    animationSpec = exponentialDecay(),
    typeConverter = Dp.VectorConverter,
    initialValue = 0.dp,
    initialVelocity = 500.dp
)

@Composable
private fun AnimationPresentationSample(
    modifier: Modifier = Modifier,
    animation: Animation<Dp, AnimationVector1D> = ANIMATION
) {
    var currentState by remember { mutableStateOf(0.dp) }
    var timeNanos by remember { mutableStateOf(0L) }
    var counter by remember { mutableStateOf(0) }

    LaunchedEffect(counter) {
        val startTime = withFrameNanos { it }
        do {
            // получаем время текущего кадра
            val animationTime = withFrameNanos { it } - startTime
            timeNanos = animationTime
            // получаем анимированное значение на основе времени
            currentState = animation.getValueFromNanos(animationTime)
        } while (!animation.isFinishedFromNanos(animationTime))
    }

    HeartWidget(
        modifier = modifier,
        currentState = currentState,
        timeMs = timeNanos / 1_000_000
    ) { counter++ }
}

@Composable
fun HeartWidget(
    modifier: Modifier = Modifier,
    maxSize: Dp = ANIMATION.targetValue,
    currentState: Dp,
    timeMs: Long? = null,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(maxSize),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(currentState)
                    .clickable(
                        remember { MutableInteractionSource() },
                        indication = null
                    ) { onClick() },
                imageVector = Icons.Default.Favorite,
                contentDescription = "Heart",
                tint = Color(0xFFFA04D1)
            )
        }
        Text(
            text = "${currentState.value} dp",
            style = MaterialTheme.typography.h5
        )
        timeMs?.let {
            Text(
                text = "$timeMs ms",
                style = MaterialTheme.typography.h5
            )
        }
    }
}