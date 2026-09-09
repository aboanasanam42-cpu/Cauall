package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.MathEngine
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Interactive 3D Mathematical Canvas and Sci-Fi Simulation Dashboard.
 * Accurately reproduces the visual elements from the official design screenshot:
 * - Upper harmonic waves, progress dials, formulas, and bar graphs
 * - Central 3D wireframe Cone, Sphere, and Isometric Coordinate System
 * - Formula notations and live interactive touch rotation.
 */
@Composable
fun Math3DCanvas(
    modifier: Modifier = Modifier,
    activeFormula: String = "f(x) = ∫ e^(-x²) dx",
    onFormulaClick: () -> Unit = {}
) {
    // Rotation state driven by touch and subtle idle animation
    var manualRotX by remember { mutableFloatStateOf(18f) }
    var manualRotY by remember { mutableFloatStateOf(28f) }

    val infiniteTransition = rememberInfiniteTransition(label = "idle_rot")
    val idleAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "idle"
    )

    // Dynamic wave phase
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    manualRotY += dragAmount.x * 0.45f
                    manualRotX = (manualRotX - dragAmount.y * 0.45f).coerceIn(-65f, 65f)
                }
            }
    ) {
        // Core Mathematical Graphics Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. TOP SECTION: Harmonic Oscillation Waves & Bar Charts
            val waveTopY = height * 0.12f
            val waveHeight = height * 0.08f

            // Cyan Curve
            val cyanPath = Path()
            val magentaPath = Path()
            val pointsCount = 60
            val startX = width * 0.06f
            val endX = width * 0.62f
            val waveWidth = endX - startX

            for (i in 0..pointsCount) {
                val progress = i.toFloat() / pointsCount
                val px = startX + progress * waveWidth
                val rad = progress * (3 * Math.PI.toFloat()) + wavePhase

                val pyCyan = waveTopY + sin(rad) * waveHeight * 0.4f
                val pyMagenta = waveTopY + cos(rad * 0.8f + 0.5f) * waveHeight * 0.45f

                if (i == 0) {
                    cyanPath.moveTo(px, pyCyan)
                    magentaPath.moveTo(px, pyMagenta)
                } else {
                    cyanPath.lineTo(px, pyCyan)
                    magentaPath.lineTo(px, pyMagenta)
                }
            }

            // Draw wave strokes with glow
            drawPath(
                path = cyanPath,
                color = NeonCyan.copy(alpha = 0.85f),
                style = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round)
            )
            drawPath(
                path = magentaPath,
                color = NeonMagenta.copy(alpha = 0.85f),
                style = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round)
            )

            // Mini bar charts on top right
            val barStartX = width * 0.76f
            val barBaseY = height * 0.18f
            val barWidth = 4.dp.toPx()
            val barSpacing = 7.dp.toPx()
            val barHeights = listOf(14.dp.toPx(), 22.dp.toPx(), 36.dp.toPx(), 28.dp.toPx(), 32.dp.toPx())

            barHeights.forEachIndexed { idx, bh ->
                val bx = barStartX + idx * barSpacing
                val col = if (idx % 2 == 0) NeonCyan else NeonMagenta
                drawLine(
                    color = col,
                    start = Offset(bx, barBaseY),
                    end = Offset(bx, barBaseY - bh),
                    strokeWidth = barWidth,
                    cap = StrokeCap.Round
                )
            }

            // 2. MIDDLE SECTION: 3D Geometry Models Projection
            val effectiveRotX = manualRotX
            val effectiveRotY = manualRotY + (idleAngle * 0.15f)

            // Model A: 3D Wireframe Cone on the Left
            val coneCenter = Offset(width * 0.22f, height * 0.46f)
            val coneLines = MathEngine.generateConeLines(radius = 36f, height = 75f, ribs = 8)

            coneLines.forEach { line3D ->
                if (line3D.size >= 2) {
                    val p2DStart = MathEngine.project3DTo2D(
                        line3D[0],
                        effectiveRotX,
                        effectiveRotY,
                        coneCenter.x,
                        coneCenter.y,
                        scale = 1.0f
                    )
                    for (k in 1 until line3D.size) {
                        val p2DEnd = MathEngine.project3DTo2D(
                            line3D[k],
                            effectiveRotX,
                            effectiveRotY,
                            coneCenter.x,
                            coneCenter.y,
                            scale = 1.0f
                        )
                        drawLine(
                            brush = Brush.linearGradient(
                                colors = listOf(NeonCyan.copy(alpha = 0.9f), NeonPurple.copy(alpha = 0.7f)),
                                start = Offset(p2DStart.x, p2DStart.y),
                                end = Offset(p2DEnd.x, p2DEnd.y)
                            ),
                            start = Offset(p2DStart.x, p2DStart.y),
                            end = Offset(p2DEnd.x, p2DEnd.y),
                            strokeWidth = 1.6.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            // Model B: 3D Wireframe Sphere in the Center
            val sphereCenter = Offset(width * 0.50f, height * 0.46f)
            val sphereLines = MathEngine.generateSphereLines(radius = 42f, latCount = 5, lonCount = 6)

            sphereLines.forEach { line3D ->
                if (line3D.size >= 2) {
                    val p2DStart = MathEngine.project3DTo2D(
                        line3D[0],
                        effectiveRotX,
                        effectiveRotY + 25f,
                        sphereCenter.x,
                        sphereCenter.y,
                        scale = 1.0f
                    )
                    for (k in 1 until line3D.size) {
                        val p2DEnd = MathEngine.project3DTo2D(
                            line3D[k],
                            effectiveRotX,
                            effectiveRotY + 25f,
                            sphereCenter.x,
                            sphereCenter.y,
                            scale = 1.0f
                        )
                        drawLine(
                            brush = Brush.linearGradient(
                                colors = listOf(NeonMagenta.copy(alpha = 0.85f), NeonCyan.copy(alpha = 0.85f)),
                                start = Offset(p2DStart.x, p2DStart.y),
                                end = Offset(p2DEnd.x, p2DEnd.y)
                            ),
                            start = Offset(p2DStart.x, p2DStart.y),
                            end = Offset(p2DEnd.x, p2DEnd.y),
                            strokeWidth = 1.4.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            // Model C: 3D Isometric Coordinate Plane Grid on the Right
            val gridCenter = Offset(width * 0.78f, height * 0.52f)
            val gridLines = MathEngine.generateGridLines(size = 40f, step = 14f)

            gridLines.forEach { line3D ->
                val p1 = MathEngine.project3DTo2D(
                    line3D[0],
                    rotX = 35f,
                    rotY = 45f + (effectiveRotY * 0.2f),
                    gridCenter.x,
                    gridCenter.y,
                    scale = 0.85f
                )
                val p2 = MathEngine.project3DTo2D(
                    line3D[1],
                    rotX = 35f,
                    rotY = 45f + (effectiveRotY * 0.2f),
                    gridCenter.x,
                    gridCenter.y,
                    scale = 0.85f
                )
                drawLine(
                    color = NeonCyan.copy(alpha = 0.7f),
                    start = Offset(p1.x, p1.y),
                    end = Offset(p2.x, p2.y),
                    strokeWidth = 1.2.dp.toPx()
                )
            }

            // Coordinate Axis Vectors on the 3D Grid (X, Y, Z)
            val origin = MathEngine.project3DTo2D(
                MathEngine.Point3D(0f, 0f, 0f),
                35f,
                45f + (effectiveRotY * 0.2f),
                gridCenter.x,
                gridCenter.y,
                scale = 0.85f
            )
            val axisX = MathEngine.project3DTo2D(
                MathEngine.Point3D(50f, 0f, 0f),
                35f,
                45f + (effectiveRotY * 0.2f),
                gridCenter.x,
                gridCenter.y,
                scale = 0.85f
            )
            val axisY = MathEngine.project3DTo2D(
                MathEngine.Point3D(0f, 50f, 0f),
                35f,
                45f + (effectiveRotY * 0.2f),
                gridCenter.x,
                gridCenter.y,
                scale = 0.85f
            )
            val axisZ = MathEngine.project3DTo2D(
                MathEngine.Point3D(0f, 0f, 50f),
                35f,
                45f + (effectiveRotY * 0.2f),
                gridCenter.x,
                gridCenter.y,
                scale = 0.85f
            )

            drawLine(NeonCyan, Offset(origin.x, origin.y), Offset(axisX.x, axisX.y), strokeWidth = 2.dp.toPx())
            drawLine(NeonMagenta, Offset(origin.x, origin.y), Offset(axisY.x, axisY.y), strokeWidth = 2.dp.toPx())
            drawLine(NeonPurple, Offset(origin.x, origin.y), Offset(axisZ.x, axisZ.y), strokeWidth = 2.dp.toPx())
        }

        // Overlay Text, Mathematical Equations and Interactive Labels
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Analytics Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Wave Analytics Tag
                Column {
                    Text(
                        text = "300  27%   65%",
                        color = TextCyan,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "∑ [f(t) · dt] osc",
                        color = TextMuted,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Mathematical formula block from screenshot
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "a = √(R-k/n) = x²/x² + 1²/2³ · R = 0",
                        color = TextCyan,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "= n (2E + 3Δt)¹.⁹ + 5·(sᵉ)",
                        color = TextPurple,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Center Floating Mathematical Symbols (matching the screenshot)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "π",
                    color = NeonCyan.copy(alpha = 0.8f),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "x = 0.5\ny = 12 + 6.1 f²x0",
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Σ",
                    color = NeonPurple.copy(alpha = 0.85f),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Bottom Formula Display Container
            Surface(
                onClick = onFormulaClick,
                shape = RoundedCornerShape(16.dp),
                color = Color(0x660B1229),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "x₂ = z(4x+3)ʳ + 6² + 2 ÷ 30",
                            color = TextCyan,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ViewInAr,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "تدوير ثلاثي الأبعاد باللمس",
                                color = TextMuted,
                                fontSize = 9.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = activeFormula,
                        color = NeonCyan,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "[منصة المحاكاة والرسم البياني الرياضي ثلاثي الأبعاد]",
                        color = TextPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
