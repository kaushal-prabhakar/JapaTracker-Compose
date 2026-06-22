package com.kaushal.japacountercompose.ui.feature.add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.ui.theme.BrandColor

private const val JAPA_NAME_MAX_LENGTH = 30L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewJapaTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.add_new_japa),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BrandColor,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        }
    )
}

@Composable
fun AddNewJapaForm(
    nameText: String,
    onNameChange: (String) -> Unit,
    goalText: String,
    onGoalChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = nameText,
            onValueChange = { if (it.length <= JAPA_NAME_MAX_LENGTH) onNameChange(it) },
            supportingText = {
                Text(
                    text = "${nameText.length}/${JAPA_NAME_MAX_LENGTH}",
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            label = {
                Text(
                    stringResource(id = R.string.enter_japa_name),
                    fontFamily = FontFamily.Monospace
                )
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = goalText,
            onValueChange = { onGoalChange(it.filter { char -> char.isDigit() }) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                Text(
                    stringResource(id = R.string.set_goal),
                    fontFamily = FontFamily.Monospace
                )
            },
            singleLine = true
        )
    }
}
