package com.artofmainstreams.examples.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.ContextCompat
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.databinding.Fragment01Binding
import com.artofmainstreams.examples.ui.example01.Hello

/**
 * Custom view
 */
class MyView(context: Context) : androidx.appcompat.widget.AppCompatTextView(context) {
    var selectedItem: Int = 0
        set(value) {
            field = value
            text = value.toString()
        }

    init {
        textSize = context.resources.getDimension(R.dimen.medium)
        with(TypedValue()) {
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
            isClickable = true
            isFocusable = true
            setBackgroundResource(resourceId)
        }
    }
}

/**
 * Пример использования custom view в compose-функции
 */
@Preview
@Composable
fun CustomView() {
    var selectedItem by remember { mutableStateOf(0) }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        // создание:
        factory = { context ->
            MyView(context).apply {
                setOnClickListener {
                    selectedItem += 1
                }
            }
        },
        // рекомпозиция:
        update = { view ->
            view.selectedItem = selectedItem
        }
    )
}

/**
 * Пример использования custom view в lazy-списке
 */
@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun AndroidViewInLazyList() {
    LazyColumn {
        items(10000) { index ->
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                factory = { context ->
                    MyView(context)
                },
                update = { view ->
                    view.selectedItem = index
                },
                // компонент переиспользуется
                onReset = { view ->
                    view.selectedItem = -1
                },
                // компонент больше не будет использоваться
                onRelease = { view ->
                    view.selectedItem = -100
                }
            )
        }
    }
}

/**
 * Пример view-binding внутри compose-функции
 */
@RequiresApi(Build.VERSION_CODES.M)
@Preview
@Composable
fun AndroidViewBindingExample() {
    val context = LocalContext.current
    AndroidViewBinding(Fragment01Binding::inflate) {
        buttonNext.setBackgroundColor(ContextCompat.getColor(context, R.color.color_blue_200))
        composeView.setContent { Hello(name = "Compose") }
    }
}

/**
 * Пример использования LocalContext
 */
@Preview
@Composable
fun ToastGreetingButton(greeting: String = "Великолепный текст") {
    val context = LocalContext.current
    Button(onClick = {
        Toast.makeText(context, greeting, Toast.LENGTH_SHORT).show()
    }) {
        Text("Greet")
    }
}

/**
 * Хороший пример с BroadcastReceiver
 */
@Composable
fun SystemBroadcastReceiver(
    systemAction: String,
    onSystemEvent: (intent: Intent?) -> Unit
) {
    // Получаем текущий context в данной части UI-дерева
    val context = LocalContext.current

    // Безопасно используем onSystemEvent лямбду, переданную в функцию
    val currentOnSystemEvent by rememberUpdatedState(onSystemEvent)

    // Если context или systemAction изменились, перерегистрируемся снова
    DisposableEffect(context, systemAction) {
        val intentFilter = IntentFilter(systemAction)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                currentOnSystemEvent(intent)
            }
        }
        context.registerReceiver(broadcast, intentFilter)
        // Когда эффект покидает композицию, очищаем callback
        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}

@Composable
fun HomeScreen() {
    SystemBroadcastReceiver(Intent.ACTION_BATTERY_CHANGED) { batteryStatus ->
        val isCharging = /* Get from batteryStatus ... */ true
        /* Do something if the device is charging */
    }
    /* Rest of the HomeScreen */
}