package me.inassar.kmp_playground

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kmpplayground",
    ) {
        App()
    }
}