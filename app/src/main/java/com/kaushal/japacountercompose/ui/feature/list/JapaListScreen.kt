package com.kaushal.japacountercompose.ui.feature.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kaushal.japacountercompose.domain.JapaInfoEntities
import com.kaushal.japacountercompose.domain.JapaStatus
import com.kaushal.japacountercompose.domain.Outcome
import com.kaushal.japacountercompose.domain.UpdateType
import com.kaushal.japacountercompose.ui.JapaAppScreens
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        japaListOutcome = japaListOutcome
    )
}

@Preview
@Composable
fun JapaListContentPreview() {
    JapaListScreenContent(
        onJapaClick = {},
        onAddClick = {},
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
                        .format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
                )
            )
        )
    )
}
