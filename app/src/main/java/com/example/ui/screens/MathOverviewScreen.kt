package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MathLibraryData
import com.example.data.MathTopic
import com.example.math.MathEngine
import com.example.ui.components.FloatingMathParticlesBackground
import com.example.ui.components.NeonHeaderBar
import com.example.ui.theme.*

/**
 * Comprehensive Math Overview Screen (شاشة نظرة عامة).
 * Displays full mathematical sections, theorems, formulas, and interactive calculators.
 */
@Composable
fun MathOverviewScreen(
    onBack: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(MathLibraryData.CAT_ALL) }
    val filteredTopics = remember(selectedCategory) {
        if (selectedCategory == MathLibraryData.CAT_ALL) {
            MathLibraryData.topics
        } else {
            MathLibraryData.topics.filter { it.category == selectedCategory }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpaceBlack)
    ) {
        FloatingMathParticlesBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header Bar with back button
            NeonHeaderBar(
                title = "نظرة عامة على القوانين",
                subtitle = "المكتبة الرياضية",
                showBack = true,
                onBackClick = onBack
            )

            // Category Filter Pills
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(MathLibraryData.categories) { cat ->
                    val isSelected = cat == selectedCategory
                    Surface(
                        onClick = { selectedCategory = cat },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) NeonCyan.copy(alpha = 0.2f) else Color(0xFF131B3A),
                        modifier = Modifier
                            .border(
                                width = 1.2.dp,
                                color = if (isSelected) NeonCyan else Color(0xFF23305A),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .testTag("filter_chip_$cat")
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) NeonCyan else Color.White,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // Topics List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredTopics, key = { it.id }) { topic ->
                    MathTopicCard(topic = topic)
                }
            }
        }
    }
}

@Composable
private fun MathTopicCard(topic: MathTopic) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Calculator input states
    val paramInputs = remember(topic.id) {
        mutableStateMapOf<String, String>().apply {
            topic.sampleValues.forEach { (k, v) -> put(k, v.toString()) }
        }
    }
    var calculatedResult by remember(topic.id) { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF00E5FF).copy(alpha = 0.6f), Color(0xFF7C4DFF).copy(alpha = 0.6f))
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .testTag("math_topic_card_${topic.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x3B121A38))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Category Badge & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Formula", "${topic.title}\n${topic.formula}")
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "تم نسخ القانون إلى الحافظة", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "نسخ",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1C2752)
                ) {
                    Text(
                        text = topic.category,
                        color = NeonPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Topic Title
            Text(
                text = topic.title,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Formula Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0x66080E24),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.8.dp, Color(0xFF1E2D60), RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = topic.formula,
                        color = NeonCyan,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    if (topic.latexDisplay != topic.formula) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = topic.latexDisplay,
                            color = TextPurple,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Explanation
            Text(
                text = topic.explanation,
                color = Color(0xFFD0D7E8),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            // Expand / Collapse Details & Interactive Calculator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = NeonCyan
                )
                Text(
                    text = if (isExpanded) "إخفاء التفاصيل والحاسبة" else "عرض التطبيقات والحساب التفاعلي",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Divider(color = Color(0xFF223060), thickness = 0.8.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "التطبيقات والاستخدامات العلمية:",
                        color = NeonMagenta,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = topic.application,
                        color = TextMuted,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )

                    // Interactive Calculation Module
                    if (topic.calculateFunction != null || topic.id == "quadratic_formula") {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "محاكاة الحساب المباشر:",
                            color = NeonCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Inputs row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            topic.parameters.forEach { param ->
                                OutlinedTextField(
                                    value = paramInputs[param] ?: "",
                                    onValueChange = { paramInputs[param] = it },
                                    label = { Text(param, color = TextMuted, fontSize = 11.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = NeonCyan,
                                        unfocusedBorderColor = Color(0xFF26376E),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                            }
                        }

                        Button(
                            onClick = {
                                if (topic.id == "quadratic_formula") {
                                    val a = paramInputs["a"]?.toDoubleOrNull() ?: 1.0
                                    val b = paramInputs["b"]?.toDoubleOrNull() ?: 0.0
                                    val c = paramInputs["c"]?.toDoubleOrNull() ?: 0.0
                                    val sol = MathEngine.solveQuadratic(a, b, c)
                                    calculatedResult = "${sol.desc}\nالجذور: ${sol.roots.joinToString(" , ")}"
                                } else if (topic.calculateFunction != null) {
                                    val map = paramInputs.mapValues { it.value.toDoubleOrNull() ?: 0.0 }
                                    val res = topic.calculateFunction.invoke(map)
                                    calculatedResult = String.format(java.util.Locale.US, "النتيجة = %.4f", res)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF162B5C)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, NeonCyan, RoundedCornerShape(12.dp))
                        ) {
                            Icon(Icons.Default.Calculate, contentDescription = null, tint = NeonCyan)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("احسب النتيجة", color = Color.White)
                        }

                        if (calculatedResult != null) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF091433),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .border(1.dp, NeonGreen.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            ) {
                                Text(
                                    text = calculatedResult ?: "",
                                    color = NeonGreen,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
