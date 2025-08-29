package com.artofmainstreams.examples.ui.example07

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.Fragment07Binding
import com.artofmainstreams.examples.ui.example01.Fragment01
import com.artofmainstreams.examples.util.viewBinding

class Fragment07 : Fragment(R.layout.fragment07) {
    private var binding by viewBinding<Fragment07Binding>()

    private lateinit var viewModel: ViewModel07


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = Fragment07Binding.bind(view)
        viewModel = ViewModelProvider(this)[ViewModel07::class.java]
        binding.buttonNext.setOnClickListener {
            Fragment01.start(this)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragment07)
        }
    }
}