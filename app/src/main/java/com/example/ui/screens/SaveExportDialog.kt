package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import java.io.File

@Composable
fun SaveExportDialog(
    currentFormula: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var savedMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1738)),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(listOf(NeonCyan, NeonPurple)),
                    shape = RoundedCornerShape(24.dp)
                )
                .testTag("save_export_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Icon with Neon Glow
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF162354))
                        .border(1.5.dp, NeonCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "الحفظ إلى الهاتف",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "حفظ وتصدير بيانات المحاكاة والصيغ الرياضية إلى ذاكرة الجهاز",
                    color = TextMuted,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )

                // Current mathematical formula preview
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF18234D),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .border(1.dp, Color(0xFF2B3A75), RoundedCornerShape(14.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "الصيغة الرياضية الحالية:",
                            color = TextCyan,
                            fontSize = 11.sp
                        )
                        Text(
                            text = currentFormula,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                AnimatedVisibility(
                    visible = savedMessage != null,
                    enter = fadeIn() + slideInVertically()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x3300E676))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = savedMessage ?: "",
                            color = NeonGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action 1: Save to internal documents file on phone
                Button(
                    onClick = {
                        try {
                            val fileName = "Math_Simulation_${System.currentTimeMillis()}.txt"
                            val fileContent = buildString {
                                appendLine("=== المكتبة الشاملة في الرياضيات ===")
                                appendLine("تصميم وبرمجة الدكتور / مالك الرميمة هاتف / 771134103")
                                appendLine("التاريخ: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date())}")
                                appendLine("الصيغة: $currentFormula")
                                appendLine("تكامل غاوس: ∫ e^(-x²) dx = √π ≈ 1.77245")
                                appendLine("الكرة ثلاثية الأبعاد: V = (4/3)·π·r³")
                                appendLine("المخروط ثلاثي الأبعاد: V = (1/3)·π·r²·h")
                                appendLine("تم التصدير بنجاح عبر تطبيق المكتبة الشاملة في الرياضيات.")
                            }
                            val dir = context.getExternalFilesDir(null) ?: context.filesDir
                            val targetFile = File(dir, fileName)
                            targetFile.writeText(fileContent)
                            savedMessage = "تم حفظ الملف بنجاح في: ${targetFile.name}"
                            Toast.makeText(context, "تم حفظ الملف في ذاكرة الهاتف", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            savedMessage = "فشل الحفظ: ${e.message}"
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B0FF)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_file_button")
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("حفظ كملف بيانات في الهاتف", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action 2: Copy to Clipboard
                OutlinedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Math Formula", currentFormula)
                        clipboard.setPrimaryClip(clip)
                        savedMessage = "تم نسخ الصيغة الرياضية إلى الحافظة"
                        Toast.makeText(context, "تم النسخ إلى الحافظة", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(1.2.dp, NeonCyan, RoundedCornerShape(14.dp))
                        .testTag("copy_formula_button")
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, tint = NeonCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("نسخ الصيغة إلى الحافظة", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action 3: Share
                OutlinedButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "صيغة رياضية من المكتبة الشاملة في الرياضيات:\n$currentFormula\nf(x) = ∫ e^(-x²) dx"
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "مشاركة المعادلة الرياضية"))
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonMagenta),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(1.2.dp, NeonMagenta, RoundedCornerShape(14.dp))
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = NeonMagenta)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("مشاركة الصيغة مع التطبيقات", color = Color.White)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "تصميم وبرمجة الدكتور / مالك الرميمة هاتف / 771134103",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("إغلاق", color = TextMuted)
                }
            }
        }
    }
}
