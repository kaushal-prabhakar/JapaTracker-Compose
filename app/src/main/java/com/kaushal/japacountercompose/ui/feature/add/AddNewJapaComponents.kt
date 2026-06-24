package com.kaushal.japacountercompose.ui.feature.add

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.ui.CustomLargeButton
import com.kaushal.japacountercompose.ui.theme.AlphaBrandColor
import com.kaushal.japacountercompose.ui.theme.BrandColor
import com.kaushal.japacountercompose.ui.theme.JapaCardColor
import com.kaushal.japacountercompose.ui.toTitleCase

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
    isLoading: Boolean,
    nameText: String,
    onNameChange: (String) -> Unit,
    goalText: String,
    onGoalChange: (String) -> Unit,
    onAddClick: () -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 80.dp),
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp),
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = JapaCardColor
            ),
            border = BorderStroke(0.3.dp, AlphaBrandColor),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                         focusedBorderColor = AlphaBrandColor,
                         focusedSupportingTextColor = AlphaBrandColor
                     ),
                    value = nameText,
                    onValueChange = { if (it.length <= JAPA_NAME_MAX_LENGTH) onNameChange(it) },
                    supportingText = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "${nameText.length}/${JAPA_NAME_MAX_LENGTH}",
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.End
                        )
                    },
                    label = {
                        Text(
                            stringResource(id = R.string.enter_japa_name),
                            fontFamily = FontFamily.Monospace,
                            color = AlphaBrandColor
                        )
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AlphaBrandColor,
                        focusedSupportingTextColor = AlphaBrandColor
                    ),
                    value = goalText,
                    onValueChange = { onGoalChange(it.filter { char -> char.isDigit() }) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text(
                            stringResource(id = R.string.set_goal),
                            fontFamily = FontFamily.Monospace,
                            color = AlphaBrandColor
                        )
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                CustomLargeButton(
                    onClick = { onAddClick.invoke() },
                    label = stringResource(id = R.string.add),
                    enabled = !isLoading
                )
            }
        }
    }
}
