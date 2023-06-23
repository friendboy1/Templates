package com.artofmainstreams.examples.ui.example01

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.artofmainstreams.examples.data.GitHubRepo
import com.artofmainstreams.examples.data.SomeRepository
import com.artofmainstreams.examples.data.Status
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModel01(
    private val someArgument: String,
    private val someRepository: SomeRepository
) : ViewModel() {
    private val _status: MutableStateFlow<Status> = MutableStateFlow(Status.LOADING)
    private val _dataFlow: MutableStateFlow<List<GitHubRepo>> = MutableStateFlow(emptyList())
    val dataFlow: StateFlow<List<GitHubRepo>> = _dataFlow
    val status = _status

    init {
        getSomething()
    }

    private fun getSomething() {
        viewModelScope.launch {
            _status.value = Status.LOADING
            try {
                _dataFlow.value = someRepository.getSomething(someArgument)
                _status.value = Status.DONE
            } catch (e: Exception) {
                _status.value = Status.ERROR
            }
        }
    }

    class Factory @AssistedInject constructor(
        @Assisted("someArgument") private val someArgument: String,
        private val someRepository: SomeRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ViewModel01(someArgument, someRepository) as T
        }

        @AssistedFactory
        interface FactoryCreator {
            fun create(@Assisted("someArgument") someArgument: String): Factory
        }
    }
}