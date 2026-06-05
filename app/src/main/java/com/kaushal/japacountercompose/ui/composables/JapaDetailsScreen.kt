package com.kaushal.japacountercompose.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.data.JapaInfoEntities
import com.kaushal.japacountercompose.data.JapaStatus
import com.kaushal.japacountercompose.data.Outcome
import com.kaushal.japacountercompose.data.UpdateType
import com.kaushal.japacountercompose.ui.theme.AlphaBrandColor
import com.kaushal.japacountercompose.ui.theme.BrandColor
import com.kaushal.japacountercompose.ui.viewmodels.JapaDetailsAction
import com.kaushal.japacountercompose.ui.viewmodels.JapaDetailsViewModel
import java.time.LocalDateTime

@Composable
fun JapaDetailsScreen(
    navController: NavController,
    viewModel: JapaDetailsViewModel = hiltViewModel()
) {
    val outcome by viewModel.japaDetailOutcome.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showUpdateDialog by remember { mutableStateOf(false) }

    val operationFailedMessage = stringResource(id = R.string.operation_failed)

    LaunchedEffect(Unit) {
        viewModel.actionEvent.collect { event ->
            when (event) {
                is JapaDetailsAction.DeleteSuccess -> navController.popBackStack()
                is JapaDetailsAction.UpdateSuccess -> Unit
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
                onUpdateClick = { showUpdateDialog = true },
                onDeleteClick = { viewModel.deleteJapa() },
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JapaDetailsLoadingScreen(
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { JapaDetailsTopBar(onBackClick = onBackClick) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = BrandColor)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JapaDetailsErrorScreen(
    snackbarHostState: SnackbarHostState,
    message: String,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { JapaDetailsTopBar(onBackClick = onBackClick) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message.ifBlank { stringResource(id = R.string.error_loading_details) },
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JapaDetailsTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.japa_details),
                fontFamily = FontFamily.Monospace
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
                    contentDescription = stringResource(id = R.string.navigate_back)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JapaDetailsContent(
    japaInfo: JapaInfoEntities,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { JapaDetailsTopBar(onBackClick = onBackClick) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Column {
                CustomLargeButton(
                    onClick = onUpdateClick,
                    label = stringResource(id = R.string.update_count),
                    enabled = !isLoading
                )
                CustomLargeButton(
                    onClick = onDeleteClick,
                    label = stringResource(id = R.string.delete_japa),
                    enabled = !isLoading
                )
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeroHeaderSection(japaInfo = japaInfo)
            }
        }
    )
}

@Composable
private fun HeroHeaderSection(japaInfo: JapaInfoEntities) {
    val progress = japaInfo.target?.let {
        if (it > 0) japaInfo.currentCount.toFloat() / it.toFloat() else 0f
    } ?: 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AlphaBrandColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = BrandColor.copy(alpha = 0.2f),
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = BrandColor,
                    startAngle = -90f,
                    sweepAngle = 360 * progress.coerceIn(0f, 1f),
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = japaInfo.currentCount.toString(),
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BrandColor,
                        fontFamily = FontFamily.Monospace
                    )
                )
                if (japaInfo.target != null) {
                    Text(
                        text = stringResource(id = R.string.of_target, japaInfo.target),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = BrandColor.copy(alpha = 0.6f),
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = japaInfo.name,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = BrandColor
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        StatusBadge(status = japaInfo.status)
    }
}

@Composable
private fun UpdateCountDialog(
    currentCount: Int,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onAdd: (Int) -> Unit,
    onDeduct: (Int) -> Unit
) {
    var inputValue by remember { mutableStateOf("") }
    val parsedValue = inputValue.toIntOrNull() ?: 0

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.update_count),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 2.dp),
                    readOnly = true,
                    value = currentCount.toString(),
                    onValueChange = {},
                    label = {
                        Text(
                            text = stringResource(id = R.string.current_count, currentCount),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 2.dp),
                    value = inputValue,
                    onValueChange = { inputValue = it.filter { ch -> ch.isDigit() } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text(
                            text = stringResource(id = R.string.enter_count),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                )

                Text(
                    text = stringResource(id = R.string.new_count_value_description),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomSmallButton(
                        onClick = { if (parsedValue > 0) onAdd(parsedValue) },
                        label = stringResource(id = R.string.add),
                        enabled = parsedValue > 0 && !isLoading,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )

                    CustomSmallButton(
                        onClick = { if (parsedValue > 0) onDeduct(parsedValue) },
                        label = stringResource(id = R.string.deduct),
                        enabled = parsedValue > 0 && !isLoading,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                }
            }
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
    )
    JapaDetailsContent(
        japaInfo = mockJapa,
        isLoading = false,
        snackbarHostState = remember { SnackbarHostState() },
        onUpdateClick = {},
        onDeleteClick = {},
        onBackClick = {}
    )
}
