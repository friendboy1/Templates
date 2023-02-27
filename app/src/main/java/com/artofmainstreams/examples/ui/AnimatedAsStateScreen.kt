package com.artofmainstreams.examples.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Preview
@Composable
fun AnimatedAsStatePreview(
    animationDuration: Int = 3000
) {
    Column {
        var isVisible by remember { mutableStateOf(false) }
        Button(modifier = Modifier.fillMaxWidth(), onClick = { isVisible = !isVisible }) {
            Text(text = "+")
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            AnimationAsState(isVisible)
            Spacer(modifier = Modifier.height(32.dp))
            AnimationSquareAsState(isVisible, animationDuration)
            Spacer(modifier = Modifier.height(32.dp))
            AnimationSquareByUpdateTransition(isVisible, animationDuration)
            Spacer(modifier = Modifier.height(32.dp))
            AnimationSquareByEncapsulatedUpdateTransition(isVisible, animationDuration)
            Spacer(modifier = Modifier.height(32.dp))
            AnimationSquareInfiniteTransition(animationDuration)
        }
    }
}

@Composable
private fun AnimationAsState(
    visible: Boolean,
    animationDuration: Int = 500,
) {
    val alpha: Float by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(animationDuration)
    )
    val char: Char by animateCharAsState(targetValue = if (visible) 'a' else 'A')
    Text(
        modifier = Modifier.alpha(alpha),
        text = "Hello Android: $char",
        style = MaterialTheme.typography.h5
    )
}

@Composable
private fun AnimationSquareAsState(
    isVisible: Boolean = false,
    animationDuration: Int = 500,
) {
    val color by animateColorAsState(
        targetValue = if (isVisible) Color.Red else Color.Blue,
        animationSpec = tween(animationDuration, easing = LinearEasing)
    )
    val size by animateDpAsState(
        targetValue = if (isVisible) 60.dp else 150.dp,
        animationSpec = tween(animationDuration, easing = LinearEasing)
    )
    val rotation by animateFloatAsState(
        targetValue = if (isVisible) 0f else 180f,
        animationSpec = tween(animationDuration, easing = LinearEasing)
    )
    AnimatedSquare(color = color, rotation = rotation, size = size)
}

@Composable
private fun AnimationSquareByUpdateTransition(
    isVisible: Boolean = false,
    animationDuration: Int = 500,
) {
    val transition = updateTransition(
        targetState = isVisible, label = "Parent"
    )
    val color by transition.animateColor(
        transitionSpec = { tween(animationDuration, easing = LinearEasing) },
        label = "Animate Color"
    ) {
        if (it) Color.Red else Color.Blue
    }
    val size by transition.animateDp(
        transitionSpec = { tween(animationDuration, easing = LinearEasing) }, label = "Animate Size"
    ) {
        if (it) 60.dp else 150.dp
    }
    val rotation by transition.animateFloat(
        transitionSpec = { tween(animationDuration, easing = LinearEasing) },
        label = "Animate Rotation"
    ) {
        if (it) 0f else 180f
    }
    AnimatedSquare(color = color, rotation = rotation, size = size)
}

@Composable
private fun AnimationSquareByEncapsulatedUpdateTransition(
    isVisible: Boolean = false,
    animationDuration: Int = 500,
) {
    val transition = updateTransitionData(isVisible, animationDuration)
    AnimatedSquare(
        color = transition.color.value,
        rotation = transition.rotation.value,
        size = transition.size.value
    )
}

@Composable
private fun AnimationSquareInfiniteTransition(
    animationDuration: Int = 500,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = Color.Blue,
        targetValue = Color.Red,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                Color.Blue at 500
                Color.Yellow at 700
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                40f at 500
                100f at 700
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    val size by infiniteTransition.animateValue(
        initialValue = 60.dp,
        targetValue = 150.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration),
            repeatMode = RepeatMode.Reverse
        )
    )
    AnimatedSquare(
        color = color,
        rotation = rotation,
        size = size
    )
}

private data class AnimState(
    val rotation: State<Float>,
    val size: State<Dp>,
    val color: State<Color>
)

@Composable
private fun updateTransitionData(
    isVisible: Boolean,
    animationDuration: Int
): AnimState {
    val transition = updateTransition(
        targetState = isVisible, label = "Parent"
    )
    val color = transition.animateColor(
        transitionSpec = { tween(animationDuration, easing = LinearEasing) },
        label = "Animate Color"
    ) {
        if (it) Color.Red else Color.Blue
    }
    val size = transition.animateDp(
        transitionSpec = { tween(animationDuration, easing = LinearEasing) }, label = "Animate Size"
    ) {
        if (it) 60.dp else 150.dp
    }
    val rotation = transition.animateFloat(
        transitionSpec = { tween(animationDuration, easing = LinearEasing) },
        label = "Animate Rotation"
    ) {
        if (it) 0f else 180f
    }
    return remember(transition) { AnimState(rotation = rotation, size = size, color = color) }
}

/**
 * Кастомное свойство
 */
@Composable
private fun animateCharAsState(
    targetValue: Char,
    animationSpec: AnimationSpec<Char> = tween(5000),
    finishedListener: ((Char) -> Unit)? = null
): State<Char> {
    return animateValueAsState(
        targetValue = targetValue,
        typeConverter = charConverter(),
        animationSpec = animationSpec,
        finishedListener = finishedListener
    )
}

private fun charConverter() = TwoWayConverter<Char, AnimationVector1D>(
    convertToVector = { AnimationVector1D(it.code.toFloat()) },
    convertFromVector = { it.value.roundToInt().toChar() }
)

/**
 * Анимируем сразу и цвет, и размер, и поворот
 */
@Composable
private fun AnimatedSquare(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    rotation: Float = 0f,
    size: Dp = 60.dp,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer { rotationY = rotation }, contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .rotate(rotation)
                .background(color)
        )
    }
}