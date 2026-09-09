package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
 * Math Search and Scientific Expression Evaluator Screen (شاشة البحث).
 */
@Composable
fun MathSearchScreen(
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var calcResult by remember { mutableStateOf<MathEngine.EvaluationResult?>(null) }
    val context = LocalContext.current

    // Quick symbol insertion shortcuts
    val mathSymbols = remember { listOf("π", "√", "²", "^", "÷", "×", "sin(", "cos(", "tan(", "ln(", "(", ")") }

    // Search results filtered across title, formula, category, and explanation
    val searchResults = remember(searchQuery) {
        val q = searchQuery.trim().lowercase()
        if (q.isEmpty()) {
            MathLibraryData.topics
        } else {
            MathLibraryData.topics.filter { topic ->
                topic.title.lowercase().contains(q) ||
                topic.formula.lowercase().contains(q) ||
                topic.category.lowercase().contains(q) ||
                topic.explanation.lowercase().contains(q) ||
                topic.application.lowercase().contains(q)
            }
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
            // Header Bar
            NeonHeaderBar(
                title = "محرك البحث الرياضي والحاسبة",
                subtitle = "البحث",
                showBack = true,
                onBackClick = onBack
            )

            // Sci-Fi Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    // Also try calculating expression live if containing mathematical operations
                    if (it.any { c -> c in "+-*/^()=√π" } || it.contains("sin") || it.contains("cos") || it.contains("sqrt")) {
                        calcResult = MathEngine.evaluate(it)
                    } else {
                        calcResult = null
                    }
                },
                placeholder = {
                    Text("ابحث عن قانون، نظرية، أو احسب (مثال: sin(pi/4) + 5^2)...", color = TextMuted, fontSize = 12.sp)
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = NeonMagenta)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            calcResult = null
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "مسح", tint = TextMuted)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .testTag("math_search_text_field"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonMagenta,
                    unfocusedBorderColor = Color(0xFF263366),
                    focusedContainerColor = Color(0x44141C3D),
                    unfocusedContainerColor = Color(0x33141C3D),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            // Quick Math Symbol Bar
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(mathSymbols) { sym ->
                    Surface(
                        onClick = {
                            searchQuery += sym
                            calcResult = MathEngine.evaluate(searchQuery)
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1B254E),
                        modifier = Modifier.border(0.8.dp, Color(0xFF2D3C75), RoundedCornerShape(10.dp))
                    ) {
                        Text(
                            text = sym,
                            color = NeonCyan,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Live Calculator Result Card (mXparser Engine)
            if (calcResult != null) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF0F1A3D),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .border(
                            1.2.dp,
                            if (calcResult?.success == true) NeonGreen else NeonPink,
                            RoundedCornerShape(14.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                val textToCopy = calcResult?.formatted ?: ""
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Result", textToCopy)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "تم نسخ النتيجة: $textToCopy", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "نسخ", tint = NeonCyan)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "الناتج الحسابي المباشر (mXparser):",
                                color = TextCyan,
                                fontSize = 11.sp
                            )
                            Text(
                                text = if (calcResult?.success == true) calcResult?.formatted ?: "" else calcResult?.error ?: "خطأ في الصيغة",
                                color = if (calcResult?.success == true) NeonGreen else NeonPink,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            // Search Results Count
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${searchResults.size} نتائج",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Text(
                    text = "القوانين والنظريات المطابقة",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Results List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(searchResults, key = { it.id }) { topic ->
                    SearchResultCard(topic = topic)
                }
            }
        }
    }
}

@Composable
private fun SearchResultCard(topic: MathTopic) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x3B151F44)),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF26366E), RoundedCornerShape(16.dp))
            .testTag("search_card_${topic.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Formula", "${topic.title}: ${topic.formula}")
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "تم نسخ القانون", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.ContentCopy,
                        contentDescription = "نسخ",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = topic.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF1E2C5E)
                    ) {
                        Text(
                            text = topic.category,
                            color = NeonMagenta,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0x66070E24),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = topic.formula,
                    color = NeonCyan,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = topic.explanation,
                color = TextMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.End,
                lineHeight = 17.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
