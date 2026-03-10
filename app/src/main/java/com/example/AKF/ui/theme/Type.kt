package com.example.project_election.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.project_election.R

// 1. ประกาศตัวแปรฟอนต์ Kanit
val KanitFontFamily = FontFamily(
    Font(R.font.kanit_regular, FontWeight.Normal),
    Font(R.font.kanit_medium, FontWeight.Medium),
    Font(R.font.kanit_bold, FontWeight.Bold)
)


val defaultTypography = Typography()


val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = KanitFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = KanitFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = KanitFontFamily),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = KanitFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = KanitFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = KanitFontFamily),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = KanitFontFamily, fontWeight = FontWeight.Bold),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = KanitFontFamily, fontWeight = FontWeight.Bold),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = KanitFontFamily, fontWeight = FontWeight.Medium),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = KanitFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = KanitFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = KanitFontFamily),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = KanitFontFamily, fontWeight = FontWeight.Medium),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = KanitFontFamily, fontWeight = FontWeight.Medium),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = KanitFontFamily, fontWeight = FontWeight.Medium)
)