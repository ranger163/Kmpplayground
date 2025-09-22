package me.inassar.kmp_playground.dancingToothless.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.inassar.kmp_playground.dancingToothless.core.GameConfig
import kotlin.math.min
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Converter to allow Animatable to work with Colors.
 */
val ColorVectorConverter: TwoWayConverter<Color, AnimationVector4D> =
    TwoWayConverter(
        convertToVector = { c: Color -> AnimationVector4D(c.red, c.green, c.blue, c.alpha) },
        convertFromVector = { v -> Color(v.v1, v.v2, v.v3, v.v4) }
    )

@OptIn(ExperimentalTime::class)
@Composable
fun BounceAndGrowGame() {
    val scope = rememberCoroutineScope()

    var started by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }
    var isToothlessDancing by remember { mutableStateOf(false) }

    val colors = listOf(Color.White, Color.Red, Color.Green, Color.Cyan, Color.Yellow)
    var colorIndex by remember { mutableIntStateOf(0) }
    val circleColor = remember { Animatable(colors.first(), ColorVectorConverter) }
    var ballColor by remember { mutableStateOf(colors.first()) }

    var ballRadius by remember { mutableStateOf(30f) }
    var ballPosition by remember { mutableStateOf(Offset(0f, 0f)) }
    var ballVelocity by remember { mutableStateOf(Offset.Zero) }

    val density = LocalDensity.current
    SoundManager.init()

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(Color(0xFF101010)),
        contentAlignment = Alignment.Center
    ) {
        val canvasSize = with(density) { Size(maxWidth.toPx(), maxHeight.toPx()) }
        val outerRadius = min(canvasSize.width, canvasSize.height) / 2.5f
        val center = Offset(canvasSize.width / 2, canvasSize.height / 2)

        // Animate circle color cycling after game over
        LaunchedEffect(finished) {
            while (finished) {
                colorIndex = (colorIndex + 1) % colors.size
                circleColor.snapTo(colors[colorIndex])
                ballColor = circleColor.value
                delay(1500)
            }
        }

        // Main game loop with frame sync
        LaunchedEffect(started) {
            if (started) {
                // Continuous circle color cycling
                scope.launch {
                    while (true) {
                        colorIndex = (colorIndex + 1) % colors.size
                        circleColor.animateTo(
                            targetValue = colors[colorIndex],
                            animationSpec = tween(3000)
                        )
                    }
                }

                var lastTime = Clock.System.now().nanosecondsOfSecond.toLong()
                while (true) {
                    withFrameNanos {now->
                        val delta = (now-lastTime)/1_000_000_000f
                        lastTime=now
                    val nextVelocity =
                        ballVelocity + Offset(0f, GameConfig.gravity * GameConfig.frameTime)
                    val nextPosition = ballPosition + nextVelocity * GameConfig.frameTime

                    val distanceFromCenter = nextPosition.getDistance()
                    val currentRadius = ballRadius
                    val isColliding = distanceFromCenter + currentRadius >= outerRadius
                    val isFullyFilled = currentRadius >= outerRadius

                    if (isColliding) {
                        if (!isFullyFilled) {
                            isToothlessDancing = true
                            SoundManager.playCollision()

                            // Reflect velocity with randomness
                            val normal = nextPosition / distanceFromCenter
                            val reflectedVelocity =
                                ballVelocity - normal * (2 * (ballVelocity dot normal))
                            val randomness = (Random.nextFloat() - 0.5f) * 0.1f
                            val randomVelocityTweak =
                                Offset(
                                    randomness * reflectedVelocity.y,
                                    randomness * reflectedVelocity.x
                                )
                            val bounceForce = Random.nextFloat() * 0.6f + 0.8f
                            ballVelocity = (reflectedVelocity + randomVelocityTweak) * bounceForce

                            val penetration = (distanceFromCenter + currentRadius) - outerRadius
                            ballPosition = nextPosition - normal * penetration

                            ballRadius = (currentRadius + GameConfig.radiusGrowth)
                            ballColor = circleColor.value
                        } else {
                            started = false
                            finished = true
                            isToothlessDancing = true
                            SoundManager.playGameOver()
                        }
                    } else {
                        ballVelocity = nextVelocity
                        ballPosition = (nextPosition)

                        if (isToothlessDancing) {
                            scope.launch {
                                delay(500)
                                isToothlessDancing = false
                            }
                        }
                    }

                }}
            }
        }

        // Game description text
        Text(
            text = "Ball grows with every bounce\nuntil toothless doesn't stop dancing",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 32.dp)
        )

        // Draw circle + ball
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = circleColor.value.copy(alpha = 0.2f),
                radius = outerRadius,
                center = center,
                style = Stroke(width = 4.dp.toPx())
            )
            drawCircle(
                color = ballColor,
                radius = ballRadius,
                center = center + ballPosition
            )
        }

        // Toothless animation
        DancingToothless(
            isPlaying = isToothlessDancing,
            modifier = Modifier.size(200.dp).align(Alignment.Center)
        )

        if (!started) {
            OutlinedButton(
                onClick = {
                    started = true
                    finished = false
                    ballVelocity = Offset(0f, -10f)

                    scope.launch {
                        ballPosition = (Offset.Zero)
                        ballRadius = (30f)
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp)
            ) {
                Text("Start")
            }
        }
    }
}

/**
 * Extension function to calculate dot product between two Offsets.
 */
infix fun Offset.dot(other: Offset): Float = this.x * other.x + this.y * other.y

/**
 * Extension function to multiply an Offset by a scalar.
 */
operator fun Offset.times(scalar: Float): Offset = Offset(this.x * scalar, this.y * scalar)