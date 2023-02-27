package com.artofmainstreams.examples.ui

import androidx.compose.animation.*
import androidx.compose.material.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AnimatedContentPreview() {
    Column {
        var count by remember { mutableStateOf(0) }
        Button(onClick = { count++ }) {
            Text(text = "+")
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            AnimationContent(count)
            CrossfadeExample(count)
            ExpandView(title = "Раскрыть") {
                Text(text = "Невероятный контент")
            }
        }
    }
}

/**
 * Текст переезжает слева направо
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimationContent(count: Int) {
    AnimatedContent(
        modifier = Modifier.fillMaxWidth(),
        targetState = count,
        transitionSpec = {
            slideInHorizontally { -it } + fadeIn() with slideOutHorizontally { it } + fadeOut() using SizeTransform(
                clip = false
            )
        }
    ) { targetState ->
        Text(
            text = "HelloAndroid: $targetState",
            style = MaterialTheme.typography.h5
        )
    }
}

/**
 * Здесь нет transitionSpec, содержимое меняется при помощи fadeIn()/fadeOut()
 */
@Composable
private fun CrossfadeExample(count: Int) {
    Crossfade(
        modifier = Modifier.fillMaxWidth(),
        targetState = count,
    ) { targetState ->
        Text(
            text = "HelloAndroid: $targetState",
            style = MaterialTheme.typography.h5
        )
    }
}


@Composable
fun ExpandView(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.Cyan
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var isContentVisible by remember { mutableStateOf(false) }
            Header(title, isContentVisible) {
                isContentVisible = !isContentVisible
            }
            if (isContentVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun Header(title: String, isContentVisible: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
            .padding(
                horizontal = 8.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f),
            text = title,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h5
        )
        Icon(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(32.dp),
            imageVector = if (isContentVisible) {
                Icons.Filled.KeyboardArrowUp
            } else {
                Icons.Filled.KeyboardArrowDown
            },
            contentDescription = "Стрелочка"
        )
    }
}