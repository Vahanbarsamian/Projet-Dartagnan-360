package com.vahan.dartagnan.ui.components.loader

import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.shimmerEffect(
    showShimmer: Boolean,
    shimmerColor: Color = Color.White
): Modifier = composed {
    if (!showShimmer) return@composed this

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )

    this.graphicsLayer(alpha = 0.99f)
        .drawWithContent {
            val brush = Brush.linearGradient(
                colors = listOf(
                    shimmerColor.copy(alpha = 0.0f),
                    shimmerColor.copy(alpha = 0.4f),
                    shimmerColor.copy(alpha = 0.0f),
                ),
                start = Offset(translateAnim - 1000f, translateAnim - 1000f),
                end = Offset(translateAnim, translateAnim)
            )
            drawContent()
            drawRect(
                brush = brush,
                blendMode = BlendMode.SrcAtop
            )
        }
}
