package com.artofmainstreams.examples.ui.example03

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.Fragment03Binding
import com.artofmainstreams.examples.ui.example04.Fragment04
import com.artofmainstreams.examples.util.viewBinding

class Fragment03 : Fragment(R.layout.fragment03)  {
    private var binding by viewBinding<Fragment03Binding>()

    private lateinit var viewModel: ViewModel03

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = Fragment03Binding.bind(view)
        viewModel = ViewModelProvider(this)[ViewModel03::class.java]
        binding.buttonNext.setOnClickListener {
            Fragment04.start(this)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragment03)
        }
    }
}