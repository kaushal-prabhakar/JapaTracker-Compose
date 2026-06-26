package com.kaushal.japacountercompose.ui.feature.details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.domain.JapaInfoEntities
import com.kaushal.japacountercompose.domain.JapaStatus
import com.kaushal.japacountercompose.domain.UpdateType
import com.kaushal.japacountercompose.ui.CustomLargeButton
import com.kaushal.japacountercompose.ui.IconButton
import com.kaushal.japacountercompose.ui.StatusBadge
import com.kaushal.japacountercompose.ui.formatWithCommas
import com.kaushal.japacountercompose.ui.icons.ArrowDownIcon
import com.kaushal.japacountercompose.ui.icons.ArrowUpIcon
import com.kaushal.japacountercompose.ui.icons.ClockIcon
import com.kaushal.japacountercompose.ui.theme.AlphaBrandColor
import com.kaushal.japacountercompose.ui.theme.BrandColor
import com.kaushal.japacountercompose.ui.theme.Completed
import com.kaushal.japacountercompose.ui.theme.ErrorRed
import com.kaushal.japacountercompose.ui.theme.JapaCardColor
import com.kaushal.japacountercompose.ui.theme.SuccessGreen
import com.kaushal.japacountercompose.ui.toTitleCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JapaDetailsLoadingScreen(
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
fun JapaDetailsErrorScreen(
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
fun JapaDetailsTopBar(
    onBackClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
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
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        navigationIcon = {
            androidx.compose.material3.IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigate_back)
                )
            }
        },
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JapaDetailsContent(
    japaInfo: JapaInfoEntities,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    onComplete: () -> Unit,
    onResetClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            JapaDetailsTopBar(
                onBackClick = onBackClick,
                actions = {
                    if (japaInfo.currentCount > 0 && japaInfo.status != JapaStatus.COMPLETED) {
                        androidx.compose.material3.IconButton(onClick = onResetClick) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(id = R.string.reset_content_description)
                            )
                        }
                    }
                    androidx.compose.material3.IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.delete_content_description)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (japaInfo.status != JapaStatus.COMPLETED) {
                FloatingActionButton(
                    onClick = { onUpdateClick() },
                    containerColor = BrandColor,
                    contentColor = Color.White,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.update_content_description)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (japaInfo.status != JapaStatus.COMPLETED) {
                IconButton(
                    onClick = onComplete,
                    label = stringResource(id = R.string.mark_complete),
                    enabled = !isLoading,
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(id = R.string.complete_content_description),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeroHeaderSection(japaInfo = japaInfo)
                PreviousSessionDetails(japaInfo = japaInfo)
            }
        },
    )
}

