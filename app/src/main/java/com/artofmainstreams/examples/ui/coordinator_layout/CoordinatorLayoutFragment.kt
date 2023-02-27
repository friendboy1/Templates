package com.artofmainstreams.examples.ui.coordinator_layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.CoordinatorLayoutFragmentBinding
import com.artofmainstreams.examples.ui.drawer_layout.DrawerLayoutFragment

/**
 * Фрагмент, который использует CoordinatorLayout
 *
 * @author Andrei Khromov on 20.11.2021
 */
class CoordinatorLayoutFragment : Fragment() {

    private lateinit var binding: CoordinatorLayoutFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CoordinatorLayoutFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottom.buttonNext.setOnClickListener {
            DrawerLayoutFragment.start(this)
        }
        binding.bottom.buttonBack.isEnabled = false
        binding.bottom.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.coordinatorLayoutFragment)
        }
    }
}