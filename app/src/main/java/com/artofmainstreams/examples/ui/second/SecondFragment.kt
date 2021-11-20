package com.artofmainstreams.examples.ui.second

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.SecondFragmentBinding

/**
 * Второй фрагмент, возвращает на первый
 *
 * @author Andrei Khromov on 20.11.2021
 */
class SecondFragment : Fragment() {

    private lateinit var binding: SecondFragmentBinding

    private lateinit var viewModel: SecondViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SecondFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SecondViewModel::class.java]
        binding.button.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {

        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.secondFragment)
        }
    }
}