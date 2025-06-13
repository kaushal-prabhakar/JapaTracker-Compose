package com.kaushal.japacountercompose.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleObserver
import com.kaushal.japacountercompose.data.JapaStatus
import com.kaushal.japacountercompose.ui.theme.Active
import com.kaushal.japacountercompose.ui.theme.BrandColor
import com.kaushal.japacountercompose.ui.theme.Completed

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

@Composable
fun StatusBadge(status: JapaStatus) {
    val backgroundColor = when (status) {
        JapaStatus.ACTIVE -> Active
        JapaStatus.COMPLETED -> Completed
    }

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.toString().uppercase(),
            fontSize = 12.sp,
            color = Color.White,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
            )
        Text(text = value,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

