package me.inassar.kmp_playground.dancingToothless.ui

import kotlinx.browser.document
import org.w3c.dom.HTMLAudioElement

actual object SoundManager {

    private lateinit var songSound: HTMLAudioElement

    actual fun init() {
        songSound = (document.createElement("audio") as HTMLAudioElement).apply {
            src = "public/dancing_toothless_song.mp3"
        }
    }

    actual fun playCollision() {
        songSound.currentTime = 0.0
        songSound.play()
    }

    actual fun playGameOver() {
        songSound.play()
    }
}