package com.artofmainstreams.examples.ui.example01

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.Fade
import androidx.transition.TransitionSet
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.Fragment01Binding
import com.artofmainstreams.examples.ui.RotateTransition2
import com.artofmainstreams.examples.ui.example01.adapter.TodoSectionAdapter
import com.artofmainstreams.examples.ui.example02.Fragment02

/**
 * RecyclerView с секциями и горизонтальной ориентацией
 */
class Fragment01 : Fragment()  {
    private lateinit var binding: Fragment01Binding
    private lateinit var viewModel: ViewModel01
    private val items: ArrayList<TodoSectionModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Fragment01Binding.inflate(inflater, container, false)
        if (items.isEmpty()) {
            items.addAll(getItems())
        }

        val adapter = TodoSectionAdapter(requireContext(), items)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewModel01::class.java]
        binding.buttonNext.setOnClickListener {
            Fragment02.start(this)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.ivCard.setOnClickListener {
            val fragment2 = Fragment02()
            //exitTransition = RotateTransition2()
//            fragment2.sharedElementEnterTransition = TransitionSet().apply {
//                addTransition(RotateTransition2())
//                addTransition(ChangeImageTransform())
//                addTransition(Fade())
//                ordering = TransitionSet.ORDERING_TOGETHER
//            }
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .addSharedElement(it, "image_start")
                .replace(R.id.nav_host_fragment, fragment2, null)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragment01)
        }
    }
}