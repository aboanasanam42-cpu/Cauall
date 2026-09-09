package com.example.math

import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.License
import kotlin.math.*

/**
 * High-performance Mathematical Evaluation & Geometry Engine.
 */
object MathEngine {

    init {
        try {
            // Confirm non-commercial use for mXparser library
            License.iConfirmNonCommercialUse("MathLibraryApp")
        } catch (_: Throwable) {
            // Fallback gracefully
        }
    }

    data class EvaluationResult(
        val success: Boolean,
        val value: Double = Double.NaN,
        val formatted: String = "",
        val error: String? = null
    )

    /**
     * Evaluates a mathematical expression string using mXparser with intelligent syntax handling.
     */
    fun evaluate(exprStr: String): EvaluationResult {
        val trimmed = exprStr.trim()
        if (trimmed.isEmpty()) {
            return EvaluationResult(false, error = "الرجاء إدخال تعبير رياضي")
        }

        return try {
            // Clean common Arabic math symbols or alternate characters
            val sanitized = trimmed
                .replace("×", "*")
                .replace("÷", "/")
                .replace("π", "pi")
                .replace("√", "sqrt")
                .replace("،", ".")

            val expr = Expression(sanitized)
            val result = expr.calculate()

            if (result.isNaN()) {
                // Try fallback simple parser for basic arithmetic if mXparser returned NaN
                val fallback = evaluateSimple(sanitized)
                if (!fallback.isNaN()) {
                    EvaluationResult(true, fallback, formatNumber(fallback))
                } else {
                    val syntaxMsg = expr.errorMessage ?: "تعبير غير صالح"
                    EvaluationResult(false, error = "تعذر الحساب: $syntaxMsg")
                }
            } else {
                EvaluationResult(true, result, formatNumber(result))
            }
        } catch (e: Exception) {
            EvaluationResult(false, error = e.localizedMessage ?: "خطأ في الصيغة")
        }
    }

    private fun formatNumber(num: Double): String {
        return when {
            num.isInfinite() -> "∞"
            num.isNaN() -> "NaN"
            num == num.toLong().toDouble() -> num.toLong().toString()
            else -> String.format(java.util.Locale.US, "%.5f", num).trimEnd('0').trimEnd('.')
        }
    }

    /**
     * Fallback lightweight parser for standard arithmetic
     */
    private fun evaluateSimple(str: String): Double {
        return try {
            val s = str.replace(" ", "")
            when {
                s.contains("+") -> {
                    val parts = s.split("+", limit = 2)
                    evaluateSimple(parts[0]) + evaluateSimple(parts[1])
                }
                s.contains("-") && !s.startsWith("-") -> {
                    val parts = s.split("-", limit = 2)
                    evaluateSimple(parts[0]) - evaluateSimple(parts[1])
                }
                s.contains("*") -> {
                    val parts = s.split("*", limit = 2)
                    evaluateSimple(parts[0]) * evaluateSimple(parts[1])
                }
                s.contains("/") -> {
                    val parts = s.split("/", limit = 2)
                    evaluateSimple(parts[0]) / evaluateSimple(parts[1])
                }
                s.equals("pi", ignoreCase = true) -> Math.PI
                s.equals("e", ignoreCase = true) -> Math.E
                else -> s.toDoubleOrNull() ?: Double.NaN
            }
        } catch (_: Exception) {
            Double.NaN
        }
    }

    /**
     * Calculates quadratic equation roots: ax^2 + bx + c = 0
     */
    fun solveQuadratic(a: Double, b: Double, c: Double): QuadraticSolution {
        if (a == 0.0) {
            return if (b != 0.0) {
                val root = -c / b
                QuadraticSolution(roots = listOf(formatNumber(root)), discriminant = 0.0, desc = "معادلة خطية: جذر وحيد")
            } else {
                QuadraticSolution(roots = emptyList(), discriminant = 0.0, desc = "لا يوجد حل")
            }
        }
        val delta = (b * b) - (4 * a * c)
        return when {
            delta > 0 -> {
                val r1 = (-b + sqrt(delta)) / (2 * a)
                val r2 = (-b - sqrt(delta)) / (2 * a)
                QuadraticSolution(
                    roots = listOf(formatNumber(r1), formatNumber(r2)),
                    discriminant = delta,
                    desc = "يوجد جذران حقيقيان مختلفان (Δ > 0)"
                )
            }
            delta == 0.0 -> {
                val r = -b / (2 * a)
                QuadraticSolution(
                    roots = listOf(formatNumber(r)),
                    discriminant = 0.0,
                    desc = "يوجد جذر حقيقي مضاعف (Δ = 0)"
                )
            }
            else -> {
                val real = -b / (2 * a)
                val imag = sqrt(-delta) / (2 * a)
                val r1 = "${formatNumber(real)} + ${formatNumber(imag)}i"
                val r2 = "${formatNumber(real)} - ${formatNumber(imag)}i"
                QuadraticSolution(
                    roots = listOf(r1, r2),
                    discriminant = delta,
                    desc = "جذران مركبان مترافقان (Δ < 0)"
                )
            }
        }
    }

