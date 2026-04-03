package com.example.rebuska.ui.theme

import android.app.Activity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

// ── Color scheme ──────────────────────────────────────
private val RebuskaColorScheme = lightColorScheme(

    // Primarios
    primary          = Blue800,
    onPrimary        = White,
    primaryContainer = BlueLight,
    onPrimaryContainer = Blue900,

    // Terciarios (dorado → rating / advertencias)
    tertiary          = Gold,
    onTertiary        = TextPrimary,
    tertiaryContainer = GoldLight,
    onTertiaryContainer = TextPrimary,

    // Error
    error          = Red800,
    onError        = White,
    errorContainer = RedLight,
    onErrorContainer = Red800,

    // Fondos
    background = Background,
    onBackground = TextPrimary,
    surface    = White,
    onSurface  = TextPrimary,
    surfaceVariant    = Surface,
    onSurfaceVariant  = TextMuted,

    // Bordes y outlines
    outline      = BlueBorder,
    outlineVariant = DividerColor,

    // Inversión (snackbars, tooltips)
    inverseSurface   = TextPrimary,
    inverseOnSurface = White,
    inversePrimary   = Blue400,
)

// ── Formas ────────────────────────────────────────────
val RebuskaShapes = Shapes(
    // Chips, badges, inputs pequeños
    extraSmall = RoundedCornerShape(8.dp),
    // Cards pequeñas, items de lista
    small      = RoundedCornerShape(12.dp),
    // Cards estándar, section cards
    medium     = RoundedCornerShape(18.dp),
    // Cards grandes, modales, bottom sheets
    large      = RoundedCornerShape(24.dp),
    // Botones pill, avatares, FABs
    extraLarge = RoundedCornerShape(50.dp),
)

// ── Theme principal ───────────────────────────────────
@Composable
fun RebuskaTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = RebuskaColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Status bar con el azul primario
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = RebuskaTypography,
        shapes      = RebuskaShapes,
        content     = content
    )
}
