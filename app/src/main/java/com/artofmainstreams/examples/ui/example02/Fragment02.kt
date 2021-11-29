package com.artofmainstreams.examples.ui.example02

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.Fragment02Binding
import com.artofmainstreams.examples.ui.example03.Fragment03
import com.artofmainstreams.examples.ui.example02.adapter.CountriesAdapter
import kotlin.collections.ArrayList

class Fragment02 : Fragment()  {
    private lateinit var binding: Fragment02Binding
    private lateinit var viewModel: ViewModel02
    private val sectionList: ArrayList<CountriesModel> = ArrayList()
    private lateinit var countries: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Fragment02Binding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewModel02::class.java]
        binding.buttonNext.setOnClickListener {
            Fragment03.start(this)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initView() {
        countries = resources.getStringArray(R.array.countries)
        val countriesModels: ArrayList<CountriesModel> = ArrayList()
        for (country in countries) {
            countriesModels.add(CountriesModel(country, false))
        }
        getHeaderListLetter(countriesModels)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = CountriesAdapter(sectionList)
    }

    /**
     * Пробегаемся по всему списку и в новый добавляем буквы
     */
    private fun getHeaderListLetter(usersList: ArrayList<CountriesModel>) {
        usersList.sortWith { user1, user2 ->
            user1.name[0].toString().uppercase().compareTo(user2.name[0].toString().uppercase())
        }
        var lastHeader = ""
        sectionList.clear()
        for (i in 0 until usersList.size) {
            val user = usersList[i]
            val header = user.name[0].toString().uppercase()
            if (!TextUtils.equals(lastHeader, header)) {
                lastHeader = header
                sectionList.add(CountriesModel(header, true))
            }
            sectionList.add(user)
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragment02)
        }
    }
}