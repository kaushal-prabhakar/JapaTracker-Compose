package com.kaushal.japacountercompose.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.domain.JapaStatus
import com.kaushal.japacountercompose.ui.theme.Active
import com.kaushal.japacountercompose.ui.theme.BrandColor
import com.kaushal.japacountercompose.ui.theme.Completed
import com.kaushal.japacountercompose.ui.theme.NotStarted

@Composable
fun CustomLargeButton(onClick: () -> Unit, label: String, enabled: Boolean = true) {
    Button(
        onClick = { onClick.invoke() },
        enabled = enabled,
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
fun CustomSmallButton(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = { onClick.invoke() },
        enabled = enabled,
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
fun IconButton(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    imageVector: ImageVector,
    contentDescription: String
) {
    Button(
        onClick = { onClick.invoke() },
        enabled = enabled,
        modifier = modifier
            .height(50.dp)
            .background(Color.Transparent),
        colors = ButtonDefaults.buttonColors(containerColor = BrandColor)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(18.dp),
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = label,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    imageVector: ImageVector,
    contentDescription: String
) {
    OutlinedButton(
        onClick = { onClick.invoke() },
        enabled = enabled,
        modifier = modifier
            .height(50.dp),
        border = BorderStroke(1.5.dp, BrandColor),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandColor)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(18.dp),
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = label,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StatusBadge(status: JapaStatus) {
    val (backgroundColor, textRes) = when (status) {
        JapaStatus.NOT_STARTED -> NotStarted to R.string.status_not_started
        JapaStatus.ACTIVE -> Active to R.string.status_active
        JapaStatus.COMPLETED -> Completed to R.string.status_completed
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
            text = stringResource(id = textRes),
            fontSize = 12.sp,
            color = Color.White,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

@Composable
fun AlertDialog(
    title: String, description: String, onConfirm: () -> Unit, onDismiss: () -> Unit = {},
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest.invoke() },
        title = { Text(title) },
        text = {
            Text(description)
        },
        confirmButton = {
            TextButton(onClick = { onConfirm.invoke() }) { Text(stringResource(id = R.string.yes)) }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss.invoke() }) { Text(stringResource(id = R.string.cancel)) }
        }
    )
}

@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = BrandColor)
    }
}
