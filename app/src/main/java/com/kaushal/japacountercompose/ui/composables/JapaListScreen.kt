package com.kaushal.japacountercompose.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.data.JapaInfoEntities
import com.kaushal.japacountercompose.data.JapaStatus
import com.kaushal.japacountercompose.data.Outcome
import com.kaushal.japacountercompose.data.UpdateType
import com.kaushal.japacountercompose.ui.JapaAppScreens
import com.kaushal.japacountercompose.ui.theme.AlphaBrandColor
import com.kaushal.japacountercompose.ui.theme.BrandColor
import com.kaushal.japacountercompose.ui.theme.Completed
import com.kaushal.japacountercompose.ui.theme.EmptyJapaListText
import com.kaushal.japacountercompose.ui.viewmodels.JapaListViewModel
import java.time.LocalDateTime


@Composable
fun JapaListScreen(navController: NavController, viewModel: JapaListViewModel = hiltViewModel()) {
    val japaListOutcome by viewModel.japaListOutcome.collectAsState()

    JapaListScreenContent(
        onJapaClick = { japaId ->
            navController.navigate("${JapaAppScreens.japaDetails.name}/$japaId")
        },
        onAddClick = {
            navController.navigate(JapaAppScreens.addJapa.name)
        },
        onBackClick = {
            navController.popBackStack()
        },
        japaListOutcome = japaListOutcome
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JapaListScreenContent(
    onJapaClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit,
    japaListOutcome: Outcome<List<JapaInfoEntities>>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.japa_list),
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddClick() },
                containerColor = BrandColor,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Japa")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { padding ->
            when (japaListOutcome) {
                is Outcome.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is Outcome.Failure -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error loading Japa List: ${japaListOutcome.message}",
                            color = Color.Red
                        )
                    }
                }

                is Outcome.Success -> {
                    val list = japaListOutcome.data
                    if (list.isEmpty()) {
                        DisplayEmptyListMessage()
                    } else {
                        JapaListItem(padding, list, onJapaClick)
                    }
                }
            }
        })
}

@Composable
fun JapaListItem(
    padding: PaddingValues,
    japaList: List<JapaInfoEntities>,
    onJapaClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
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
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick() }
    ) {

        Row(
            modifier = Modifier
                .background(AlphaBrandColor)
                .padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_japa),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, BrandColor, CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = japaInfoEntities.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 20.sp,
                            color = BrandColor
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    StatusBadge(status = japaInfoEntities.status)
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (japaInfoEntities.target != null) {
                    InfoRow(label = "Target", value = japaInfoEntities.target.toString())
                }
                InfoRow(label = "Current Count", value = japaInfoEntities.currentCount.toString())
                Spacer(modifier = Modifier.height(8.dp))

                if (progress != null) {
                    LinearProgressIndicator(
                        progress = { progress.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Completed,
                        trackColor = BrandColor,
                        strokeCap = StrokeCap.Round
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Progress: ${progress.times(100).toInt()}%",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.align(Alignment.End)
                    )
                } else {
                    Text(
                        text = "No specific Target set for this Japa",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.align(Alignment.End)
                    )
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
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.empty_japa_list_message),
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center,
            color = EmptyJapaListText,
            fontSize = dimensionResource(id = R.dimen.large_text).value.sp
        )
    }
}

@Preview
@Composable
fun JapaListContentPreview() {
    JapaListScreenContent(
        onJapaClick = {},
        onAddClick = {},
        onBackClick = {},
        japaListOutcome = Outcome.Success(
            listOf(
                JapaInfoEntities(
                    id = 1,
                    name = "Maha Mantra",
                    target = 108,
                    status = JapaStatus.ACTIVE,
                    currentCount = 54,
                    lastUpdatedValue = 1,
                    lastUpdatedType = UpdateType.INCREMENT,
                    lastUpdatedTime = LocalDateTime.now()
                )
            )
        )
    )
}
