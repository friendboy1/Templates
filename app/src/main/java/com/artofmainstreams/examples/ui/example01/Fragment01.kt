package com.artofmainstreams.examples.ui.example01

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.Fragment01Binding
import com.artofmainstreams.examples.ui.example02.Fragment02
import com.artofmainstreams.examples.util.viewBinding

class Fragment01 : Fragment(R.layout.fragment01)  {
    private var binding by viewBinding<Fragment01Binding>()

    private lateinit var viewModel: ViewModel01

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = Fragment01Binding.bind(view)
        viewModel = ViewModelProvider(this)[ViewModel01::class.java]
        binding.buttonNext.setOnClickListener {
            Fragment02.start(this)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragment01)
        }
    }
}