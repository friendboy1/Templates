package com.artofmainstreams.examples.ui.example01

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.Fragment01Binding
import com.artofmainstreams.examples.ui.example02.Fragment02
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Fragment01 : Fragment() {
    private lateinit var binding: Fragment01Binding

    private lateinit var viewModel: ViewModel01

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Fragment01Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewModel01::class.java]
        binding.buttonNext.setOnClickListener {
            Fragment02.start(this)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        init()
    }

    fun init() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            // должно использоваться только с viewLifecycleOwner
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                // onStart() -> onStop()
            }
        }
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewModel.dataFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    // обрабатываем значения
                }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            // привязан к ЖЦ view во фрагменте
            // onCreateView() -> onDestroyView()

        }
        lifecycleScope.launch {
            // привязан к ЖЦ фрагмента
            // onCreate() -> onDestroy()
            // для одиночных операций non-suspend:
            withCreated {
                // onCreate() -> onDestroy()
            }
            withStarted {
                // onStart() -> onDestroy()
            }
            withResumed {
                // onResume() -> onDestroy()
            }
        }
        lifecycleScope.launchWhenCreated {
            // onCreate() -> onDestroy()
        }
        lifecycleScope.launchWhenStarted {
            // onStart() -> onDestroy()
        }
        lifecycleScope.launchWhenResumed {
            // onResume() -> onDestroy()
            if (this@Fragment01.view != null) {
                println()
            }
            // или
            if (lifecycle.currentState >= Lifecycle.State.RESUMED) {
                println()
            }
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragment01)
        }
    }
}