package com.artofmainstreams.examples.ui.example04

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.Fragment04Binding
import com.artofmainstreams.examples.ui.example05.Fragment05
import com.artofmainstreams.examples.util.viewBinding

class Fragment04 : Fragment(R.layout.fragment04)  {
    private var binding by viewBinding<Fragment04Binding>()

    private lateinit var viewModel: ViewModel04

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = Fragment04Binding.bind(view)
        viewModel = ViewModelProvider(this)[ViewModel04::class.java]
        binding.buttonNext.setOnClickListener {
            Fragment05.start(this)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragment04)
        }
    }
}