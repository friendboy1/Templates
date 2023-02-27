package com.artofmainstreams.examples.ui

import androidx.compose.animation.core.*
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.material.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun UpdateTransitionPreview() {
    UpdateTransition()
}

/**
 * Допустим, у нас есть несколько состояний
 */
private enum class Hobby {
    BRUSH,
    COLOR_LENS,
    BIKE,
    SCOOTER
}

/**
 * Расширение для вычисления смещения человечка (чтоб по центру иконки был)
 */
private fun IntOffset.toPersonOffset(
    iconSize: Int,
    personSize: Int
): IntOffset {
    return copy(
        x = x + (iconSize - personSize) / 2,
        y = y + (iconSize - personSize) / 2
    )
}

@Composable
fun UpdateTransition() {
    var currentState by remember { mutableStateOf(Hobby.BRUSH) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // управляющие кнопки
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(onClick = { currentState = Hobby.BRUSH }) {
                Icon(imageVector = Icons.Filled.Brush, contentDescription = null)
            }
            IconButton(onClick = { currentState = Hobby.COLOR_LENS }) {
                Icon(imageVector = Icons.Filled.ColorLens, contentDescription = null)
            }
            IconButton(onClick = { currentState = Hobby.BIKE }) {
                Icon(imageVector = Icons.Filled.PedalBike, contentDescription = null)
            }
            IconButton(onClick = { currentState = Hobby.SCOOTER }) {
                Icon(imageVector = Icons.Filled.ElectricScooter, contentDescription = null)
            }
        }

        // передаём enum для определения состояний
        val transition = updateTransition(
            targetState = currentState,
            label = "Parent Transition",
        )
        SituationWidget(
            transition = transition
        )
    }
}

@Composable
private fun SituationWidget(
    canvasSize: Int = 300,
    iconSize: Int = 70,
    personSize: Int = 30,
    transition: Transition<Hobby>,
) {
    // вычисляем расположение иконок
    val brushOffset = IntOffset(
        x = 0,
        y = 0
    )
    val colorLensOffset = IntOffset(
        x = canvasSize - iconSize,
        y = 0
    )
    val bikeOffset = IntOffset(
        x = canvasSize - iconSize,
        y = canvasSize - iconSize
    )
    val scooterOffset = IntOffset(
        x = 0,
        y = canvasSize - iconSize
    )

    // в зависимости от состояния, настраиваем анимацию
    val personOffset by transition.animateIntOffset(
        label = "Position Animation",
        transitionSpec = {
            when {
                Hobby.BRUSH isTransitioningTo Hobby.BIKE -> tween(5000)
                Hobby.BIKE isTransitioningTo Hobby.BRUSH -> tween(5000)
                Hobby.COLOR_LENS isTransitioningTo Hobby.SCOOTER -> tween(5000)
                Hobby.SCOOTER isTransitioningTo Hobby.COLOR_LENS -> tween(5000)
                else -> spring(dampingRatio = DampingRatioMediumBouncy, stiffness = StiffnessVeryLow)
            }
        }
    ) {
        when (it) {
            Hobby.BRUSH -> brushOffset
            Hobby.COLOR_LENS -> colorLensOffset
            Hobby.BIKE -> bikeOffset
            Hobby.SCOOTER -> scooterOffset
        }.toPersonOffset(iconSize, personSize)
    }

    Location(
        canvasSize = canvasSize,
        smallSize = iconSize,
        personSize = personSize,
        brushOffset = brushOffset,
        colorLensOffset = colorLensOffset,
        bikeOffset = bikeOffset,
        scooterOffset = scooterOffset,
        personOffset = personOffset
    )
}

@Composable
private fun Location(
    canvasSize: Int,
    smallSize: Int,
    personSize: Int,
    brushOffset: IntOffset,
    colorLensOffset: IntOffset,
    bikeOffset: IntOffset,
    scooterOffset: IntOffset,
    personOffset: IntOffset
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(32.dp)
            .size(canvasSize.dp),
    ) {
        CardIcon(
            offset = brushOffset,
            size = smallSize,
            imageVector = Icons.Filled.Brush,
            tint = Color.Cyan
        )

        CardIcon(
            offset = colorLensOffset,
            size = smallSize,
            imageVector = Icons.Filled.ColorLens,
            tint = Color.Red
        )
        CardIcon(
            offset = bikeOffset,
            size = smallSize,
            imageVector = Icons.Filled.PedalBike,
            tint = Color.Green
        )
        CardIcon(
            offset = scooterOffset,
            size = smallSize,
            imageVector = Icons.Filled.ElectricScooter,
            tint = Color.Yellow
        )
        CardIcon(
            offset = personOffset,
            size = personSize,
            padding = 2.dp,
            imageVector = Icons.Default.Person,
            background = Color(0xFFEAD9F0)
        )
    }
}

/**
 *
 */
@Composable
private fun CardIcon(
    offset: IntOffset,
    size: Int,
    padding: Dp = 8.dp,
    imageVector: ImageVector,
    tint: Color = Color.Black,
    background: Color = Color(0xFF0B7CA0)
) {
    Card(
        modifier = Modifier
            .offset(
                x = offset.x.dp,
                y = offset.y.dp,
            )
            .size(size.dp),
        backgroundColor = background
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentDescription = null,
            tint = tint,
            imageVector = imageVector
        )
    }
}
