package com.artofmainstreams.examples.ui.example02

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.Fragment02Binding
import com.artofmainstreams.examples.ui.example03.Fragment03
import com.artofmainstreams.examples.util.viewBinding

class Fragment02 : Fragment(R.layout.fragment02) {
    private var binding by viewBinding<Fragment02Binding>()

    private lateinit var viewModel: ViewModel02

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = Fragment02Binding.bind(view)
        viewModel = ViewModelProvider(this)[ViewModel02::class.java]
        binding.buttonNext.setOnClickListener {
            Fragment03.start(this)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragment02)
        }
    }
}