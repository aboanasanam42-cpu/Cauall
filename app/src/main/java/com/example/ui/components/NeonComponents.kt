package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Sci-Fi Top Header Bar with customizable back/action icon and glowing Arabic title.
 */
@Composable
fun NeonHeaderBar(
    title: String = "المكتبة الشاملة في الرياضيات",
    subtitle: String = "المكتبة",
    showBack: Boolean = true,
    onBackClick: () -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back or Action Icon Button on Left
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(GlassSurfaceDark)
                .border(1.dp, Color(0xFF263366), RoundedCornerShape(14.dp))
                .testTag("top_bar_back_button")
        ) {
            Icon(
                imageVector = if (showBack) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.Menu,
                contentDescription = if (showBack) "الرجوع" else "القائمة",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Center / Right App Title & Glowing Badge
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = subtitle,
                    color = NeonCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = NeonMagenta,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }

        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(8.dp))
            trailingIcon()
        }
    }
}

/**
 * Sci-Fi Glassmorphic Card Container with Neon Gradient Border.
 */
@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    borderColor: List<Color> = listOf(CardBorderGradientStart, CardBorderGradientEnd),
    content: @Composable BoxScope.() -> Unit
) {
    Card(
        modifier = modifier
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(borderColor),
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x2E111836))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            content = content
        )
    }
}

/**
 * Bottom Glassmorphic Navigation Button (Overview / Search) matching screenshot design.
 */
@Composable
fun NeonGlassButton(
    text: String,
    icon: ImageVector,
    neonColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF141C3D),
        modifier = modifier
            .height(58.dp)
            .border(1.2.dp, neonColor, RoundedCornerShape(18.dp))
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = neonColor.copy(alpha = 0.5f),
                ambientColor = neonColor.copy(alpha = 0.2f)
            )
            .testTag("neon_btn_${text}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = neonColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Center Radiant Circular "Save to Phone" Button with multilayer glow and pulsating halo.
 */
@Composable
fun NeonSaveCircularButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_save")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "save_scale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(86.dp)
            .testTag("neon_save_to_phone_button")
    ) {
        // Outer glowing halo
        Canvas(modifier = Modifier.size(86.dp * pulseScale)) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonCyan.copy(alpha = 0.45f), Color.Transparent),
                    center = center,
                    radius = size.minDimension / 2f
                )
            )
            // Outer cyan border ring
            drawCircle(
                color = NeonCyan,
                radius = (size.minDimension / 2f) - 3.dp.toPx(),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // Inner glowing solid button
        Button(
            onClick = onClick,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B0FF)),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .size(72.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    spotColor = NeonCyan,
                    ambientColor = NeonCyan
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "حفظ إلى الهاتف",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "الحفظ إلى",
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 11.sp
                )
                Text(
                    text = "الهاتف",
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 11.sp
                )
            }
        }
    }
}

/**
 * Ambient Animated Sci-Fi Mathematical Floating Particles
 */
@Composable
fun FloatingMathParticlesBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Subtle background grid points
        val gridStep = 45.dp.toPx()
        var x = 0f
        while (x < width) {
            var y = 0f
            while (y < height) {
                drawCircle(
                    color = Color(0x1200E5FF),
                    radius = 1.dp.toPx(),
                    center = Offset(x, y)
                )
                y += gridStep
            }
            x += gridStep
        }

        // Glowing particle clusters
        val particles = listOf(
            Triple(0.12f, 0.25f, NeonCyan),
            Triple(0.88f, 0.32f, NeonPurple),
            Triple(0.08f, 0.65f, NeonMagenta),
            Triple(0.92f, 0.70f, NeonCyan),
            Triple(0.20f, 0.88f, NeonBlue),
            Triple(0.80f, 0.90f, NeonPurple),
            Triple(0.50f, 0.08f, NeonPink)
        )

        particles.forEachIndexed { index, (px, py, color) ->
            val rad = Math.toRadians((phase + index * 50).toDouble())
            val dx = (kotlin.math.sin(rad) * 12.dp.toPx()).toFloat()
            val dy = (kotlin.math.cos(rad) * 12.dp.toPx()).toFloat()
            val center = Offset(width * px + dx, height * py + dy)

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(color.copy(alpha = 0.35f), Color.Transparent),
                    center = center,
                    radius = 24.dp.toPx()
                ),
                center = center,
                radius = 24.dp.toPx()
            )
            drawCircle(
                color = color.copy(alpha = 0.7f),
                radius = 2.dp.toPx(),
                center = center
            )
        }
    }
}
