package com.artofmainstreams.examples.ui

import androidx.compose.animation.core.*
import androidx.compose.animation.core.Spring.DampingRatioHighBouncy
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.DampingRatioNoBouncy
import androidx.compose.animation.core.Spring.StiffnessHigh
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.Spring.StiffnessMedium
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AnimatedSpecPreview() {
    var isVisible by remember { mutableStateOf(false) }
    Column {
        Button(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth(fraction = 0.9f), onClick = { isVisible = !isVisible }) {
            Text(text = if (isVisible) "Сюда" else "Туда")
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            AnimatedSpec(isVisible)
        }
    }
}

@Composable
private fun AnimatedSpec(isVisible: Boolean = false) {
    val fraction = remember(isVisible) { if (isVisible) 1f else 0f }
    // Tween
    val duration = 2000
    val fastOutSlowInEasing by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(
            durationMillis = duration,
            easing = FastOutSlowInEasing
        )
    )
    val linearOutSlowInEasing by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(
            durationMillis = duration,
            easing = LinearOutSlowInEasing
        )
    )
    val fastOutLinearInEasing by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(
            durationMillis = duration,
            easing = FastOutLinearInEasing
        )
    )
    val linearEasing by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(
            durationMillis = duration,
            easing = LinearEasing
        )
    )
    // Spring
    val stiffnessVeryLow by animateFloatAsState(
        targetValue = fraction,
        animationSpec = spring(stiffness = StiffnessVeryLow)
    )
    val stiffnessLow by animateFloatAsState(
        targetValue = fraction,
        animationSpec = spring(stiffness = StiffnessLow)
    )
    val stiffnessMediumLow by animateFloatAsState(
        targetValue = fraction,
        animationSpec = spring(stiffness = StiffnessMediumLow)
    )
    val stiffnessMedium by animateFloatAsState(
        targetValue = fraction,
        animationSpec = spring(stiffness = StiffnessMedium)
    )
    val stiffnessHigh by animateFloatAsState(
        targetValue = fraction,
        animationSpec = spring(stiffness = StiffnessHigh)
    )
    val dampingRatioNoBouncy by animateFloatAsState(
        targetValue = fraction,
        animationSpec = spring(
            stiffness = StiffnessVeryLow,
            dampingRatio = DampingRatioNoBouncy
        )
    )
    val dampingRatioLowBouncy by animateFloatAsState(
        targetValue = fraction,
        animationSpec = spring(
            stiffness = StiffnessVeryLow,
            dampingRatio = DampingRatioLowBouncy
        )
    )
    val dampingRatioMediumBouncy by animateFloatAsState(
        targetValue = fraction,
        animationSpec = spring(
            stiffness = StiffnessVeryLow,
            dampingRatio = DampingRatioMediumBouncy
        )
    )
    val dampingRatioHighBouncy by animateFloatAsState(
        targetValue = fraction,
        animationSpec = spring(
            stiffness = StiffnessVeryLow,
            dampingRatio = DampingRatioHighBouncy
        )
    )
    // Repeatable
    val repeatable by animateFloatAsState(
        targetValue = fraction,
        animationSpec = repeatable(iterations = 3, animation = tween(duration))
    )
    val infiniteRepeatable by animateFloatAsState(
        targetValue = fraction,
        animationSpec = infiniteRepeatable(
            animation = tween(duration),
            repeatMode = RepeatMode.Reverse
        )
    )
    // Snap
    val snap by animateFloatAsState(
        targetValue = fraction,
        animationSpec = snap(duration)
    )
    // KeyFrames
    val keyFrames by animateFloatAsState(
        targetValue = fraction,
        animationSpec = keyframes {
            durationMillis = duration
            0.0f atFraction 0f with LinearOutSlowInEasing
            0.2f atFraction 0.25f with FastOutLinearInEasing
            0.4f atFraction 0.5f
            0.4f atFraction 0.7f
        }
    )
    Column {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "tween",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        AnimationLineString(text = "fastOutSlowInEasing", fraction = fastOutSlowInEasing)
        AnimationLineString(text = "linearOutSlowInEasing", fraction = linearOutSlowInEasing)
        AnimationLineString(text = "fastOutLinearInEasing", fraction = fastOutLinearInEasing)
        AnimationLineString(text = "linearEasing", fraction = linearEasing)
        Text(
            modifier = Modifier.padding(8.dp),
            text = "spring",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        AnimationLineString(text = "stiffnessVeryLow", fraction = stiffnessVeryLow)
        AnimationLineString(text = "stiffnessLow", fraction = stiffnessLow)
        AnimationLineString(text = "stiffnessMediumLow", fraction = stiffnessMediumLow)
        AnimationLineString(text = "stiffnessMedium", fraction = stiffnessMedium)
        AnimationLineString(text = "stiffnessHigh", fraction = stiffnessHigh)
        Spacer(modifier = Modifier.width(32.dp))
        AnimationLineString(text = "dampingRatioNoBouncy", fraction = dampingRatioNoBouncy)
        AnimationLineString(text = "dampingRatioLowBouncy", fraction = dampingRatioLowBouncy)
        AnimationLineString(text = "dampingRatioMediumBouncy", fraction = dampingRatioMediumBouncy)
        AnimationLineString(text = "dampingRatioHighBouncy", fraction = dampingRatioHighBouncy)
        Text(
            modifier = Modifier.padding(8.dp),
            text = "repeat",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        AnimationLineString(text = "repeatable", fraction = repeatable)
        AnimationLineString(text = "infiniteRepeatable", fraction = infiniteRepeatable)
        Text(
            modifier = Modifier.padding(8.dp),
            text = "snap",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        AnimationLineString(text = "snap", fraction = snap)
        Text(
            modifier = Modifier.padding(8.dp),
            text = "keyFrames",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        AnimationLineString(text = "keyFrames", fraction = keyFrames)
    }
}