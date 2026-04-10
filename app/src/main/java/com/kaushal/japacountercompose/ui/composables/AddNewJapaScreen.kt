package com.kaushal.japacountercompose.ui.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.data.Outcome
import com.kaushal.japacountercompose.ui.theme.BrandColor
import com.kaushal.japacountercompose.ui.viewmodels.AddNewJapaViewModel
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun AddNewJapaScreen(
    navController: NavController,
    newJapaViewModel: AddNewJapaViewModel = hiltViewModel()
) {
    val isLoading by newJapaViewModel.isLoading.collectAsState()

    AddNewJapaScreenContent(
        onBackClick = {
            navController.popBackStack()
        },
        onSaveClick = { name, target ->
            newJapaViewModel.addNewJapa(name, target)
        },
        isLoading = isLoading,
        addNewJapaOutcome = newJapaViewModel.addNewJapaOutcome,
        onSaveSuccess = {
            navController.popBackStack()
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewJapaScreenContent(
    onBackClick: () -> Unit,
    onSaveClick: (String, Int?) -> Unit,
    isLoading: Boolean,
    addNewJapaOutcome: SharedFlow<Outcome<Unit>>,
    onSaveSuccess: () -> Unit
) {

    var nameText by remember { mutableStateOf("") }
    var goalText by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        addNewJapaOutcome.collect { result ->
            when (result) {
                is Outcome.Success -> {
                    Toast.makeText(context, "Japa Added Successfully", Toast.LENGTH_SHORT).show()
                    onSaveSuccess()
                }

                is Outcome.Failure -> {
                    Toast.makeText(
                        context,
                        "Error adding Japa.. please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Outcome.Loading -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.add_new_japa),
                            fontFamily = FontFamily.Monospace
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BrandColor,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = { onBackClick() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            },

            bottomBar = {
                CustomLargeButton(
                    onClick = {
                        if (nameText.isBlank()) {
                            Toast.makeText(context, "Japa Name cannot be empty", Toast.LENGTH_SHORT)
                                .show()
                            return@CustomLargeButton
                        }
                        onSaveClick(nameText.trim(), goalText.toIntOrNull())
                    },
                    label = "Add",
                )
            },

            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 2.dp),
                        value = nameText,
                        onValueChange = { if (it.length <= 30) nameText = it },
                        supportingText = {
                            Text("${nameText.length}/30", fontFamily = FontFamily.Monospace)
                        },
                        label = {
                            Text("Enter Japa Name", fontFamily = FontFamily.Monospace)
                        })

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

        if (isLoading) {
            ShowLoader()
        }
    }
}

@Composable
fun ShowLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun AddNewJapaScreenPreview() {
    AddNewJapaScreenContent(
        onBackClick = {},
        onSaveClick = { _, _ -> },
        isLoading = false,
        addNewJapaOutcome = kotlinx.coroutines.flow.MutableSharedFlow(),
        onSaveSuccess = {}
    )
}
