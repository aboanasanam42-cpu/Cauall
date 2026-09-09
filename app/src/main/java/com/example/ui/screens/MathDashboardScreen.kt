package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MathLibraryData
import com.example.ui.components.*
import com.example.ui.theme.*

/**
 * Main Math Dashboard Screen (شاشة البداية والواجهة الرئيسية).
 * Matches the exact Dark Sci-Fi theme and geometry layout from the provided design prompt and image.
 */
@Composable
fun MathDashboardScreen(
    onNavigateToOverview: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {}
) {
    var showSaveDialog by remember { mutableStateOf(false) }
    var selectedFormula by remember { mutableStateOf("f(x) = ∫ e^(-x²) dx") }

    val presetFormulas = remember {
        listOf(
            "f(x) = ∫ e^(-x²) dx",
            "V = (4/3)·π·r³ [الكرة]",
            "V = (1/3)·π·r²·h [المخروط]",
            "e^(i·π) + 1 = 0",
            "x = (-b ± √(b²-4ac)) / 2a",
            "σ = √( ∑(x-μ)² / N )"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpaceBlack)
    ) {
        // Floating Ambient Sci-Fi Mathematical Particles
        FloatingMathParticlesBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // شريط العنوان العلوي الأنيق
            NeonHeaderBar(
                title = "المكتبة الشاملة في الرياضيات",
                subtitle = "المكتبة",
                showBack = false,
                onBackClick = {}
            )

            // شريط الصيغ السريعة للتفاعل المباشر
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(presetFormulas) { formula ->
                    val isSelected = formula == selectedFormula
                    Surface(
                        onClick = { selectedFormula = formula },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Color(0xFF1F2B5C) else Color(0x33141C3D),
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = if (isSelected) NeonCyan else Color(0x44263366),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Text(
                            text = formula,
                            color = if (isSelected) NeonCyan else TextMuted,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // الحاوية الوسطى: شاشة العرض الرياضية ثلاثية الأبعاد والمحاكاة التفاعلية
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 8.dp)
                    .border(
                        width = 1.2.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF00E5FF), Color(0xFF7C4DFF))
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x28151B38))
            ) {
                Math3DCanvas(
                    activeFormula = selectedFormula,
                    onFormulaClick = {
                        // Tapping the formula switches to next preset
                        val nextIdx = (presetFormulas.indexOf(selectedFormula) + 1) % presetFormulas.size
                        selectedFormula = presetFormulas[nextIdx]
                    }
                )
            }

            // أزرار التحكم السفلية المتطابقة مع التصميم
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // زر نظرة عامة (Overview)
                NeonGlassButton(
                    text = "نظرة عامة",
                    icon = Icons.Default.Visibility,
                    neonColor = NeonCyan,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToOverview
                )

                Spacer(modifier = Modifier.width(10.dp))

                // زر الحفظ إلى الهاتف الدائري البارز (Save to Phone)
                NeonSaveCircularButton(
                    onClick = { showSaveDialog = true }
                )

                Spacer(modifier = Modifier.width(10.dp))

                // زر البحث (Search)
                NeonGlassButton(
                    text = "البحث",
                    icon = Icons.Default.Search,
                    neonColor = NeonMagenta,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToSearch
                )
            }

            // Developer Signature
            Text(
                text = "تصميم وبرمجة الدكتور / مالك الرميمة هاتف / 771134103",
                color = TextMuted.copy(alpha = 0.75f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
            )
        }

        // Save & Export Dialog
        if (showSaveDialog) {
            SaveExportDialog(
                currentFormula = selectedFormula,
                onDismiss = { showSaveDialog = false }
            )
        }
    }
}
