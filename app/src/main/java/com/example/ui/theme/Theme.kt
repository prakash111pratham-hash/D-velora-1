package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CoffeeGoldPrimaryDark,
    onPrimary = OnCoffeeGoldPrimaryDark,
    primaryContainer = CoffeeBrownPrimary,
    onPrimaryContainer = CoffeeGoldPrimaryDark,
    secondary = HarvestGoldSecondaryDark,
    onSecondary = OnHarvestGoldSecondary,
    secondaryContainer = DarkRoastSurfaceVariant,
    onSecondaryContainer = HarvestGoldSecondaryDark,
    tertiary = AmberTertiaryDark,
    background = DarkRoastBackground,
    onBackground = TextLightCream,
    surface = DarkRoastSurface,
    onSurface = TextLightCream,
    surfaceVariant = DarkRoastSurfaceVariant,
    onSurfaceVariant = TextMutedFawn,
    outline = OutlineDarkCocoa
)

private val LightColorScheme = lightColorScheme(
    primary = CoffeeBrownPrimary,
    onPrimary = OnCoffeeBrownPrimary,
    primaryContainer = WarmFrothSurfaceVariant,
    onPrimaryContainer = CoffeeBrownPrimary,
    secondary = HarvestGoldSecondary,
    onSecondary = OnHarvestGoldSecondary,
    secondaryContainer = WarmFrothSurfaceVariant,
    onSecondaryContainer = CoffeeBrownPrimary,
    tertiary = AmberTertiary,
    background = WarmCreamBackground,
    onBackground = TextDarkEspresso,
    surface = WarmCreamSurface,
    onSurface = TextDarkEspresso,
    surfaceVariant = WarmFrothSurfaceVariant,
    onSurfaceVariant = TextMutedCocoa,
    outline = OutlineLatte
)

@Composable
fun DVeloraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
