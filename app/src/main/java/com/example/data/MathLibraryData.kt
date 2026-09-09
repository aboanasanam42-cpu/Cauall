package com.example.data

data class MathTopic(
    val id: String,
    val title: String,
    val category: String,
    val formula: String,
    val latexDisplay: String,
    val explanation: String,
    val application: String,
    val parameters: List<String> = emptyList(),
    val sampleValues: Map<String, Double> = emptyMap(),
    val calculateFunction: ((Map<String, Double>) -> Double)? = null
)

object MathLibraryData {

    const val CAT_ALL = "الكل"
    const val CAT_ALGEBRA = "الجبر"
    const val CAT_CALCULUS = "التفاضل والتكامل"
    const val CAT_GEOMETRY = "الهندسة الفراغية"
    const val CAT_STATS = "الإحصاء والاحتمالات"
    const val CAT_CONSTANTS = "الثوابت الرياضية"

    val categories = listOf(
        CAT_ALL,
        CAT_ALGEBRA,
        CAT_CALCULUS,
        CAT_GEOMETRY,
        CAT_STATS,
        CAT_CONSTANTS
    )

    val topics: List<MathTopic> = listOf(
        // Calculus
        MathTopic(
            id = "gaussian_integral",
            title = "تكامل غاوس (دالة الخطأ)",
            category = CAT_CALCULUS,
            formula = "f(x) = ∫ e^(-x²) dx",
            latexDisplay = "∫_{-∞}^{∞} e^{-x²} dx = √π ≈ 1.77245",
            explanation = "تكامل غاوس هو أحد أهم التكاملات المحددة في التحليل الرياضي ونظرية الاحتمالات، ويشكل حجر الأساس للتوزيع الطبيعي وميكانيكا الكم.",
            application = "يستخدم في حساب دالة الخطأ erf(x)، ونظرية الأوتار، ومعالجة الإشارات الرقمية، ونمذجة الانتشار الحراري.",
            parameters = listOf("x"),
            sampleValues = mapOf("x" to 1.0)
        ),
        MathTopic(
            id = "taylor_series",
            title = "متسلسلة تايلور",
            category = CAT_CALCULUS,
            formula = "f(x) = ∑ (f^(n)(a) / n!) * (x - a)^n",
            latexDisplay = "f(x) = f(a) + f'(a)(x-a) + (f''(a)/2!)(x-a)² + ...",
            explanation = "تمثيل دالة رياضية كمجموع غير منتهٍ من الحدود المحسوبة من قيم مشتقات الدالة عند نقطة واحدة.",
            application = "تقريب الدوال المعقدة (المثلثية والأسية) في معالجات الحواسيب والهواتف الذكية وتطبيقات الذكاء الاصطناعي."
        ),
        MathTopic(
            id = "euler_identity",
            title = "متطابقة أويلر الأيقونية",
            category = CAT_CALCULUS,
            formula = "e^(i*π) + 1 = 0",
            latexDisplay = "e^{iπ} + 1 = 0",
            explanation = "توصف بأنها أجمل معادلة في تاريخ الرياضيات لأنها تجمع بين أهم خمسة ثوابت رياضية: 0, 1, e, i, π بعمليات الجمع والضرب والأسس.",
            application = "التحليل المركب، الهندسة الكهربائية والدوائر المترددة، ميكانيكا الكم."
        ),
        MathTopic(
            id = "derivative_definition",
            title = "تعريف المشتقة الأولى",
            category = CAT_CALCULUS,
            formula = "f'(x) = lim[h→0] (f(x+h) - f(x)) / h",
            latexDisplay = "f'(x) = lim_{h \\to 0} \\frac{f(x+h) - f(x)}{h}",
            explanation = "المشتقة تقيس المعدل اللحظي لتغير قيمة دالة ما بالنسبة لمتغيرها المستقل، وتمثل هندسياً ميل مماس المنحنى.",
            application = "حساب السرعات والتسارعات، خوارزميات الانحدار التدريجي (Gradient Descent) في تعلم الآلة."
        ),

        // Geometry
        MathTopic(
            id = "sphere_volume",
            title = "حجم ومساحة الكرة ثلاثية الأبعاد",
            category = CAT_GEOMETRY,
            formula = "V = (4/3) * π * r³ ,  A = 4 * π * r²",
            latexDisplay = "V = \\frac{4}{3}πr³, \\quad A = 4πr²",
            explanation = "الكرة هي المحل الهندسي لجميع النقاط في الفضاء ثلاثي الأبعاد التي تبعد مسافة ثابتة (نصف القطر r) عن نقطة المركز.",
            application = "حسابات الأجرام السماوية، تصميم الخزانات الكروية، ونمذجة المجالات الكهرومغناطيسية.",
            parameters = listOf("r"),
            sampleValues = mapOf("r" to 5.0),
            calculateFunction = { p -> (4.0 / 3.0) * Math.PI * Math.pow(p["r"] ?: 1.0, 3.0) }
        ),
        MathTopic(
            id = "cone_volume",
            title = "حجم ومساحة المخروط الدوراني",
            category = CAT_GEOMETRY,
            formula = "V = (1/3) * π * r² * h",
            latexDisplay = "V = \\frac{1}{3}πr²h, \\quad S = πr(r + \\sqrt{h² + r²})",
            explanation = "المخروط مجسم يتكون من قاعدة دائرية متصلة برأس (قمة) عبر سطح منحنٍ، وحجمه يساوي بالضبط ثلث حجم أسطوانة متطابقة القاعدة والارتفاع.",
            application = "حسابات الجيوديسيا والمساحة، الهندسة المعمارية، البصريات ومساقط الضوء.",
            parameters = listOf("r", "h"),
            sampleValues = mapOf("r" to 3.0, "h" to 7.0),
            calculateFunction = { p -> (1.0 / 3.0) * Math.PI * Math.pow(p["r"] ?: 1.0, 2.0) * (p["h"] ?: 1.0) }
        ),
        MathTopic(
            id = "pythagoras",
            title = "مبرهنة فيثاغورس",
            category = CAT_GEOMETRY,
            formula = "a² + b² = c²",
            latexDisplay = "c = \\sqrt{a² + b²}",
            explanation = "في أي مثلث قائم الزاوية، مساحة المربع المنشأ على الوتر تساوي مجموع مساحتي المربعين المنشأين على الضلعين الآخرين.",
            application = "حساب المسافات الإقليدية، الملاحة الجوية والبحرية، وتطوير محركات الرسوميات ثلاثية الأبعاد.",
            parameters = listOf("a", "b"),
            sampleValues = mapOf("a" to 3.0, "b" to 4.0),
            calculateFunction = { p -> Math.sqrt(Math.pow(p["a"] ?: 3.0, 2.0) + Math.pow(p["b"] ?: 4.0, 2.0)) }
        ),

        // Algebra
        MathTopic(
            id = "quadratic_formula",
            title = "القانون العام لحل المعادلة التربيعية",
            category = CAT_ALGEBRA,
            formula = "x = (-b ± √(b² - 4ac)) / (2a)",
            latexDisplay = "x = \\frac{-b \\pm \\sqrt{b² - 4ac}}{2a}, \\quad \\Delta = b² - 4ac",
            explanation = "صيغة جبرية دقيقة لحساب جذور المعادلة من الدرجة الثانية ax² + bx + c = 0، حيث يحدد المميز دلتا طبيعة الجذور (حقيقية أو مركبة).",
            application = "حساب المقذوفات في الفيزياء، تحليل الدوائر الكهربائية، نمذجة التكلفة والأرباح الاقتصادية.",
            parameters = listOf("a", "b", "c"),
            sampleValues = mapOf("a" to 1.0, "b" to -5.0, "c" to 6.0)
        ),
        MathTopic(
            id = "matrix_determinant",
            title = "محدد المصفوفة (Determinant)",
            category = CAT_ALGEBRA,
            formula = "det(A) = ad - bc",
            latexDisplay = "det \\begin{pmatrix} a & b \\\\ c & d \\end{pmatrix} = ad - bc",
            explanation = "قيمة عددية تحسب من عناصر المصفوفة المربعة، وتدل على قابلية المصفوفة للعكس ومقدار التمدد أو التقلص الهندسي في التحويلات الخطية.",
            application = "حل أنظمة المعادلات الخطية بطريقة كرامر، تحويلات الرسوميات الحاسوبية، وميكانيكا الموائع.",
            parameters = listOf("a", "b", "c", "d"),
            sampleValues = mapOf("a" to 4.0, "b" to 2.0, "c" to 1.0, "d" to 3.0),
            calculateFunction = { p -> (p["a"] ?: 0.0) * (p["d"] ?: 0.0) - (p["b"] ?: 0.0) * (p["c"] ?: 0.0) }
        ),
        MathTopic(
            id = "binomial_theorem",
            title = "مبرهنة ذات الحدين (نيوتن)",
            category = CAT_ALGEBRA,
            formula = "(x + y)ⁿ = ∑ [n! / (k!(n-k)!)] * x^(n-k) * y^k",
            latexDisplay = "(x + y)^n = \\sum_{k=0}^{n} \\binom{n}{k} x^{n-k} y^k",
            explanation = "صيغة لنشر قوى المقادير ذات الحدين لأي أس صحيح موجب أو حقيقي باستخدام المعاملات التوافيقية ومثلث باسكال.",
            application = "نظرية الاحتمالات، التوزيع ثنائي الحدين، تحليل الخوارزميات وتشفير البيانات."
        ),

        // Statistics
        MathTopic(
            id = "gaussian_distribution",
            title = "التوزيع الطبيعي (الغاوسي)",
            category = CAT_STATS,
            formula = "f(x) = (1 / (σ √(2π))) * e^(-(x - μ)² / (2σ²))",
            latexDisplay = "f(x) = \\frac{1}{\\sigma \\sqrt{2\\pi}} e^{-\\frac{1}{2}\\left(\\frac{x-\\mu}{\\sigma}\\right)^2}",
            explanation = "أهم توزيع احتمالي متصل في علم الإحصاء، يأخذ شكل الجرس المتماثل حول المتوسط الحسابي μ والانحراف المعياري σ.",
            application = "تحليل البيانات الضخمة، ضبط جودة التصنيع Six Sigma، علوم الجينات واختبارات الذكاء."
        ),
        MathTopic(
            id = "standard_deviation",
            title = "الانحراف المعياري والتباين",
            category = CAT_STATS,
            formula = "σ = √( (1/N) * ∑ (x_i - μ)² )",
            latexDisplay = "\\sigma = \\sqrt{ \\frac{1}{N} \\sum_{i=1}^{N} (x_i - \\mu)^2 }",
            explanation = "مقياس إحصائي يعبر عن مدى تشتت أو انتشار قيم عينة البيانات حول متوسطها الحسابي.",
            application = "قياس المخاطر وتقلبات الأسهم في البورصة، تقييم التجارب المخبرية في الفيزياء والكيمياء."
        ),
        MathTopic(
            id = "bayes_theorem",
            title = "مبرهنة بايز للاحتمال الشرطي",
            category = CAT_STATS,
            formula = "P(A|B) = [P(B|A) * P(A)] / P(B)",
            latexDisplay = "P(A|B) = \\frac{P(B|A) P(A)}{P(B)}",
            explanation = "قانون رياضي يصف احتمال وقوع حدث ما استناداً إلى المعرفة المسبقة بالظروف والقرائن ذات الصلة بالحدث.",
            application = "فلاتر البريد المزعج (Spam Filters)، التشخيص الطبي الاستدلالي، ونماذج تعلم الآلة البايزية."
        ),

        // Constants
        MathTopic(
            id = "constant_pi",
            title = "النسبة التقريبية ط (Pi - π)",
            category = CAT_CONSTANTS,
            formula = "π ≈ 3.141592653589793",
            latexDisplay = "π = \\frac{C}{d} = 3.141592653589793...",
            explanation = "النسبة بين محيط الدائرة إلى قطرها، وهو عدد غير نسبي وأصم لا يتكرر ولا ينتهي.",
            application = "كل حسابات الدوائر والكرات والموجات الصوتية والضوئية والفيزياء الفلكية."
        ),
        MathTopic(
            id = "constant_e",
            title = "العدد النيبيري (Euler's Number - e)",
            category = CAT_CONSTANTS,
            formula = "e = lim[n→∞] (1 + 1/n)ⁿ ≈ 2.718281828",
            latexDisplay = "e = \\sum_{n=0}^{\\infty} \\frac{1}{n!} = 2.718281828459...",
            explanation = "أساس اللوغاريتم الطبيعي، يمثل الحد الأقصى للنمو الأسي المركب المستمر.",
            application = "النمو السكاني، التحلل الإشعاعي للمواد، حساب الفائدة المركبة في البنوك."
        ),
        MathTopic(
            id = "constant_phi",
            title = "النسبة الذهبية (Golden Ratio - φ)",
            category = CAT_CONSTANTS,
            formula = "φ = (1 + √5) / 2 ≈ 1.6180339887",
            latexDisplay = "\\phi = \\frac{1 + \\sqrt{5}}{2} \\approx 1.6180339887",
            explanation = "نسبة رياضية فريدة بين قياسين، حيث نسبة المجموع إلى الأكبر تساوي نسبة الأكبر إلى الأصغر، وترتبط بمتتالية فيبوناتشي.",
            application = "العمارة الفرعونية والإغريقية، تكوين النباتات والمجرات الحلزونية، وتصميم الواجهات الجمالية."
        )
    )
}