@Composable
fun HeroHeaderSection(japaInfo: JapaInfoEntities) {
    val progress = japaInfo.target?.let {
        if (it > 0) japaInfo.currentCount.toFloat() / it.toFloat() else 0f
    } ?: 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000),
        label = "ProgressAnimation"
    )

    val hasTargetCount = japaInfo.target != null

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
                    style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = BrandColor,
                    startAngle = -90f,
                    sweepAngle = 360 * animatedProgress,
                    useCenter = false,
                    style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = japaInfo.currentCount.formatWithCommas(),
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BrandColor,
                        fontFamily = FontFamily.Monospace
                    )
                )
                if (hasTargetCount) {
                    Text(
                        text = stringResource(
                            id = R.string.of_target,
                            japaInfo.target.formatWithCommas()
                        ),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = BrandColor.copy(alpha = 0.6f),
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }
            }
        }

        if (hasTargetCount) {
            val remainingCount = japaInfo.target - japaInfo.currentCount
            Text(
                text = "${remainingCount.formatWithCommas()} remaining",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = BrandColor
                ),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = japaInfo.name.toTitleCase(),
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
fun PreviousSessionDetails(japaInfo: JapaInfoEntities) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = R.string.last_session_title),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = BrandColor
            ),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = BrandColor.copy(alpha = 0.05f)
            ),
            border = BorderStroke(1.dp, BrandColor.copy(alpha = 0.1f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ClockIcon,
                        contentDescription = null,
                        tint = BrandColor.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = japaInfo.lastUpdatedTime,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = FontFamily.Monospace,
                            color = Color.DarkGray
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val isIncrement = japaInfo.lastUpdatedType == UpdateType.INCREMENT
                    val statusColor = if (isIncrement) SuccessGreen else ErrorRed

                    Icon(
                        imageVector = if (isIncrement) ArrowUpIcon else ArrowDownIcon,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    val count = japaInfo.lastUpdatedValue.formatWithCommas()
                    val labelText = if (japaInfo.status == JapaStatus.NOT_STARTED) {
                        stringResource(id = R.string.added_label)
                    } else {
                        if (isIncrement) stringResource(
                            id = R.string.added_count_label,
                            count
                        )
                        else stringResource(
                            id = R.string.deducted_count_label,
                            count
                        )
                    }

                    Text(
                        text =
                            if (labelText.contains(count)) {
                                buildAnnotatedString {
                                    val start = labelText.indexOf(count)
                                    val end = start + count.length

                                    append(labelText)

                                    addStyle(
                                        style = SpanStyle(
                                            color = if (isIncrement) Completed else Color.Red,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        start = start,
                                        end = end
                                    )
                                }
                            } else {
                                buildAnnotatedString { append(labelText) }
                            },
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = FontFamily.Monospace,
                            color = Color.DarkGray
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCountBottomSheet(
    targetCount: Int,
    currentCount: Int,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (Int, Boolean) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        UpdateCountBottomSheetContent(
            targetCount,
            currentCount = currentCount,
            isLoading = isLoading,
            onSave = onSave
        )
    }
}

@Composable
fun UpdateCountBottomSheetContent(
    targetCount: Int,
    currentCount: Int,
    isLoading: Boolean,
    onSave: (Int, Boolean) -> Unit
) {
    var inputValue by remember { mutableStateOf("") }
    var isDeduct by remember { mutableStateOf(false) }
    val parsedValue = inputValue.toIntOrNull() ?: 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, end = 24.dp, bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.update_count),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = BrandColor
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(id = R.string.current_count),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = BrandColor
            ),
            modifier = Modifier.padding(bottom = 6.dp)
                .align(Alignment.Start)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            color = BrandColor.copy(alpha = 0.08f)
        ) {
            Text(
                text = buildAnnotatedString {
                    append(currentCount.formatWithCommas())
                    withStyle(
                        style = SpanStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = BrandColor.copy(alpha = 0.6f)
                        )
                    ) {
                        append(" / ${targetCount.formatWithCommas()}")
                    }
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputValue,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AlphaBrandColor,
                focusedSupportingTextColor = AlphaBrandColor
            ),
            onValueChange = { inputValue = it.filter { ch -> ch.isDigit() } },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            label = {
                Text(
                    text = stringResource(id = R.string.enter_count),
                    fontFamily = FontFamily.Monospace,
                    color = BrandColor
                )
            }
        )

        Text(
            text = stringResource(id = R.string.new_count_value_description),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            textAlign = TextAlign.Start,
            color = Color.Black,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isDeduct) stringResource(id = R.string.deduct) else stringResource(id = R.string.add_japa),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = BrandColor
                )
            )
            Switch(
                checked = isDeduct,
                onCheckedChange = { isDeduct = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ErrorRed,
                    checkedTrackColor = ErrorRed.copy(alpha = 0.3f),
                    uncheckedThumbColor = Completed,
                    uncheckedTrackColor = Completed.copy(alpha = 0.3f)
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        CustomLargeButton(
            onClick = { if (parsedValue > 0) onSave(parsedValue, isDeduct) },
            label = stringResource(id = R.string.update_count),
            enabled = parsedValue > 0 && !isLoading
        )
    }
}
