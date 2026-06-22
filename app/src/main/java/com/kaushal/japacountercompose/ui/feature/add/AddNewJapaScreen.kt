package com.kaushal.japacountercompose.ui.feature.add

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.domain.Outcome
import com.kaushal.japacountercompose.ui.CustomLargeButton
import com.kaushal.japacountercompose.ui.LoadingOverlay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun AddNewJapaScreen(
    navController: NavController,
    newJapaViewModel: AddNewJapaViewModel = hiltViewModel()
) {
    val isLoading by newJapaViewModel.isLoading.collectAsState()

    AddNewJapaScreenContent(
        onBackClick = { navController.popBackStack() },
        onSaveClick = { name, target -> newJapaViewModel.addNewJapa(name, target) },
        isLoading = isLoading,
        addNewJapaOutcome = newJapaViewModel.addNewJapaOutcome,
        onSaveSuccess = { navController.popBackStack() }
    )
}

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
                    Toast.makeText(context, context.getString(R.string.japa_added_successfully), Toast.LENGTH_SHORT).show()
                    onSaveSuccess()
                }
                is Outcome.Failure -> {
                    Toast.makeText(context, context.getString(R.string.error_adding_japa), Toast.LENGTH_SHORT).show()
                }
                is Outcome.Loading -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { AddNewJapaTopBar(onBackClick = onBackClick) },
            bottomBar = {
                CustomLargeButton(
                    onClick = {
                        if (nameText.isBlank()) {
                            Toast.makeText(context, context.getString(R.string.japa_name_empty_error), Toast.LENGTH_SHORT).show()
                            return@CustomLargeButton
                        }
                        onSaveClick(nameText.trim(), goalText.toIntOrNull())
                    },
                    label = stringResource(id = R.string.add),
                    enabled = !isLoading
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                AddNewJapaForm(
                    nameText = nameText,
                    onNameChange = { nameText = it },
                    goalText = goalText,
                    onGoalChange = { goalText = it }
                )
            }
        }

        if (isLoading) {
            LoadingOverlay()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddNewJapaScreenPreview() {
    AddNewJapaScreenContent(
        onBackClick = {},
        onSaveClick = { _, _ -> },
        isLoading = false,
        addNewJapaOutcome = MutableSharedFlow(),
        onSaveSuccess = {}
    )
}
