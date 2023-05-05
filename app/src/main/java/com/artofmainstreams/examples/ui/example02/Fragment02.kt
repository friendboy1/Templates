package com.artofmainstreams.examples.ui.example02

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.Fragment02Binding
import com.artofmainstreams.examples.databinding.FragmentContainerBinding
import com.artofmainstreams.examples.ui.example01.Hello
import com.artofmainstreams.examples.ui.example03.Fragment03

/**
 * Пример использования нескольких ComposeView
 * В этом случае нужно указывать id для правильного восстановления состояния
 */
class Fragment02 : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = LinearLayout(requireContext()).apply {
        addView(
            ComposeView(requireContext()).apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
                id = R.id.compose_view_x
                setContent {
                    Hello(name = "World!")
                }
            }
        )
        addView(TextView(requireContext()).apply {
            text = getString(R.string.some_text)
        })
        addView(
            ComposeView(requireContext()).apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
                id = R.id.compose_view_y
                setContent {
                    Hello(name = "World!")
                }
            }
        )
    }

    companion object {

        @Composable
        fun Start() {
            AndroidViewBinding(FragmentContainerBinding::inflate) {
                val myFragment = fragmentContainerView.getFragment<Fragment02>()
                // ...
            }
        }
    }
}