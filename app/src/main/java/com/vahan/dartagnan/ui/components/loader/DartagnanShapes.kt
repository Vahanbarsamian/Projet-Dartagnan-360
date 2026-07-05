package com.vahan.dartagnan.ui.components.loader

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate

// Couleurs Premium
val GoldGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFD4AF37), Color(0xFFFFD700), Color(0xFF8B7500)),
    start = Offset.Zero,
    end = Offset.Infinite
)
val DarkGold = Color(0xFF5D4037)
val DeepBlack = Color(0xFF0A0A0A)
val Steel = Color(0xFFE0E0E0)

fun DrawScope.drawMusketeerCross(isGlowing: Boolean, glowAlpha: Float) {
    val w = size.width
    val h = size.height
    val cx = w / 2
    val cy = h / 2
    val r = w * 0.45f
    
    val crossPath = Path().apply {
        // Branche verticale évasée
        moveTo(cx - w * 0.05f, cy - r * 0.8f)
        quadraticTo(cx, cy - r, cx + w * 0.05f, cy - r * 0.8f)
        lineTo(cx + w * 0.03f, cy + r * 0.8f)
        quadraticTo(cx, cy + r, cx - w * 0.03f, cy + r * 0.8f)
        close()
        
        // Branche horizontale évasée
        moveTo(cx - r * 0.8f, cy - h * 0.05f)
        quadraticTo(cx - r, cy, cx - r * 0.8f, cy + h * 0.05f)
        lineTo(cx + r * 0.8f, cy + h * 0.03f)
        quadraticTo(cx + r, cy, cx + r * 0.8f, cy - h * 0.03f)
        close()
    }

    if (isGlowing) {
        // Halo lumineux
        drawPath(crossPath, Color(0xFFFFD700).copy(alpha = 0.2f * glowAlpha), style = Stroke(width = 30f))
    }
    
    // Corps de la croix avec dégradé
    if (isGlowing) {
        drawPath(crossPath, GoldGradient)
    } else {
        drawPath(crossPath, DarkGold)
    }
    
    // Fleur-de-lis aux extrémités
    val fSize = w * 0.15f
    val positions = listOf(
        Offset(cx, cy - r * 0.9f), Offset(cx, cy + r * 0.9f),
        Offset(cx - r * 0.9f, cy), Offset(cx + r * 0.9f, cy)
    )
    positions.forEach { pos ->
        drawCircle(if (isGlowing) Color(0xFFFFD700) else DarkGold, radius = fSize / 4, center = pos)
    }
}

fun DrawScope.drawMusketeerHat() {
    val w = size.width
    val h = size.height
    
    val hatBody = Path().apply {
        moveTo(w * 0.2f, h * 0.55f)
        cubicTo(w * 0.2f, h * 0.35f, w * 0.8f, h * 0.35f, w * 0.8f, h * 0.55f)
        quadraticTo(w * 0.95f, h * 0.65f, w * 0.5f, h * 0.7f)
        quadraticTo(w * 0.05f, h * 0.65f, w * 0.2f, h * 0.55f)
    }
    
    // Ombre
    translate(5f, 5f) {
        drawPath(hatBody, Color.Black.copy(alpha = 0.5f))
    }
    
    drawPath(hatBody, DeepBlack)
    drawPath(hatBody, Color(0xFFD4AF37).copy(alpha = 0.6f), style = Stroke(width = 2f))
    
    val plume = Path().apply {
        moveTo(w * 0.35f, h * 0.45f)
        cubicTo(w * 0.1f, h * 0.2f, w * 0.3f, h * 0.05f, w * 0.5f, h * 0.1f)
        quadraticTo(w * 0.4f, h * 0.3f, w * 0.4f, h * 0.45f)
    }
    drawPath(plume, Color.White.copy(alpha = 0.8f))
}

fun DrawScope.drawMusketeerMoustache() {
    val w = size.width
    val h = size.height
    val path = Path().apply {
        moveTo(w * 0.5f, h * 0.72f)
        cubicTo(w * 0.35f, h * 0.72f, w * 0.15f, h * 0.65f, w * 0.1f, h * 0.8f)
        quadraticTo(w * 0.3f, h * 0.77f, w * 0.5f, h * 0.75f)
        cubicTo(w * 0.7f, h * 0.75f, w * 0.9f, h * 0.65f, w * 0.95f, h * 0.8f)
        quadraticTo(w * 0.65f, h * 0.77f, w * 0.5f, h * 0.72f)
    }
    drawPath(path, DeepBlack)
}

fun DrawScope.drawScabbard() {
    val w = size.width
    val h = size.height
    val path = Path().apply {
        moveTo(w * 0.46f, h * 0.82f)
        lineTo(w * 0.54f, h * 0.82f)
        lineTo(w * 0.52f, h * 0.98f)
        lineTo(w * 0.48f, h * 0.98f)
        close()
    }
    drawPath(path, DeepBlack)
    drawPath(path, Color(0xFFD4AF37), style = Stroke(width = 2f))
}

fun DrawScope.drawRapier(yOffset: Float, isReflecting: Boolean) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f
    val base = h * 0.82f + yOffset
    
    drawLine(
        brush = Brush.verticalGradient(listOf(Color.White, Steel, Color.Gray)),
        start = Offset(cx, base),
        end = Offset(cx, base - h * 0.65f),
        strokeWidth = 3f
    )
    
    drawArc(
        color = Color(0xFFD4AF37),
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(cx - w * 0.1f, base - h * 0.05f),
        size = Size(w * 0.2f, h * 0.1f),
        style = Stroke(width = 3f)
    )
    
    if (isReflecting) {
        val tipY = base - h * 0.65f
        drawCircle(
            brush = Brush.radialGradient(listOf(Color.White, Color.Transparent)),
            radius = 15f,
            center = Offset(cx, tipY)
        )
    }
}
