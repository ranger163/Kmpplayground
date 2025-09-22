package me.inassar.kmp_playground.dancingToothless.ui

import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

actual object SoundManager {

    private lateinit var songSound: Clip
    actual fun init() {
        val stream = javaClass.getResourceAsStream("/sounds/dancing_toothless_song.wav")?: error("Resource not found: /sounds/dancing_toothless_song.wav")
        val audioInput = AudioSystem.getAudioInputStream(stream.buffered())
        songSound = AudioSystem.getClip().apply {
            open(audioInput)
        }
    }

    actual fun playCollision() {
        songSound.apply {
            framePosition = 0
            start()
        }
    }

    actual fun playGameOver() {
        songSound.start()
        songSound.loop(1)
    }
}