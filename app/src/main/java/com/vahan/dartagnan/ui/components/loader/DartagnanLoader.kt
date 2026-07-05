package com.vahan.dartagnan.ui.components.loader

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DartagnanLoader(
    thinking: Boolean,
    size: Int = 180
) {
    val infiniteTransition = rememberInfiniteTransition(label = "DartagnanSequence")

    // 1. Animation de l\u0027illumination de la croix (Phase 2)
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    // 2. Sortie de l\u0027épée (Phase 3 \u0026 5)
    // -200f = sortie totale, 0f = dans le fourreau
    val swordTranslation by animateFloatAsState(
        targetValue = if (thinking) -120f else 0f,
        animationSpec = if (thinking) {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
        } else {
            tween(500, easing = FastOutLinearInEasing)
        },
        label = "swordMove"
    )

    // 3. Vibration subtile (Phase 4)
    val vibration by infiniteTransition.animateFloat(
        initialValue = -0.8f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(50, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vibration"
    )

    Box(
        modifier = Modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size((size * 0.9f).dp)
                .graphicsLayer {
                    if (thinking) {
                        translationX = vibration
                    }
                }
        ) {
            // --- DESSIN SELON LA SÉQUENCE ---

            // A. La Croix (Elle s\u0027illumine si on réfléchit)
            drawMusketeerCross(isGlowing = thinking, glowAlpha = glowAlpha)

            // B. Le Fourreau (Fixe en bas)
            drawScabbard()

            // C. L\u0027Épée (Sort ou rentre)
            drawRapier(yOffset = swordTranslation, isReflecting = thinking)

            // D. La Moustache
            drawMusketeerMoustache()

            // E. Le Chapeau
            drawMusketeerHat()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1117L)
@Composable
fun DartagnanLoaderPreview() {
    com.vahan.dartagnan.ui.theme.DartagnanTheme {
        Box(modifier = Modifier.size(300.dp), contentAlignment = Alignment.Center) {
            DartagnanLoader(thinking = true)
        }
    }
}
