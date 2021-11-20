package com.artofmainstreams.examples.ui.example02.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artofmainstreams.examples.ui.example02.CountriesModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artofmainstreams.examples.R

class CountriesAdapter(private val mCountriesModelList: ArrayList<CountriesModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SECTION_VIEW) {
            SectionHeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_header_title, parent, false)
            )
        } else ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (mCountriesModelList[position].isSection) {
            SECTION_VIEW
        } else {
            CONTENT_VIEW
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            SECTION_VIEW -> {
                val sectionHeaderViewHolder = holder as SectionHeaderViewHolder
                val sectionItem = mCountriesModelList[position]
                sectionHeaderViewHolder.headerTitleTextview.text = sectionItem.name
            }
            CONTENT_VIEW -> {
                val itemViewHolder = holder as ItemViewHolder
                itemViewHolder.nameTextview.text = mCountriesModelList[position].name
            }
        }
    }

    override fun getItemCount(): Int {
        return mCountriesModelList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextview: TextView = itemView.findViewById(R.id.nameTextview)

    }

    inner class SectionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var headerTitleTextview: TextView = itemView.findViewById(R.id.headerTitleTextview)

    }

    companion object {
        const val SECTION_VIEW = 0
        const val CONTENT_VIEW = 1
    }
}