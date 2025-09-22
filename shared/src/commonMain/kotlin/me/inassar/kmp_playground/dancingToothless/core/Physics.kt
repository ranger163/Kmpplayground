package me.inassar.kmp_playground.dancingToothless.core

import kotlin.math.sqrt

/**
 * Basic 2D vector math for multiplatform game physics.
 */
data class Vec2(val x: Float, val y: Float) {
    operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)
    operator fun minus(other: Vec2) = Vec2(x - other.x, y - other.y)
    operator fun times(scalar: Float) = Vec2(x * scalar, y * scalar)
    infix fun dot(other: Vec2): Float = x * other.x + y * other.y
    fun length(): Float = sqrt(x * x + y * y)
    fun normalize(): Vec2 = if (length() > 0f) this * (1f / length()) else Vec2(0f, 0f)
}

/**
 * Game state model.
 */
data class GameState(
    val ballPosition: Vec2 = Vec2(0f, 0f),
    val ballVelocity: Vec2 = Vec2(0f, 0f),
    val ballRadius: Float = 30f,
    val isStarted: Boolean = false,
    val isFinished: Boolean = false
)
