package me.inassar.kmp_playground.dancingToothless.ui

import androidx.compose.runtime.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import org.jetbrains.compose.resources.imageResource
import kmpplayground.composeapp.generated.resources.Res
import kmpplayground.composeapp.generated.resources.dancingtoothless
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi

/**
 * Draw Toothless sprite animation from sprite sheet.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun DancingToothless(isPlaying: Boolean, modifier: Modifier = Modifier) {
    val spriteSheet = imageResource(Res.drawable.dancingtoothless)

    val columns = 5
    val rows = 45
    val totalFrames = 223
    val frameWidth = spriteSheet.width / columns
    val frameHeight = spriteSheet.height / rows

    var frameIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            frameIndex = (frameIndex + 1) % totalFrames
            delay(40) // ~25 fps
        }
    }

    Canvas(modifier = modifier) {
        val col = frameIndex % columns
        val row = frameIndex / columns

        drawImage(
            image = spriteSheet,
            srcOffset = IntOffset(col * frameWidth, row * frameHeight),
            srcSize = IntSize(frameWidth, frameHeight),
            dstSize = IntSize(size.width.toInt(), size.height.toInt())
        )
    }
}
