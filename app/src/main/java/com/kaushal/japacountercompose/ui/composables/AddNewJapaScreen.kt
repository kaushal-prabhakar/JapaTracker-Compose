package com.kaushal.japacountercompose.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.ui.theme.BrandColor

@Composable
fun AddNewJapaScreen(navController: NavController) {
    AddNewJapaScreenContent(
        onBackClick = {
            navController.popBackStack()
        },
        onSaveClick = {

        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewJapaScreenContent(onBackClick: () -> Unit, onSaveClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = stringResource(id = R.string.add_new_japa),
                    fontFamily = FontFamily.Monospace
                )},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrandColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { onBackClick.invoke() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },

        bottomBar = {
            CustomLargeButton(
                onClick = { /*TODO*/ },
                label = "Add")
        },

        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var nameText by remember {
                    mutableStateOf("")
                }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 2.dp),
                    value = nameText,
                    onValueChange = { nameText = it },
                    label = {
                        Text("Enter Japa Name", fontFamily = FontFamily.Monospace)
                    })

                var goalText by remember {
                    mutableStateOf("")
                }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 2.dp),
                    value = goalText,
                    onValueChange = { goalText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text("Set Goal", fontFamily = FontFamily.Monospace)

                    })
            }
        }
    )
}

@Preview
@Composable
fun AddNewJapaScreenPreview() {
    AddNewJapaScreenContent(onBackClick = {}, onSaveClick = {})

}