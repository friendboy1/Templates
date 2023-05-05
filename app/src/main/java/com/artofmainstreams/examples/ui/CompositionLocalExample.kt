package com.artofmainstreams.examples.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.tooling.preview.Preview

data class User(val name: String, val surname: String)

/**
 * Если маловероятно, что значение изменится, лучше использовать staticCompositionLocalOf
 */
val LocalActiveUser = compositionLocalOf<User> { error("No active user found!") }
val LocalActiveUser2 = compositionLocalOf { User("B", "C") }

@Preview
@Composable
fun Composition() {
    CompositionLocalProvider(LocalActiveUser provides User("A", "A")) {
        SomeScreen()
    }
}

@Composable
fun SomeScreen() {
    val user = LocalActiveUser.current // будет ошибка, если запустить просто так
    val user2 = LocalActiveUser2.current    // запустится в любом случае
    Text(text = user.toString())
    Text(text = user2.toString())
}

/**
 * Пример как для наследников можно поменять альфа по умолчанию
 */
@Preview
@Composable
fun CompositionLocalExample() {
    MaterialTheme { // MaterialTheme sets ContentAlpha.high as default
        Column {
            Text("Uses MaterialTheme's provided alpha")
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("Medium value provided for LocalContentAlpha")
                Text("This Text also uses the medium value")
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                    DescendantExample()
                }
            }
        }
    }
}

@Composable
fun DescendantExample() {
    // CompositionLocalProviders also work across composable functions
    Text("This Text uses the disabled alpha now")
}