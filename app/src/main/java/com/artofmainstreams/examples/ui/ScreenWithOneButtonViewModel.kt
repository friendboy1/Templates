package com.artofmainstreams.examples.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ScreenWithOneButtonViewModel : ViewModel() {
    private val _effects = Channel<ScreenWithOneButtonEffect>()
    val effects: Flow<ScreenWithOneButtonEffect> = _effects.receiveAsFlow()

    fun onClickNavigateNext() {
        viewModelScope.launch {
            _effects.send(ScreenWithOneButtonEffect.NavigateNext)
        }
    }
}

sealed class ScreenWithOneButtonEffect {
    object NavigateNext: ScreenWithOneButtonEffect()
}