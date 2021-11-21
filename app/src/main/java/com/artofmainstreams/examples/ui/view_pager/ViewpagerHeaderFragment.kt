package com.artofmainstreams.examples.ui.view_pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.DrawerLayoutFragmentBinding
import com.artofmainstreams.examples.databinding.ViewPagerFragmentBinding
import com.artofmainstreams.examples.ui.coordinator_layout.CoordinatorLayoutFragment

/**
 * Фрагмент, использующий ViewPager
 *
 * @author Andrei Khromov on 21.11.2021
 */
class ViewPagerFragment : Fragment() {
    private lateinit var binding: ViewPagerFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ViewPagerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ViewPagerAdapter()
        binding.viewPager2.adapter = adapter
        binding.header.numPages = adapter.itemCount
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.header.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        })
        binding.bottom.buttonNext.setOnClickListener {
            CoordinatorLayoutFragment.start(this)
        }
        binding.bottom.buttonNext.isEnabled = false
        binding.bottom.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.viewPagerFragment)
        }
    }
}