package me.inassar.kmp_playground.dancingToothless.ui

/**
 * A cross-platform sound manager to play short effects (collision)
 * and longer ones (game over).
 */
expect object SoundManager {
    fun init()
    fun playCollision()
    fun playGameOver()
}
