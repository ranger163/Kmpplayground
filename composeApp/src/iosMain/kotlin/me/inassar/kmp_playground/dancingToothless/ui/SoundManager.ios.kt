package me.inassar.kmp_playground.dancingToothless.ui

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.*
import platform.Foundation.*
import platform.Foundation.NSBundle

actual object SoundManager {
    private var songPlayer: AVAudioPlayer? = null
    private var lastCollisionTime = (0L)

    actual fun init() {
        songPlayer = loadSound("dancing_toothless_song", "mp3")
    }

    actual fun playCollision() {
        val now = currentMillis()
        if (now - lastCollisionTime >= 80) {
            songPlayer?.apply {
                stop()
                currentTime = 0.0
                play()
            }
        }
        lastCollisionTime = now
    }

    actual fun playGameOver() {
        songPlayer?.apply {
            currentTime = 0.0
            play()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun loadSound(name: String, ext: String): AVAudioPlayer? {
        val url = NSBundle.mainBundle.URLForResource(name, ext) ?: return null
        val player = AVAudioPlayer(contentsOfURL = url, error = null)
        player.prepareToPlay()
        return player
    }

    private fun currentMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()
}