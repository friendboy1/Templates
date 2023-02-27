package com.artofmainstreams.examples.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.artofmainstreams.examples.R

@Preview
@Composable
fun AnimatedVisibilityPreview() {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        AnimationVisibleState()
        AnimationVisibleCustom()
        AnimationForChildrenFromModifier()
        AnimationFadeInOut()
        AnimationSlideInOut()
        AnimationSlideInOutHorizontallyVertically()
        AnimationExpandVerticallyInShrinkHorizontallyOut()
        AnimationExpandHorizontallyInShrinkVerticallyOut()
        AnimationExpandInShrinkOut()
        AnimationScaleInOut()
        AnimationFadeScaleInOut()
    }
}

@Composable
private fun AnimationVisibleState() {
    Column(modifier = Modifier.height(300.dp)) {
        val isVisible = remember { MutableTransitionState(true) }
        val color = when {
            // Appearing
            !isVisible.isIdle && !isVisible.currentState -> Color.Green
            // Appeared
            isVisible.isIdle && isVisible.currentState -> MaterialTheme.colors.primary
            // Hiding
            !isVisible.isIdle && isVisible.currentState -> Color.Yellow
            // Hidden
            isVisible.isIdle && !isVisible.currentState -> Color.Red
            else -> error("Illegal State $isVisible")
        }
        Button(
            onClick = { isVisible.targetState = !isVisible.targetState },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = color,
                contentColor = Color.White
            )
        ) {
            Text(text = "VisibleState")
        }
        AnimatedVisibility(
            visibleState = isVisible, enter = fadeIn(tween(2000)), exit = fadeOut(
                tween(2000)
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Фотография профиля"
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimationVisibleCustom() {
    Column(modifier = Modifier.height(300.dp)) {
        var isVisible by remember { mutableStateOf(true) }
        Button(
            onClick = { isVisible = !isVisible },
        ) {
            Text(text = "Custom")
        }
        AnimatedVisibility(visible = isVisible) {
            val background by transition.animateColor(label = "color") { state ->
                if (state == EnterExitState.Visible) Color.Yellow else Color.Green
            }
            Box(modifier = Modifier.size(128.dp).background(background))
        }
    }
}

@Composable
private fun AnimationFadeInOut() {
    Column(modifier = Modifier.height(300.dp)) {
        var isVisible by remember { mutableStateOf(false) }
        Button(onClick = { isVisible = !isVisible }) {
            Text(text = "fadeIn fadeOut")
        }
        AnimatedVisibility(visible = isVisible, enter = fadeIn(), exit = fadeOut()) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Фотография профиля"
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimationForChildrenFromModifier() {
    Column(modifier = Modifier.height(300.dp)) {
        var isVisible by remember { mutableStateOf(false) }
        Button(onClick = { isVisible = !isVisible }) {
            Text(text = "Children")
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = EnterTransition.None,
            exit = ExitTransition.None
        ) {
            Column {
                Text(
                    modifier = Modifier.animateEnterExit(
                        enter = expandHorizontally(expandFrom = Alignment.Start),
                        exit = shrinkHorizontally(shrinkTowards = Alignment.Start)
                    ), text = "Some text", style = MaterialTheme.typography.h5
                )
                Image(
                    modifier = Modifier
                        .animateEnterExit(
                            enter = scaleIn(),
                            exit = scaleOut()
                        ),
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Фотография профиля"
                )

            }
        }
    }
}

@Composable
private fun AnimationSlideInOut() {
    Column(modifier = Modifier.height(300.dp)) {
        var isVisible by remember { mutableStateOf(false) }
        Button(onClick = { isVisible = !isVisible }) {
            Text(text = "slideIn slideOut")
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = slideIn(initialOffset = { IntOffset(640, 640) }),
            exit = slideOut(targetOffset = { IntOffset(640, 640) })
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Фотография профиля"
            )
        }
    }
}

@Composable
private fun AnimationSlideInOutHorizontallyVertically() {
    Column(modifier = Modifier.height(300.dp)) {
        var isVisible by remember { mutableStateOf(false) }
        Button(onClick = { isVisible = !isVisible }) {
            Text(text = "slideInHorizontally slideOutVertically")
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(),
            exit = slideOutVertically()
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Фотография профиля"
            )
        }
    }
}

@Composable
private fun AnimationExpandVerticallyInShrinkHorizontallyOut() {
    Column(modifier = Modifier.height(300.dp)) {
        var isVisible by remember { mutableStateOf(false) }
        Button(onClick = { isVisible = !isVisible }) {
            Text(text = "expandVertically shrinkHorizontally")
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = expandVertically(),
            exit = shrinkHorizontally()
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Фотография профиля"
            )
        }
    }
}

@Composable
private fun AnimationExpandHorizontallyInShrinkVerticallyOut() {
    Column(modifier = Modifier.height(300.dp)) {
        var isVisible by remember { mutableStateOf(false) }
        Button(onClick = { isVisible = !isVisible }) {
            Text(text = "expandHorizontally shrinkVertically")
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = expandHorizontally(),
            exit = shrinkVertically()
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Фотография профиля"
            )
        }
    }
}

@Composable
private fun AnimationExpandInShrinkOut() {
    Column(modifier = Modifier.height(300.dp)) {
        var isVisible by remember { mutableStateOf(false) }
        Button(onClick = { isVisible = !isVisible }) {
            Text(text = "expandIn shrinkOut")
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = expandIn(),
            exit = shrinkOut()
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Фотография профиля"
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimationScaleInOut() {
    Column(modifier = Modifier.height(300.dp)) {
        var isVisible by remember { mutableStateOf(false) }
        Button(onClick = { isVisible = !isVisible }) {
            Text(text = "scaleIn scaleOut")
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Фотография профиля"
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimationFadeScaleInOut() {
    Column(modifier = Modifier.height(300.dp)) {
        var isVisible by remember { mutableStateOf(false) }
        Button(onClick = { isVisible = !isVisible }) {
            Text(text = "fadeIn() + scaleIn(), fadeOut() + scaleOut()")
        }
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Фотография профиля"
            )
        }
    }
}