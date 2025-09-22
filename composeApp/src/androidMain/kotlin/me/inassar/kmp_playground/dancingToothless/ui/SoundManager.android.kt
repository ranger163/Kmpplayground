package me.inassar.kmp_playground.dancingToothless.ui

import android.media.MediaPlayer
import android.media.SoundPool
import android.os.SystemClock
import me.inassar.kmp_playground.AppContext
import me.inassar.kmp_playground.R

actual object SoundManager {
    private lateinit var soundPool: SoundPool
    private lateinit var gameOverPlayer: MediaPlayer
    private var soundId: Int = 0
    private var lastCollisionTime: Long = 0L

    actual fun init() {
        val appContext = AppContext.get()

        // Collision sound using SoundPool
        soundPool = SoundPool.Builder().setMaxStreams(1).build()
        soundId = soundPool.load(appContext, R.raw.dancing_toothless_song, 1)

        // Game over looping sound
        gameOverPlayer = MediaPlayer.create(appContext, R.raw.dancing_toothless_song)
    }

    actual fun playCollision() {
        val now = SystemClock.elapsedRealtime()
        if (now - lastCollisionTime >= 80) {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        } else {
            soundPool.release()
        }
        lastCollisionTime = now
    }

    actual fun playGameOver() {
        gameOverPlayer.start()
    }
}
