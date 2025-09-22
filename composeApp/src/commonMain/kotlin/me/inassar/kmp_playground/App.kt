package me.inassar.kmp_playground

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import me.inassar.kmp_playground.dancingToothless.ui.BounceAndGrowGame

@Composable
@Preview
fun App() {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme())
            darkColorScheme() else lightColorScheme()
    ) {
        Scaffold {
            BounceAndGrowGame()
        }
    }
}