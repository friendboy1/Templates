package com.artofmainstreams.examples.ui.example01

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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

    val dataFlow: Flow<String> = flowOf()

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