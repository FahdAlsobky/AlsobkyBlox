package com.example.data.repository

import com.example.data.model.BloxFruit
import com.example.data.model.FruitRarity
import com.example.data.model.FruitTrend
import com.example.data.model.FruitType

object FruitRepository {

    val allItems: List<BloxFruit> = listOf(
        // === MYTHICAL FRUITS ===
        BloxFruit(
            id = "kitsune",
            name = "Kitsune",
            arabicName = "كيتسوني",
            rarity = FruitRarity.MYTHICAL,
            fruitType = FruitType.BEAST,
            beliPrice = 8_000_000L,
            robuxPrice = 4000,
            physicalValue = 125_000_000L,
            permValue = 350_000_000L,
            demand = 10,
            trend = FruitTrend.HYPED,
            notes = "أعلى قيمة وطلب في اللعبة حالياً، تدمير وسرعة خارقة في الـ PvP"
        ),
        BloxFruit(
            id = "dragon",
            name = "Dragon (Eastern)",
            arabicName = "تنين",
            rarity = FruitRarity.MYTHICAL,
            fruitType = FruitType.BEAST,
            beliPrice = 5_000_000L,
            robuxPrice = 3500,
            physicalValue = 110_000_000L,
            permValue = 340_000_000L,
            demand = 10,
            trend = FruitTrend.HYPED,
            notes = "الطلب عليها في أعلى مستوياته تحسباً للتحديث والـ Rework"
        ),
        BloxFruit(
            id = "leopard",
            name = "Leopard",
            arabicName = "ليوبارد (الفهد)",
            rarity = FruitRarity.MYTHICAL,
            fruitType = FruitType.BEAST,
            beliPrice = 5_000_000L,
            robuxPrice = 3000,
            physicalValue = 40_000_000L,
            permValue = 220_000_000L,
            demand = 9,
            trend = FruitTrend.STABLE,
            notes = "فاكهة PvP ممتازة وسريعة، عملة تداول أساسية في الصفقات الكبرى"
        ),
        BloxFruit(
            id = "dough",
            name = "Dough",
            arabicName = "دوغ (العجين)",
            rarity = FruitRarity.MYTHICAL,
            fruitType = FruitType.NATURAL,
            beliPrice = 2_800_000L,
            robuxPrice = 2400,
            physicalValue = 25_000_000L,
            permValue = 190_000_000L,
            demand = 10,
            trend = FruitTrend.HYPED,
            awakening = true,
            notes = "الفاكهة المفضلة لكومبوهات الـ PvP مع الإيقاظ V2 الكامل"
        ),
        BloxFruit(
            id = "t_rex",
            name = "T-Rex",
            arabicName = "تي ريكس (الديناصور)",
            rarity = FruitRarity.MYTHICAL,
            fruitType = FruitType.BEAST,
            beliPrice = 2_700_000L,
            robuxPrice = 2350,
            physicalValue = 20_000_000L,
            permValue = 180_000_000L,
            demand = 8,
            trend = FruitTrend.STABLE,
            notes = "قريبة من الدوغ والماموث في القوة، سهلة الاستخدام جداً"
        ),
        BloxFruit(
            id = "mammoth",
            name = "Mammoth",
            arabicName = "ماموث",
            rarity = FruitRarity.MYTHICAL,
            fruitType = FruitType.BEAST,
            beliPrice = 2_700_000L,
            robuxPrice = 2350,
            physicalValue = 12_500_000L,
            permValue = 160_000_000L,
            demand = 7,
            trend = FruitTrend.DROPPING,
            notes = "دفاع قوي وهجوم مستمر بنمط الدهس، لكن قيمتها هبطت مؤخراً"
        ),
        BloxFruit(
            id = "spirit",
            name = "Spirit",
            arabicName = "سبيريت (الأرواح)",
            rarity = FruitRarity.MYTHICAL,
            fruitType = FruitType.NATURAL,
            beliPrice = 3_400_000L,
            robuxPrice = 2550,
            physicalValue = 10_000_000L,
            permValue = 150_000_000L,
            demand = 7,
            trend = FruitTrend.STABLE,
            notes = "فاكهة تكتيكية بمهارات الجليد والنار، توازن رائع في القتال"
        ),
        BloxFruit(
            id = "venom",
            name = "Venom",
            arabicName = "فينوم (السم)",
            rarity = FruitRarity.MYTHICAL,
            fruitType = FruitType.NATURAL,
            beliPrice = 3_000_000L,
            robuxPrice = 2450,
            physicalValue = 9_000_000L,
            permValue = 145_000_000L,
            demand = 7,
            trend = FruitTrend.STABLE,
            notes = "ضرر السم التراكمي وتحول التنين السام فتاك في المعارك"
        ),
        BloxFruit(
            id = "shadow",
            name = "Shadow",
            arabicName = "شادو (الظل)",
            rarity = FruitRarity.MYTHICAL,
            fruitType = FruitType.NATURAL,
            beliPrice = 2_900_000L,
            robuxPrice = 2425,
            physicalValue = 6_000_000L,
            permValue = 130_000_000L,
            demand = 6,
            trend = FruitTrend.STABLE,
            notes = "ضرر مرتفع مع سرقة الحياة ومقياس Umbra"
        ),
        BloxFruit(
            id = "gravity",
            name = "Gravity",
            arabicName = "غرافيتي (الجاذبية)",
            rarity = FruitRarity.MYTHICAL,
            fruitType = FruitType.NATURAL,
            beliPrice = 2_500_000L,
            robuxPrice = 2300,
            physicalValue = 2_500_000L,
            permValue = 100_000_000L,
            demand = 2,
            trend = FruitTrend.DROPPING,
            notes = "أقل فاكهة ميثيكال طلباً، تُستخدم كإضافة لفرق الـ 40%"
        ),

        // === LEGENDARY FRUITS ===
        BloxFruit(
            id = "portal",
            name = "Portal",
            arabicName = "بورتال (البوابة)",
            rarity = FruitRarity.LEGENDARY,
            fruitType = FruitType.NATURAL,
            beliPrice = 1_900_000L,
            robuxPrice = 2000,
            physicalValue = 11_000_000L,
            permValue = 135_000_000L,
            demand = 10,
            trend = FruitTrend.RISING,
            notes = "ملك التنقل اللحظي والـ Sword PvP، طلبها يفوق بعض الميثيكال!"
        ),
        BloxFruit(
            id = "buddha",
            name = "Buddha",
            arabicName = "بوذا",
            rarity = FruitRarity.LEGENDARY,
            fruitType = FruitType.BEAST,
            beliPrice = 1_200_000L,
            robuxPrice = 1650,
            physicalValue = 10_000_000L,
            permValue = 140_000_000L,
            demand = 10,
            trend = FruitTrend.HYPED,
            awakening = true,
            notes = "أفضل فاكهة لرفع المستوى (Grinding) والريدز ووحوش البحر بدون منازع"
        ),
        BloxFruit(
            id = "rumble",
            name = "Rumble",
            arabicName = "رامبل (البرق)",
            rarity = FruitRarity.LEGENDARY,
            fruitType = FruitType.ELEMENTAL,
            beliPrice = 2_100_000L,
            robuxPrice = 2100,
            physicalValue = 6_500_000L,
            permValue = 125_000_000L,
            demand = 9,
            trend = FruitTrend.RISING,
            awakening = true,
            notes = "صعقات قوية وتيليبورت متعدد، شائعة جداً بين محترفي السيوف"
        ),
        BloxFruit(
            id = "blizzard",
            name = "Blizzard",
            arabicName = "بليزارد (العاصفة الثلجية)",
            rarity = FruitRarity.LEGENDARY,
            fruitType = FruitType.ELEMENTAL,
            beliPrice = 2_400_000L,
            robuxPrice = 2250,
            physicalValue = 5_000_000L,
            permValue = 115_000_000L,
            demand = 7,
            trend = FruitTrend.STABLE,
            notes = "عاصفة دائرية ذات ضرر هائل ممتاز للأحداث البحرية"
        ),
        BloxFruit(
            id = "sound",
            name = "Sound",
            arabicName = "ساوند (الصوت)",
            rarity = FruitRarity.LEGENDARY,
            fruitType = FruitType.NATURAL,
            beliPrice = 1_700_000L,
            robuxPrice = 1900,
            physicalValue = 4_000_000L,
            permValue = 110_000_000L,
            demand = 6,
            trend = FruitTrend.STABLE,
            notes = "بوفات للسرعة والدرع وهجمات صوتية واسعة النطاق"
        ),
        BloxFruit(
            id = "phoenix",
            name = "Phoenix",
            arabicName = "فينيكس (العنقاء)",
            rarity = FruitRarity.LEGENDARY,
            fruitType = FruitType.BEAST,
            beliPrice = 1_800_000L,
            robuxPrice = 1800,
            physicalValue = 3_500_000L,
            permValue = 100_000_000L,
            demand = 5,
            trend = FruitTrend.STABLE,
            awakening = true,
            notes = "علاج ذاتي وجماعي وطيران سريع بعد الإيقاظ"
        ),
        BloxFruit(
            id = "love",
            name = "Love",
            arabicName = "لوف (الحب)",
            rarity = FruitRarity.LEGENDARY,
            fruitType = FruitType.NATURAL,
            beliPrice = 1_300_000L,
            robuxPrice = 1700,
            physicalValue = 1_800_000L,
            permValue = 85_000_000L,
            demand = 4,
            trend = FruitTrend.STABLE,
            notes = "استدعاء الفلامينغو للطيران ومساعد القتال"
        ),
        BloxFruit(
            id = "spider",
            name = "Spider",
            arabicName = "سبايدر (العنكبوت)",
            rarity = FruitRarity.LEGENDARY,
            fruitType = FruitType.NATURAL,
            beliPrice = 1_500_000L,
            robuxPrice = 1800,
            physicalValue = 1_500_000L,
            permValue = 80_000_000L,
            demand = 3,
            trend = FruitTrend.STABLE,
            awakening = true,
            notes = "خيوط حادة وإمكانية إيقاظ V2 لزيادة المدى والضرر"
        ),
        BloxFruit(
            id = "pain",
            name = "Pain",
            arabicName = "بين (الألم)",
            rarity = FruitRarity.LEGENDARY,
            fruitType = FruitType.NATURAL,
            beliPrice = 2_300_000L,
            robuxPrice = 2200,
            physicalValue = 2_000_000L,
            permValue = 90_000_000L,
            demand = 2,
            trend = FruitTrend.DROPPING,
            notes = "صعوبة التصويب تجعل طلبها ضعيفاً في السوق"
        ),
        BloxFruit(
            id = "quake",
            name = "Quake",
            arabicName = "كويك (الزلزال)",
            rarity = FruitRarity.LEGENDARY,
            fruitType = FruitType.NATURAL,
            beliPrice = 1_000_000L,
            robuxPrice = 1500,
            physicalValue = 1_200_000L,
            permValue = 70_000_000L,
            demand = 3,
            trend = FruitTrend.STABLE,
            awakening = true,
            notes = "تسونامي وموجات زلزالية تدمر كل شيء حولك"
        ),

        // === RARE FRUITS ===
        BloxFruit(
            id = "magma",
            name = "Magma",
            arabicName = "ماغما (الحمم)",
            rarity = FruitRarity.RARE,
            fruitType = FruitType.ELEMENTAL,
            beliPrice = 850_000L,
            robuxPrice = 1300,
            physicalValue = 1_500_000L,
            permValue = 65_000_000L,
            demand = 8,
            trend = FruitTrend.RISING,
            awakening = true,
            notes = "أعلى ضرر (Highest DPS) ضد وحوش البحر والأعداء الكبار بعد الإيقاظ"
        ),
        BloxFruit(
            id = "light",
            name = "Light",
            arabicName = "لايت (الضوء)",
            rarity = FruitRarity.RARE,
            fruitType = FruitType.ELEMENTAL,
            beliPrice = 650_000L,
            robuxPrice = 1100,
            physicalValue = 1_000_000L,
            permValue = 55_000_000L,
            demand = 7,
            trend = FruitTrend.STABLE,
            awakening = true,
            notes = "أسرع طيران في البحر الأول مع سيف ضوئي مجاني ممتاز للمبتدئين"
        ),
        BloxFruit(
            id = "ghost",
            name = "Ghost",
            arabicName = "غوست (الشبح)",
            rarity = FruitRarity.RARE,
            fruitType = FruitType.NATURAL,
            beliPrice = 940_000L,
            robuxPrice = 1275,
            physicalValue = 800_000L,
            permValue = 50_000_000L,
            demand = 4,
            trend = FruitTrend.STABLE,
            notes = "قدرة العودة للحياة بعد الموت بفرصة ثانية"
        ),
        BloxFruit(
            id = "rubber",
            name = "Rubber",
            arabicName = "مطاط (لوفي)",
            rarity = FruitRarity.RARE,
            fruitType = FruitType.NATURAL,
            beliPrice = 750_000L,
            robuxPrice = 1200,
            physicalValue = 700_000L,
            permValue = 45_000_000L,
            demand = 3,
            trend = FruitTrend.STABLE,
            notes = "مناعة تامة ضد هجمات الرامبل/البرق وسلاح الرصاص"
        ),
        BloxFruit(
            id = "barrier",
            name = "Barrier",
            arabicName = "الحاجز",
            rarity = FruitRarity.RARE,
            fruitType = FruitType.NATURAL,
            beliPrice = 800_000L,
            robuxPrice = 1250,
            physicalValue = 500_000L,
            permValue = 40_000_000L,
            demand = 2,
            trend = FruitTrend.STABLE,
            notes = "بناء جدران وسلالم دفاعية لحجب الهجمات"
        ),
        BloxFruit(
            id = "diamond",
            name = "Diamond",
            arabicName = "ألماس",
            rarity = FruitRarity.RARE,
            fruitType = FruitType.NATURAL,
            beliPrice = 600_000L,
            robuxPrice = 1000,
            physicalValue = 300_000L,
            permValue = 35_000_000L,
            demand = 2,
            trend = FruitTrend.STABLE,
            notes = "زيادة دفاعية كبيرة +25% ومظهر متبلور"
        ),

        // === UNCOMMON FRUITS ===
        BloxFruit(
            id = "ice",
            name = "Ice",
            arabicName = "آيس (الجليد)",
            rarity = FruitRarity.UNCOMMON,
            fruitType = FruitType.ELEMENTAL,
            beliPrice = 350_000L,
            robuxPrice = 750,
            physicalValue = 600_000L,
            permValue = 40_000_000L,
            demand = 6,
            trend = FruitTrend.RISING,
            awakening = true,
            notes = "تجميد وسطح جليدي للمشي على الماء، كومبو الموت مع السيوف"
        ),
        BloxFruit(
            id = "dark",
            name = "Dark",
            arabicName = "دارك (الظلام)",
            rarity = FruitRarity.UNCOMMON,
            fruitType = FruitType.ELEMENTAL,
            beliPrice = 500_000L,
            robuxPrice = 950,
            physicalValue = 500_000L,
            permValue = 35_000_000L,
            demand = 4,
            trend = FruitTrend.STABLE,
            awakening = true,
            notes = "سحب وثقب أسود يصدم الخصوم لفترة طويلة"
        ),
        BloxFruit(
            id = "sand",
            name = "Sand",
            arabicName = "رمل",
            rarity = FruitRarity.UNCOMMON,
            fruitType = FruitType.ELEMENTAL,
            beliPrice = 420_000L,
            robuxPrice = 850,
            physicalValue = 400_000L,
            permValue = 30_000_000L,
            demand = 3,
            trend = FruitTrend.STABLE,
            awakening = true,
            notes = "عواصف رملية واحتجاز، تفقد تأثير اللوجيا في الماء"
        ),
        BloxFruit(
            id = "flame",
            name = "Flame",
            arabicName = "فليم (اللهب)",
            rarity = FruitRarity.UNCOMMON,
            fruitType = FruitType.ELEMENTAL,
            beliPrice = 250_000L,
            robuxPrice = 550,
            physicalValue = 350_000L,
            permValue = 25_000_000L,
            demand = 4,
            trend = FruitTrend.STABLE,
            awakening = true,
            notes = "طيران ناري وهجمات حارقة سريعة"
        ),
        BloxFruit(
            id = "falcon",
            name = "Falcon",
            arabicName = "صقر",
            rarity = FruitRarity.UNCOMMON,
            fruitType = FruitType.BEAST,
            beliPrice = 300_000L,
            robuxPrice = 650,
            physicalValue = 200_000L,
            permValue = 20_000_000L,
            demand = 1,
            trend = FruitTrend.STABLE,
            notes = "أجنحة صقر وضربات ريش سريعة"
        ),
        BloxFruit(
            id = "spike",
            name = "Spike",
            arabicName = "أشواك",
            rarity = FruitRarity.UNCOMMON,
            fruitType = FruitType.NATURAL,
            beliPrice = 180_000L,
            robuxPrice = 380,
            physicalValue = 150_000L,
            permValue = 15_000_000L,
            demand = 1,
            trend = FruitTrend.STABLE,
            notes = "كرات وقذائف شوكية"
        ),

        // === COMMON FRUITS ===
        BloxFruit(
            id = "smoke",
            name = "Smoke",
            arabicName = "دخان",
            rarity = FruitRarity.COMMON,
            fruitType = FruitType.ELEMENTAL,
            beliPrice = 100_000L,
            robuxPrice = 250,
            physicalValue = 100_000L,
            permValue = 12_000_000L,
            demand = 2,
            trend = FruitTrend.STABLE,
            notes = "أرخص فاكهة عنصرية (Logia) مع طيران مستمر"
        ),
        BloxFruit(
            id = "chop",
            name = "Chop",
            arabicName = "تشوب (التقطيع)",
            rarity = FruitRarity.COMMON,
            fruitType = FruitType.NATURAL,
            beliPrice = 30_000L,
            robuxPrice = 100,
            physicalValue = 100_000L,
            permValue = 10_000_000L,
            demand = 3,
            trend = FruitTrend.STABLE,
            notes = "مناعة كاملة ضد أي ضرر سيوف في اللعبة!"
        ),
        BloxFruit(
            id = "bomb",
            name = "Bomb",
            arabicName = "قنبلة",
            rarity = FruitRarity.COMMON,
            fruitType = FruitType.NATURAL,
            beliPrice = 80_000L,
            robuxPrice = 220,
            physicalValue = 80_000L,
            permValue = 10_000_000L,
            demand = 1,
            trend = FruitTrend.STABLE,
            notes = "انفجار ذاتي واسع النطاق"
        ),
        BloxFruit(
            id = "spring",
            name = "Spring",
            arabicName = "زنبرك",
            rarity = FruitRarity.COMMON,
            fruitType = FruitType.NATURAL,
            beliPrice = 60_000L,
            robuxPrice = 180,
            physicalValue = 60_000L,
            permValue = 8_000_000L,
            demand = 1,
            trend = FruitTrend.STABLE,
            notes = "قفزات ارتدادية وسرعة اندفاع"
        ),
        BloxFruit(
            id = "spin",
            name = "Spin",
            arabicName = "دوران",
            rarity = FruitRarity.COMMON,
            fruitType = FruitType.NATURAL,
            beliPrice = 7_500L,
            robuxPrice = 75,
            physicalValue = 40_000L,
            permValue = 5_000_000L,
            demand = 1,
            trend = FruitTrend.STABLE,
            notes = "دوران كالمروحية وطيران منخفض"
        ),
        BloxFruit(
            id = "rocket",
            name = "Rocket",
            arabicName = "صاروخ",
            rarity = FruitRarity.COMMON,
            fruitType = FruitType.NATURAL,
            beliPrice = 5_000L,
            robuxPrice = 50,
            physicalValue = 20_000L,
            permValue = 3_000_000L,
            demand = 1,
            trend = FruitTrend.STABLE,
            notes = "أرخص فاكهة في اللعبة (بديلة الكيلو السابقة)"
        ),

        // === GAMEPASSES & SPECIALS ===
        BloxFruit(
            id = "gp_fruit_notifier",
            name = "Fruit Notifier",
            arabicName = "منبه الفواكه (Fruit Notifier)",
            rarity = FruitRarity.GAMEPASS,
            fruitType = FruitType.GAMEPASS,
            beliPrice = 0L,
            robuxPrice = 2700,
            physicalValue = 230_000_000L,
            permValue = 230_000_000L,
            demand = 9,
            trend = FruitTrend.STABLE,
            isGamepass = true,
            notes = "ينبهك عند نزول أي فاكهة في السيرفر مع المسافة بالمتر"
        ),
        BloxFruit(
            id = "gp_dark_blade",
            name = "Dark Blade (Yoru)",
            arabicName = "دارك بليد (سيف يورو)",
            rarity = FruitRarity.GAMEPASS,
            fruitType = FruitType.GAMEPASS,
            beliPrice = 0L,
            robuxPrice = 1200,
            physicalValue = 85_000_000L,
            permValue = 85_000_000L,
            demand = 8,
            trend = FruitTrend.STABLE,
            isGamepass = true,
            notes = "سيف أسطوري كلاسيكي قابل للتطوير إلى V2 و V3"
        ),
        BloxFruit(
            id = "gp_fruit_storage",
            name = "+1 Fruit Storage",
            arabicName = "توسيع مساحة الحقيبة (+1 Storage)",
            rarity = FruitRarity.GAMEPASS,
            fruitType = FruitType.GAMEPASS,
            beliPrice = 0L,
            robuxPrice = 400,
            physicalValue = 45_000_000L,
            permValue = 45_000_000L,
            demand = 10,
            trend = FruitTrend.HYPED,
            isGamepass = true,
            notes = "الجيم باس الأكثر طلباً وتداولاً في مجتمع بلوكس فروت بأكمله!"
        ),
        BloxFruit(
            id = "gp_2x_mastery",
            name = "2x Mastery",
            arabicName = "مضاعفة نقاط الماستري (2x Mastery)",
            rarity = FruitRarity.GAMEPASS,
            fruitType = FruitType.GAMEPASS,
            beliPrice = 0L,
            robuxPrice = 450,
            physicalValue = 40_000_000L,
            permValue = 40_000_000L,
            demand = 9,
            trend = FruitTrend.STABLE,
            isGamepass = true,
            notes = "مضاعفة سرعة فتح مهارات الفواكه والأساليب والسيوف"
        ),
        BloxFruit(
            id = "gp_2x_money",
            name = "2x Money",
            arabicName = "مضاعفة البيلي (2x Money)",
            rarity = FruitRarity.GAMEPASS,
            fruitType = FruitType.GAMEPASS,
            beliPrice = 0L,
            robuxPrice = 450,
            physicalValue = 40_000_000L,
            permValue = 40_000_000L,
            demand = 9,
            trend = FruitTrend.STABLE,
            isGamepass = true,
            notes = "مضاعفة مكافآت البيلي من المهمات والزعماء"
        ),
        BloxFruit(
            id = "gp_fast_boats",
            name = "Fast Boats",
            arabicName = "القوارب السريعة (Fast Boats)",
            rarity = FruitRarity.GAMEPASS,
            fruitType = FruitType.GAMEPASS,
            beliPrice = 0L,
            robuxPrice = 350,
            physicalValue = 15_000_000L,
            permValue = 15_000_000L,
            demand = 6,
            trend = FruitTrend.STABLE,
            isGamepass = true,
            notes = "استدعاء السفن السريعة المنيعة كالسفينة المشتعلة والإنفورنو"
        ),
        BloxFruit(
            id = "gp_2x_boss_drops",
            name = "2x Boss Drops",
            arabicName = "مضاعفة غنائم الزعماء (2x Boss Drops)",
            rarity = FruitRarity.GAMEPASS,
            fruitType = FruitType.GAMEPASS,
            beliPrice = 0L,
            robuxPrice = 350,
            physicalValue = 15_000_000L,
            permValue = 15_000_000L,
            demand = 5,
            trend = FruitTrend.STABLE,
            isGamepass = true,
            notes = "مضاعفة فرصة الحصول على السيوف والإكسسوارات النادرة من الزعماء"
        )
    )

    fun findById(id: String): BloxFruit? = allItems.find { it.id == id }

    fun filterItems(
        query: String = "",
        rarity: FruitRarity? = null,
        type: FruitType? = null,
        onlyAwakened: Boolean = false,
        sortBy: String = "value_desc"
    ): List<BloxFruit> {
        val filtered = allItems.filter { item ->
            val matchesQuery = query.isBlank() ||
                item.name.contains(query, ignoreCase = true) ||
                item.arabicName.contains(query, ignoreCase = true)

            val matchesRarity = rarity == null || item.rarity == rarity
            val matchesType = type == null || item.fruitType == type
            val matchesAwakened = !onlyAwakened || item.awakening

            matchesQuery && matchesRarity && matchesType && matchesAwakened
        }

        return when (sortBy) {
            "value_desc" -> filtered.sortedByDescending { it.physicalValue }
            "value_asc" -> filtered.sortedBy { it.physicalValue }
            "demand_desc" -> filtered.sortedByDescending { it.demand }
            "beli_desc" -> filtered.sortedByDescending { it.beliPrice }
            "perm_desc" -> filtered.sortedByDescending { it.permValue }
            "name" -> filtered.sortedBy { it.name }
            else -> filtered
        }
    }
}
