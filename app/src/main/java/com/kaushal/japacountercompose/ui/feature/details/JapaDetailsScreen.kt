package com.kaushal.japacountercompose.ui.feature.details

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.domain.JapaInfoEntities
import com.kaushal.japacountercompose.domain.JapaStatus
import com.kaushal.japacountercompose.domain.Outcome
import com.kaushal.japacountercompose.domain.UpdateType
import com.kaushal.japacountercompose.ui.AlertDialog
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val ANIM_CELEBRATION_DURATION = 3000L

@Composable
fun JapaDetailsScreen(
    navController: NavController,
    viewModel: JapaDetailsViewModel = hiltViewModel()
) {
    val outcome by viewModel.japaDetailOutcome.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showIncompleteWarningDialog by remember { mutableStateOf(false) }
    var showCelebration by remember { mutableStateOf(false) }

    val operationFailedMessage = stringResource(id = R.string.operation_failed)

    LaunchedEffect(Unit) {
        viewModel.actionEvent.collect { event ->
            when (event) {
                is JapaDetailsAction.DeleteSuccess -> navController.popBackStack()
                is JapaDetailsAction.UpdateSuccess -> Unit
                is JapaDetailsAction.CompletionSuccess -> {
                    showIncompleteWarningDialog = false
                    showCelebration = true
                    delay(ANIM_CELEBRATION_DURATION) // Show celebration for 3 seconds
                    showCelebration = false
                }
                is JapaDetailsAction.Failure -> {
                    snackbarHostState.showSnackbar(
                        event.message.ifBlank { operationFailedMessage }
                    )
                }
            }
        }
    }

    when (val state = outcome) {
        is Outcome.Loading -> {
            JapaDetailsLoadingScreen(
                snackbarHostState = snackbarHostState,
                onBackClick = { navController.popBackStack() }
            )
        }

        is Outcome.Failure -> {
            JapaDetailsErrorScreen(
                snackbarHostState = snackbarHostState,
                message = state.message,
                onBackClick = { navController.popBackStack() }
            )
        }

        is Outcome.Success -> {
            JapaDetailsContent(
                japaInfo = state.data,
                isLoading = isLoading,
                snackbarHostState = snackbarHostState,
                onComplete = {
                    if (state.data.target != null) {
                        if (state.data.currentCount < state.data.target) {
                            showIncompleteWarningDialog = true
                        } else {
                            showIncompleteWarningDialog = false
                            viewModel.markComplete()
                        }
                    } else {
                        showIncompleteWarningDialog = false
                        viewModel.markComplete()
                    }
                },
                onUpdateClick = { showUpdateDialog = true },
                onResetClick = { showResetDialog = true },
                onDeleteClick = { showDeleteDialog = true },
                onBackClick = { navController.popBackStack() }
            )

            if (showUpdateDialog) {
                UpdateCountDialog(
                    currentCount = state.data.currentCount,
                    isLoading = isLoading,
                    onDismiss = { showUpdateDialog = false },
                    onAdd = { value ->
                        viewModel.incrementCount(value)
                        showUpdateDialog = false
                    },
                    onDeduct = { value ->
                        viewModel.decrementCount(value)
                        showUpdateDialog = false
                    }
                )
            }

            if (showDeleteDialog) {
                AlertDialog(
                    stringResource(id = R.string.delete_japa_confirm_title),
                    stringResource(id = R.string.delete_japa_confirm_message),
                    { viewModel.deleteJapa() },
                    { showDeleteDialog = false },
                    { showDeleteDialog = false })
            }

            if (showResetDialog) {
                AlertDialog(
                    stringResource(id = R.string.reset_japa_confirm_title),
                    stringResource(id = R.string.reset_japa_confirm_message),
                    {
                        viewModel.resetCounter()
                        showResetDialog = false
                    },
                    { showResetDialog = false },
                    { showResetDialog = false }
                )
            }

            if (showIncompleteWarningDialog) {
                AlertDialog(
                    stringResource(id = R.string.mark_complete_confirm_title),
                    stringResource(id = R.string.mark_complete_confirm_message),
                    { viewModel.markComplete() },
                    { showIncompleteWarningDialog = false },
                    { showIncompleteWarningDialog = false }
                )
            }

            CelebrationOverlay(visible = showCelebration)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun JapaDetailsLoadingScreenPreview() {
    JapaDetailsLoadingScreen(
        snackbarHostState = remember { SnackbarHostState() },
        onBackClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun UpdateCountDialogPreview() {
    UpdateCountDialog(
        currentCount = 54,
        isLoading = false,
        onDismiss = {},
        onAdd = {},
        onDeduct = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun JapaDetailsScreenPreview() {
    val mockJapa = JapaInfoEntities(
        id = 1,
        name = "Maha Mantra",
        target = 108,
        status = JapaStatus.ACTIVE,
        currentCount = 54,
        lastUpdatedValue = 1,
        lastUpdatedType = UpdateType.INCREMENT,
        lastUpdatedTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
    )
    JapaDetailsContent(
        japaInfo = mockJapa,
        isLoading = false,
        snackbarHostState = remember { SnackbarHostState() },
        onComplete = {},
        onResetClick = {},
        onUpdateClick = {},
        onDeleteClick = {},
        onBackClick = {}
    )
}
