package com.artofmainstreams.examples.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ScreenWithOneButtonDestination(
    modifier: Modifier = Modifier,
    onClickNext: () -> Unit = {},
    text: String = "Navigate next ->",
    viewModel: ScreenWithOneButtonViewModel = hiltViewModel()
) {
    ScreenWithOneButton(
        modifier = modifier,
        text = text,
        onClickNext = viewModel::onClickNavigateNext
    )
    LaunchedEffect(true) {
        viewModel.effects.collect {
            when (it) {
                ScreenWithOneButtonEffect.NavigateNext -> onClickNext()
            }
        }
    }
}

@Composable
fun ScreenWithOneButton(
    modifier: Modifier = Modifier,
    text: String = "Navigate next ->",
    onClickNext: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onClickNext) {
            Text(text = text)
        }
    }
}