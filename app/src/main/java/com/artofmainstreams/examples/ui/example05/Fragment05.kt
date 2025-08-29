package com.artofmainstreams.examples.ui.example05

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.Fragment05Binding
import com.artofmainstreams.examples.ui.example06.Fragment06
import com.artofmainstreams.examples.util.viewBinding

class Fragment05 : Fragment(R.layout.fragment05) {
    private var binding by viewBinding<Fragment05Binding>()

    private lateinit var viewModel: ViewModel05


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = Fragment05Binding.bind(view)
        viewModel = ViewModelProvider(this)[ViewModel05::class.java]
        binding.buttonNext.setOnClickListener {
            Fragment06.start(this)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragment05)
        }
    }
}