package com.kaushal.japacountercompose.ui.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleObserver
import com.kaushal.japacountercompose.ui.theme.BrandColor

class CommonUi {

    companion object {
        val commonLargeButtonModifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(60.dp)

    }

}

@Composable
fun CustomLargeButton(onClick: () -> Unit, label: String) {
    Button(
        onClick = { onClick.invoke() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrandColor)
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun CustomSmallButton(onClick: () -> Unit, label: String, modifier: Modifier = Modifier) {
    Button(
        onClick = { onClick.invoke() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = BrandColor)
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun RegularText(text: String, fontSize: TextUnit) {

    val localuser = compositionLocalOf {
        "kaushal"
    }

    val dark = isSystemInDarkTheme()

    CompositionLocalProvider(value = localuser provides "poornima") {
        
    }

 return Text(text = text, fontSize = fontSize, fontFamily = FontFamily.Monospace)
}

