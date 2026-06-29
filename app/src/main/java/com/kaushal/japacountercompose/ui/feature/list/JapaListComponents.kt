package com.kaushal.japacountercompose.ui.feature.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.domain.JapaInfoEntities
import com.kaushal.japacountercompose.domain.JapaStatus
import com.kaushal.japacountercompose.domain.Outcome
import com.kaushal.japacountercompose.ui.formatWithCommas
import com.kaushal.japacountercompose.ui.getJapaStatusUiInfo
import com.kaushal.japacountercompose.ui.icons.DotCircle
import com.kaushal.japacountercompose.ui.icons.Infinite
import com.kaushal.japacountercompose.ui.theme.BrandColor
import com.kaushal.japacountercompose.ui.theme.JapaCardColor
import com.kaushal.japacountercompose.ui.theme.LightTextColor
import com.kaushal.japacountercompose.ui.theme.ProgressBarBgColor
import com.kaushal.japacountercompose.ui.toTitleCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JapaListScreenContent(
    onJapaClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    japaListOutcome: Outcome<List<JapaInfoEntities>>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.japa_list),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrandColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddClick() },
                containerColor = BrandColor,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_japa)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { padding ->
            when (japaListOutcome) {
                is Outcome.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BrandColor)
                    }
                }

                is Outcome.Failure -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.error_loading_japa_list,
                                japaListOutcome.message
                            ),
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }

                is Outcome.Success -> {
                    val list = japaListOutcome.data
                    if (list.isEmpty()) {
                        DisplayEmptyListMessage()
                    } else {
                        JapaList(padding, list, onJapaClick)
                    }
                }
            }
        }
    )
}

@Composable
fun JapaList(
    paddingValues: PaddingValues,
    japaList: List<JapaInfoEntities>,
    onJapaClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(japaList) { item ->
            JapaCard(japaInfoEntities = item, onClick = { onJapaClick(item.id) })
        }
    }
}

@Composable
fun JapaCard(japaInfoEntities: JapaInfoEntities, onClick: () -> Unit) {
    val progress = japaInfoEntities.target?.let {
        if (it > 0) japaInfoEntities.currentCount.toFloat() / it.toFloat() else null
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = JapaCardColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row() {
                Text(
                    text = japaInfoEntities.name.toTitleCase(),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = BrandColor
                    ),
                )
            }

            if (japaInfoEntities.target != null) {
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp),
                    text = japaInfoEntities.currentCount.formatWithCommas() + " of " + japaInfoEntities.target.formatWithCommas(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif,
                        color = BrandColor
                    ),
                )

                if (japaInfoEntities.currentCount > 0) {
                    val remainingCount = japaInfoEntities.target - japaInfoEntities.currentCount
                    Text(
                        modifier = Modifier
                            .padding(top = 4.dp),
                        text = "${remainingCount.formatWithCommas()} remaining",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.SansSerif,
                            color = LightTextColor
                        ),
                    )
                }
            } else {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = japaInfoEntities.currentCount.formatWithCommas() + " Chants",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif,
                        color = BrandColor
                    ),
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    thickness = 1.dp,
                    color = ProgressBarBgColor
                )

                Row(
                    modifier = Modifier.padding(top = 12.dp)
                ) {

                    Icon(
                        imageVector = Infinite,
                        contentDescription = null,
                        tint = BrandColor,
                        modifier = Modifier
                            .size(18.dp)
                            .align(Alignment.CenterVertically),
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 6.dp),
                        text = stringResource(R.string.no_target_set_title),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif,
                            color = BrandColor
                        ),
                    )
                }
            }

            if (progress != null) {
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    gapSize = 0.dp,
                    drawStopIndicator = {},
                    color = BrandColor,
                    trackColor = ProgressBarBgColor,
                    strokeCap = StrokeCap.Butt
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${progress.times(100).toInt()}% complete",
                        fontSize = 15.sp,
                        color = BrandColor,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val statusInfo = getJapaStatusUiInfo(japaInfoEntities.status)
                        if (japaInfoEntities.status == JapaStatus.COMPLETED) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = null,
                                tint = statusInfo.color,
                                modifier = Modifier
                                    .size(18.dp)
                                    .align(Alignment.CenterVertically),
                            )
                        } else {
                            Icon(
                                imageVector = DotCircle,
                                contentDescription = null,
                                tint = statusInfo.color,
                                modifier = Modifier
                                    .size(12.dp)
                                    .align(Alignment.CenterVertically),
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = statusInfo.statusName,
                            fontSize = 15.sp,
                            color = LightTextColor,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayEmptyListMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_japa),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .alpha(0.5f),
            colorFilter = ColorFilter.tint(BrandColor)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.empty_japa_list_message),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = FontFamily.Monospace,
                lineHeight = 24.sp
            ),
            textAlign = TextAlign.Center,
            color = LightTextColor
        )
    }
}
