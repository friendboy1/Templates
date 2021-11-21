package com.artofmainstreams.examples.ui.view_pager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artofmainstreams.examples.databinding.ViewPagerItemPageBinding

/**
 * Adapter для ViewPager
 */
class ViewPagerAdapter : RecyclerView.Adapter<PagerViewHolder>() {
    private lateinit var binding: ViewPagerItemPageBinding

    private val colors = intArrayOf(
        android.R.color.holo_red_light,
        android.R.color.holo_blue_dark,
        android.R.color.holo_purple
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ViewPagerItemPageBinding.inflate(inflater, parent, false)
        return PagerViewHolder(binding.root)
    }

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) = holder.itemView.run {
        binding.title.text = "item $position"
        binding.container.setBackgroundResource(colors[position])
    }
}

class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)