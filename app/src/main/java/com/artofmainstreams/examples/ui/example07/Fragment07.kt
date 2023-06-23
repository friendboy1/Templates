package com.artofmainstreams.examples.ui.example07

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.Fragment07Binding
import com.artofmainstreams.examples.ui.example01.Fragment01

class Fragment07 : Fragment()  {
    private lateinit var binding: Fragment07Binding

    private lateinit var viewModel: ViewModel07

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Fragment07Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewModel07::class.java]
        binding.buttonNext.setOnClickListener {
            Fragment01.start(this, TOP_NEWS_ID)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val TOP_NEWS_ID = "top"

        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragment07)
        }
    }
}