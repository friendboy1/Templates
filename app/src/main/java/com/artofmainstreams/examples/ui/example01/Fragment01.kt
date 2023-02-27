package com.artofmainstreams.examples.ui.example01

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.theme.ComposeExampleTheme
import com.artofmainstreams.examples.theme.newStyle
import com.artofmainstreams.examples.ui.LoaderComponent
import kotlin.random.Random

/**
 * Пример работы с Accessibility
 */
class Fragment01 : Fragment() {
    private lateinit var viewModel: ViewModel01

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Hello(name = "Compose")
                LoaderComponent()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewModel01::class.java]
    }

    @Preview(
        showBackground = true,
        fontScale = 2f,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showSystemUi = true
    )
    @Composable
    fun HelloPreview(
        @PreviewParameter(HelloPreviewParameterProvider::class) hello: String
    ) {
        ComposeExampleTheme {
            Hello(hello)
        }
    }

    @Composable
    fun Hello(name: String) {
        Text(
            text = stringResource(id = R.string.hello, name),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.some_padding)),
            fontSize = with(LocalDensity.current) {
                dimensionResource(id = R.dimen.font_size_32).toSp()
            },
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.newStyle
        )
    }

    /**
     * Порядок важен
     */
    private val maxWidthGreyModifier = Modifier
        .background(Color.LightGray)
        .fillMaxWidth()
        .padding(16.dp)
    private val selected = Random.nextBoolean()

    /**
     * Хорошей практикой является возможность указывать Modifier, при этом порядок важен
     */
    @Preview(
        showBackground = true,
        fontScale = 2f,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showSystemUi = true
    )
    @Composable
    fun Notification(
        modifier: Modifier = Modifier,
        messageModifier: Modifier = Modifier,
        buttonModifier: Modifier = Modifier
    ) {
        Column(
            // порядок важен
            modifier = if (selected) maxWidthGreyModifier else modifier
        ) {
            Text(text = "Text", modifier = messageModifier)
            Button(onClick = { Log.i("Notification", "onClick") }, modifier = buttonModifier) {
                Text(text = "Ok")
            }
        }
    }

    @Preview
    @Composable
    fun AccessibilityExample() {
        Column {
            /**
             * Интерактивные элементы
             */
            Text(
                text = "SomeText",
                modifier = Modifier.clickable(enabled = false, onClickLabel = "Обычный текст") {})

            /**
             * В кнопке уже есть onClick, поэтому через Modifier.semantic
             */
            Button(onClick = { }, modifier = Modifier.semantics {
                onClick(label = "Некоторая кнопка") {
                    true
                }
                stateDescription = if (selected) "Выбран контакт, с которым делимся контентом"
                else "Этот контакт не выбран. С ним подкастом не делимся"
                heading() // для основной информации
            }) {
                Text(text = "Кнопка 1")
            }
            Icon(imageVector = Icons.Filled.Share, contentDescription = "Поделить подкастом")
            Box(modifier = Modifier.semantics {
                customActions = listOf(
                    CustomAccessibilityAction(label = "Поделиться контактом", action = { true }),
                    CustomAccessibilityAction(label = "Добавить в избранное", action = { true })
                )
            }) {
                Button(onClick = { }, modifier = Modifier.clearAndSetSemantics { }) {
                    Text(text = "Кнопка 2")
                }
            }
        }
    }

    companion object {
        fun start(from: Fragment) {
            from.findNavController().navigate(R.id.fragment01)
        }
    }
}

class HelloPreviewParameterProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf("Compose", "Android", "World")
}
