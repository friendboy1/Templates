package com.artofmainstreams.examples.ui.drawer_layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.DrawerLayoutFragmentBinding
import com.artofmainstreams.examples.ui.view_pager.ViewPagerFragment

/**
 * Фрагмент, использующий DrawerLayout
 *
 * @author Andrei Khromov on 21.11.2021
 */
class DrawerLayoutFragment : Fragment() {

    private lateinit var binding: DrawerLayoutFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DrawerLayoutFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.content.bottom.buttonNext.setOnClickListener {
            ViewPagerFragment.start(this)
        }
        binding.content.bottom.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.drawerLayoutFragment)
        }
    }
}