    data class QuadraticSolution(
        val roots: List<String>,
        val discriminant: Double,
        val desc: String
    )

    /**
     * Numerical integration using Simpson's 1/3 Rule
     */
    fun integrateSimpsons(func: (Double) -> Double, a: Double, b: Double, n: Int = 100): Double {
        val steps = if (n % 2 == 0) n else n + 1
        val h = (b - a) / steps
        var sum = func(a) + func(b)

        for (i in 1 until steps) {
            val x = a + i * h
            sum += if (i % 2 == 0) 2 * func(x) else 4 * func(x)
        }
        return (h / 3.0) * sum
    }

    // 3D Projection & Geometry Model
    data class Point3D(val x: Float, val y: Float, val z: Float)
    data class Point2D(val x: Float, val y: Float)

    /**
     * Projects a 3D coordinate to 2D screen with rotation around X and Y axes
     */
    fun project3DTo2D(
        point: Point3D,
        rotX: Float,
        rotY: Float,
        centerX: Float,
        centerY: Float,
        scale: Float
    ): Point2D {
        val radX = Math.toRadians(rotX.toDouble()).toFloat()
        val radY = Math.toRadians(rotY.toDouble()).toFloat()

        // Rotate around Y axis
        val cosY = cos(radY)
        val sinY = sin(radY)
        val x1 = point.x * cosY + point.z * sinY
        val y1 = point.y
        val z1 = -point.x * sinY + point.z * cosY

        // Rotate around X axis
        val cosX = cos(radX)
        val sinX = sin(radX)
        val y2 = y1 * cosX - z1 * sinX
        val z2 = y1 * sinX + z1 * cosX

        // Perspective projection factor
        val distance = 400f
        val factor = distance / (distance + z2)

        val screenX = centerX + x1 * scale * factor
        val screenY = centerY - y2 * scale * factor

        return Point2D(screenX, screenY)
    }

    /**
     * Generates 3D sphere wireframe lines (latitudes and longitudes)
     */
    fun generateSphereLines(radius: Float, latCount: Int = 6, lonCount: Int = 8): List<List<Point3D>> {
        val lines = mutableListOf<List<Point3D>>()

        // Latitude circles
        for (i in 1..latCount) {
            val phi = -PI.toFloat() / 2f + i * (PI.toFloat() / (latCount + 1))
            val rLat = radius * cos(phi)
            val y = radius * sin(phi)
            val ring = mutableListOf<Point3D>()
            val steps = 24
            for (j in 0..steps) {
                val theta = j * (2 * PI.toFloat() / steps)
                ring.add(Point3D(rLat * cos(theta), y, rLat * sin(theta)))
            }
            lines.add(ring)
        }

        // Longitude circles
        for (i in 0 until lonCount) {
            val theta = i * (PI.toFloat() / lonCount)
            val circle = mutableListOf<Point3D>()
            val steps = 24
            for (j in 0..steps) {
                val phi = j * (2 * PI.toFloat() / steps)
                val x = radius * cos(phi) * cos(theta)
                val y = radius * sin(phi)
                val z = radius * cos(phi) * sin(theta)
                circle.add(Point3D(x, y, z))
            }
            lines.add(circle)
        }

        return lines
    }

    /**
     * Generates 3D cone wireframe lines (base circle, ribs, apex)
     */
    fun generateConeLines(radius: Float, height: Float, ribs: Int = 10): List<List<Point3D>> {
        val lines = mutableListOf<List<Point3D>>()
        val apex = Point3D(0f, height / 2f, 0f)
        val baseCenter = Point3D(0f, -height / 2f, 0f)

        // Base circle
        val baseCircle = mutableListOf<Point3D>()
        val steps = 24
        for (i in 0..steps) {
            val theta = i * (2 * PI.toFloat() / steps)
            baseCircle.add(Point3D(radius * cos(theta), -height / 2f, radius * sin(theta)))
        }
        lines.add(baseCircle)

        // Intermediate rings for 3D body shading
        val midCircle = mutableListOf<Point3D>()
        val midR = radius * 0.5f
        for (i in 0..steps) {
            val theta = i * (2 * PI.toFloat() / steps)
            midCircle.add(Point3D(midR * cos(theta), 0f, midR * sin(theta)))
        }
        lines.add(midCircle)

        // Ribs from apex to base
        for (i in 0 until ribs) {
            val theta = i * (2 * PI.toFloat() / ribs)
            val basePt = Point3D(radius * cos(theta), -height / 2f, radius * sin(theta))
            lines.add(listOf(apex, basePt))
        }

        return lines
    }

    /**
     * Generates 3D coordinate grid plane lines
     */
    fun generateGridLines(size: Float, step: Float): List<List<Point3D>> {
        val lines = mutableListOf<List<Point3D>>()
        var current = -size
        while (current <= size) {
            // Lines parallel to X axis on plane Y=0
            lines.add(listOf(Point3D(-size, 0f, current), Point3D(size, 0f, current)))
            // Lines parallel to Z axis on plane Y=0
            lines.add(listOf(Point3D(current, 0f, -size), Point3D(current, 0f, size)))
            current += step
        }
        return lines
    }
}
