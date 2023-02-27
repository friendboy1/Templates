package com.artofmainstreams.examples.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.FragmentScene02Binding

/**
 * Второй фрагмент, все настройки внутри MotionScene
 *
 * @author Andrei Khromov on 20.11.2021
 */
class FragmentScene02 : Fragment() {

    private lateinit var binding: FragmentScene02Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScene02Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonNext.setOnClickListener {
            FragmentScene03.start(this)
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragmentScene02)
        }
    }
}