@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.kaushal.japacountercompose.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.data.JapaInfoEntities
import com.kaushal.japacountercompose.data.Outcome
import com.kaushal.japacountercompose.ui.JapaAppScreens
import com.kaushal.japacountercompose.ui.theme.AlphaBrandColor
import com.kaushal.japacountercompose.ui.theme.BrandColor
import com.kaushal.japacountercompose.ui.theme.Completed
import com.kaushal.japacountercompose.ui.theme.EmptyJapaListText
import com.kaushal.japacountercompose.ui.viewmodels.JapaListViewModel


@Composable
fun JapaListScreen(navController: NavController, viewModel: JapaListViewModel = hiltViewModel()) {
    JapaListScreenContent(
        onItemClick = { item ->
            navController.navigate(item.name)
        },
        onBackClick = {
            navController.popBackStack()
        },
        viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JapaListScreenContent(
    onItemClick: (JapaAppScreens) -> Unit,
    onBackClick: () -> Unit,
    viewModel: JapaListViewModel
) {

    LaunchedEffect(Unit) {
        viewModel.getMyJapaList()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
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
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onItemClick(JapaAppScreens.addJapa)
                },
                containerColor = BrandColor,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { padding ->

            val japaListOutcome by viewModel.japaListOutcome.collectAsState(initial = Outcome.loading())

            when (japaListOutcome) {
                is Outcome.InProgress -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is Outcome.Failure -> {
                    val msg = (japaListOutcome as Outcome.Failure).message
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error loading Japa List $msg",
                            color = Color.Red
                        )
                    }
                }

                is Outcome.Success<*> -> {
                    val list = (japaListOutcome as Outcome.Success<*>).data
                    if ((list as List<*>).isEmpty()) {
                        DisplayEmptyListMessage()
                    } else {
                        JapaListItem(padding, list as List<JapaInfoEntities>)
                    }
                }
            }
        })
}

@Composable
fun JapaListItem(padding: PaddingValues, japaList: List<JapaInfoEntities>) {
    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(japaList) { item ->
            JapaCard(japaInfoEntities = item)
        }
    }
}

@Composable
fun JapaCard(japaInfoEntities: JapaInfoEntities) {

    val progress = japaInfoEntities.target?.let {
        japaInfoEntities.currentCount.toFloat() / it.toFloat()
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
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

                InfoRow(label = "Target", value = japaInfoEntities.target.toString())
                InfoRow(label = "Current Count", value = japaInfoEntities.currentCount.toString())
                Spacer(modifier = Modifier.height(8.dp))

                if (progress != null && !progress.isNaN()) {
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
    JapaListScreenContent(onItemClick = {}, onBackClick = {}, viewModel = viewModel())
}