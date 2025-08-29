package com.artofmainstreams.examples.ui.example06

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.Fragment06Binding
import com.artofmainstreams.examples.ui.example07.Fragment07
import com.artofmainstreams.examples.util.viewBinding

class Fragment06 : Fragment(R.layout.fragment06)  {
    private var binding by viewBinding<Fragment06Binding>()

    private lateinit var viewModel: ViewModel06


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = Fragment06Binding.bind(view)
        viewModel = ViewModelProvider(this)[ViewModel06::class.java]
        binding.buttonNext.setOnClickListener {
            Fragment07.start(this)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragment06)
        }
    }
}