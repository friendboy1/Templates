package com.artofmainstreams.examples.ui.example01

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViewModel01 : ViewModel() {

    /**
     * UseCase передаётся в конструктор viewModel
     */
    private val useCase: DataUseCase = object : DataUseCase {
        override suspend fun invoke(): Data {
            return Data("Some")
        }
    }

    val data: LiveData<Data> = liveData(context = viewModelScope.coroutineContext) {
        emit(useCase.invoke())
    }

    /**
     * Рекомендуемый подход, чтобы при смене конфигурации не обращаться снова к репозиторию,
     * но при этом при уходе с экрана больше, чем на 5 секунд отменялась подписка
     */
    val dataFlow: StateFlow<String> = flowOf<String>().stateIn(
        initialValue = "",
        scope = viewModelScope,
        started = WhileSubscribed(5000)
    )

    fun doSomething() {
        viewModelScope.launch {
            // корутина остановится в момент вызова onCleared
        }
    }

    override fun onCleared() {
        super.onCleared()
        //viewmodel остановлена
    }

    interface DataUseCase {
        suspend operator fun invoke(): Data
    }

    data class Data(val value: String)
}