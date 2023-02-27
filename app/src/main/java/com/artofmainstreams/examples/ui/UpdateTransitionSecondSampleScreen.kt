package com.artofmainstreams.examples.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.material.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Пример, как с помощью updateTransition можно по отдельности управлять анимацией в разных частях
 * Суть сводится к тому, что при помощи createChildTransition можно создавать свои transition для разных участков
 */
@Preview(showBackground = true)
@Composable
fun UpdateTransitionSecondSamplePreview() {
    CameraAnimationScreen()
}

/**
 * Допустим у нас есть несколько состояний
 */
private enum class CameraState {
    Portrait,
    Camera,
    Video
}

@Composable
fun CameraAnimationScreen(modifier: Modifier = Modifier) {
    var state: CameraState by remember { mutableStateOf(CameraState.Camera) }

    CameraComponent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        itemSelected = { state = it }
    )
}

@OptIn(ExperimentalTransitionApi::class)
@Composable
private fun CameraComponent(
    modifier: Modifier = Modifier,
    state: CameraState,
    itemSelected: (CameraState) -> Unit,
) {
    val transition = updateTransition(
        label = "Parent Animation",
        targetState = state
    )

    Column(
        modifier = modifier.background(Color(0xFFACFC80)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // здесь в зависимости от состояния меняем иконку
        CameraIcon(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            transition = transition.createChildTransition {
                when (it) {
                    CameraState.Camera -> Icons.Default.AccountCircle
                    CameraState.Video -> Icons.Default.PlayArrow
                    CameraState.Portrait -> Icons.Default.Face
                }
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        // здесь в зависимости от состояния показываем или нет зум
        ZoomSelector(
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp
            ),
            transition = transition.createChildTransition {
                when (it) {
                    CameraState.Camera -> true
                    CameraState.Video -> true
                    CameraState.Portrait -> false
                }
            }
        )
        // здесь в зависимости от состояния показываем или нет режимы съёмки
        VideoSelector(
            modifier = Modifier.padding(bottom = 8.dp),
            transition = transition.createChildTransition {
                when (it) {
                    CameraState.Camera -> false
                    CameraState.Video -> true
                    CameraState.Portrait -> false
                }
            }
        )
        CameraBottomContainer(cameraButton = {
            CameraButton(
                transition = transition.createChildTransition {
                    when (it) {
                        CameraState.Camera -> 1f
                        CameraState.Video -> 0.6f
                        CameraState.Portrait -> 0.4f
                    }
                },
                transitionColor = transition.createChildTransition {
                    when (it) {
                        CameraState.Camera -> Color.White
                        CameraState.Video -> Color.Red
                        CameraState.Portrait -> Color.White
                    }
                }
            )
        },
            cameraModeSelector = {
                ModeSelector(
                    modes = CameraState.values().toList(),
                    selectedMode = state,
                    itemSelected = itemSelected
                )
            }
        )
    }
}

@Composable
private fun CameraBottomContainer(
    cameraButton: @Composable () -> Unit,
    cameraModeSelector: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(80.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            cameraButton()
        }
        Spacer(modifier = Modifier.height(8.dp))
        cameraModeSelector()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CameraIcon(
    modifier: Modifier = Modifier,
    transition: Transition<ImageVector>
) {
    transition.AnimatedContent(
        transitionSpec = {
            slideInVertically() + fadeIn() + scaleIn() with
                    slideOutVertically() + fadeOut() + scaleOut()
        }
    ) {
        Icon(
            modifier = modifier,
            imageVector = it,
            contentDescription = null,
            tint = Color.Blue
        )
    }
}

@Composable
private fun CameraButton(
    modifier: Modifier = Modifier,
    transition: Transition<Float>,
    transitionColor: Transition<Color>
) {
    val fraction by transition.animateFloat(label = "fraction") { it }
    val color by transitionColor.animateColor(label = "color") { it }

    IconButton(
        modifier = modifier
            .clip(CircleShape)
            .aspectRatio(1f)
            .background(Color.White.copy(alpha = 0.3f))
            .border(2.dp, color, CircleShape),
        onClick = {}
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxSize(fraction)
                .background(color),
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ZoomSelector(
    modifier: Modifier = Modifier,
    transition: Transition<Boolean>
) {
    var selectedItem by remember { mutableStateOf("x1") }

    transition.AnimatedVisibility(
        modifier = modifier,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        visible = { it }
    ) {
        SelectorHelper(
            items = listOf("x1", "x2"),
            selectedIte = selectedItem,
            itemSelected = { selectedItem = it }
        ) { text, isSelected ->
            SelectedText(isSelected, text)
        }
    }
}

@Composable
private fun ModeSelector(
    modifier: Modifier = Modifier,
    modes: List<CameraState>,
    selectedMode: CameraState,
    itemSelected: (CameraState) -> Unit,
) {
    SelectorHelper(
        modifier = modifier,
        items = modes,
        selectedIte = selectedMode,
        itemSelected = { itemSelected(it) }
    ) { text, isSelected ->
        SelectedText(
            isSelected,
            when (text) {
                CameraState.Camera -> "Camera"
                CameraState.Video -> "Video"
                CameraState.Portrait -> "Portrait"
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun VideoSelector(
    modifier: Modifier = Modifier,
    transition: Transition<Boolean>
) {
    var selectedItem by remember { mutableStateOf("Normal") }
    transition.AnimatedVisibility(
        modifier = modifier,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        visible = { it }
    ) {
        SelectorHelper(
            items = listOf("Slow Motion", "Normal", "Time Lapse"),
            selectedIte = selectedItem,
            itemSelected = { selectedItem = it }
        ) { text, isSelected ->
            SelectedText(isSelected, text)
        }
    }
}

@Composable
private fun SelectedText(isSelected: Boolean, text: String) {
    Text(
        text = text
    )
}

@Composable
private fun <T : Any> SelectorHelper(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedIte: T,
    itemSelected: (T) -> Unit,
    content: @Composable (T, Boolean) -> Unit
) {
    val itemWithSelectedItem: List<Pair<T, Boolean>> = items.map {
        Pair(it, it == selectedIte)
    }
    Card(
        modifier = modifier
    ) {
        LazyRow(
            modifier = Modifier
                .background(Color(0xFF9AB9F1))
                .padding(4.dp)
                .background(Color(0xFFC8F1E9)),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(itemWithSelectedItem) { (item, isSelected) ->
                Card(
                    modifier = Modifier
                        .clickable { itemSelected(item) },

                    backgroundColor =
                    if (isSelected) {
                        Color.Cyan
                    } else {
                        Color.Transparent
                    }
                ) {
                    Box(
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp,
                        )
                    ) {
                        content(
                            item,
                            isSelected
                        )
                    }
                }
            }
        }
    }
}
