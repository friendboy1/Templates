package com.artofmainstreams.examples.ui.example01

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.appComponent
import com.artofmainstreams.examples.data.Analytics
import com.artofmainstreams.examples.data.GitHubRepo
import com.artofmainstreams.examples.data.Status
import com.artofmainstreams.examples.databinding.Fragment01Binding
import com.artofmainstreams.examples.ui.example02.Fragment02
import kotlinx.coroutines.launch
import javax.inject.Inject

class Fragment01 : Fragment() {
    private lateinit var binding: Fragment01Binding
    private val viewModel: ViewModel01 by viewModels {
        factory.create("friendboy1")
    }

    // Lazy и Provider не работают  с зависимостями, которые используют Assisted Inject
    @Inject
    lateinit var factory: ViewModel01.Factory.FactoryCreator

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Fragment01Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNext.setOnClickListener {
            Fragment02.start(this)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataFlow.collect(::updateUi)
            }
        }
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.status.collect(::updateStatus)
            }
        }
    }

    @Inject
    fun trackScreenShow(analytics: Analytics) {
        analytics.trackScreenShow()
        Log.i("Fragment01", "Initialisation")
    }

    private fun updateStatus(status: Status) {
        if (view == null) return
        when (status) {
            Status.LOADING -> binding.body.text = "Загрузка"
            Status.DONE -> binding.title.text = "Успешно"
            Status.ERROR -> binding.body.text = "Ошибка"
        }
    }
    private fun updateUi(news: List<GitHubRepo>) {
        if (view == null) return
        val result = StringBuilder()
        news.forEach {
            result.append(it.name)
            result.append("\n")
        }
        binding.body.text = result
    }

    companion object {
        private const val ARG_NEWS_ID = "news_id"

        fun start(from: Fragment, newsId: String) {
            from.findNavController().navigate(R.id.fragment01, Bundle(1).apply {
                putString(ARG_NEWS_ID, newsId)
            })
        }
    }
}