package com.artofmainstreams.examples.ui

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.random.Random

@Preview
@Composable
fun AnimatablePreview() {
    InfiniteLineAnimation()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.swipeUpToChangeColor(Color.Cyan).size(64.dp))
    }
}

@Composable
fun InfiniteLineAnimation(
    modifier: Modifier = Modifier
) {
    val animatable = remember { Animatable(0.2f) }
    LaunchedEffect(Unit) {
        while (true) {
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(1000)
            )
            animatable.animateTo(
                targetValue = 0f,
                animationSpec = tween(1000)
            )
            animatable.snapTo(0.2f)
        }
    }
    AnimationLineString(
        modifier = modifier,
        fraction = animatable.value,
    )
}


@Composable
fun AnimationLineString(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0) fraction: Float,
    text: String? = null,
    pinColor: Color = Color(0xFF0920B9),
    lineColor: Color = Color(0xFF3D0D44)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        if (text != null) {
            Text(
                modifier = Modifier
                    .width(174.dp)
                    .padding(start = 4.dp),
                text = text,
                color = lineColor,
                style = MaterialTheme.typography.caption
            )
        }
        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            val startOffset = 16.dp
            Spacer(
                modifier = Modifier
                    .padding(horizontal = startOffset)
                    .fillMaxWidth()
                    .height(1.dp)
                    .alpha(0.2f)
                    .background(lineColor)
            )
            val pinSize = 16.dp
            Card(
                modifier = Modifier
                    .size(pinSize)
                    .offset(x = startOffset + (maxWidth - startOffset * 2) * fraction - pinSize / 2),
                shape = CircleShape,
                backgroundColor = pinColor
            ) {}
        }
    }
}


private fun Modifier.swipeUpToChangeColor(initColor: Color = Color.White): Modifier = composed {
    val offsetY = remember { Animatable(0f) }
    val color = remember { androidx.compose.animation.Animatable(initColor) }
    pointerInput(Unit) {
        // Используется для расчета положения установки анимации броска
        val decay = splineBasedDecay<Float>(this)
        offsetY.updateBounds(
            lowerBound = -size.height.toFloat(),
            upperBound = size.height.toFloat()
        )
        coroutineScope {
            while (true) {
                // ждём, пока палец коснётся
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                offsetY.stop()  // останавливаем любую анимацию, которая была до этого момента
                color.stop()
                // для замера скорости броска
                val velocityTracker = VelocityTracker()
                // ожидаем пермещения
                awaitPointerEventScope {
                    verticalDrag(pointerId) { change ->
                        val verticalDragOffset = offsetY.value + change.positionChange().y
                        launch {
                            offsetY.snapTo(verticalDragOffset)
                        }
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }
                val velocity = velocityTracker.calculateVelocity().y
                val targetOffsetY = decay.calculateTargetValue(offsetY.value, velocity)

                launch {
                    if (targetOffsetY.absoluteValue <= size.width) {
                        // обратно, скорости не хватает
                        offsetY.animateTo(targetValue = 0f, initialVelocity = velocity)
                        color.animateTo(targetValue = initColor)
                    } else {
                        // анимируем
                        offsetY.animateDecay(velocity, decay)
                        color.animateTo(getRandomColor())
                        offsetY.snapTo(0f)
                    }
                }
            }
        }
    }.offset {
        IntOffset(0, offsetY.value.roundToInt())
    }.background(color = color.value)
}

fun getRandomColor() = Color(Random.nextLong(0xFF000000, 0xFFFFFFFF))