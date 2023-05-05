package com.artofmainstreams.examples.ui.example01

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R

class Fragment01 : Fragment() {
    private lateinit var viewModel: ViewModel01

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext())
        .apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Hello(name = "Compose")
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewModel01::class.java]
    }

    @Preview(
        showBackground = true,
        fontScale = 2f,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
        showSystemUi = true
    )
    @Composable
    fun HelloPreview() {
        Hello(name = "Compose")
    }

    @Composable
    fun Hello(name: String) {
        Text(
            text = "Hello $name",
            modifier = Modifier.padding(32.dp),
            fontSize = 32.sp
        )
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragment01)
        }
    }
}

@Composable
fun Hello(name: String, color: Color = Color.Black) {
    Text(
        text = "Hello $name",
        modifier = Modifier.padding(32.dp),
        fontSize = 32.sp,
        color = color
    )
}