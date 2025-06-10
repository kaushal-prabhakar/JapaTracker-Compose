package com.kaushal.japacountercompose.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.ui.JapaAppScreens
import com.kaushal.japacountercompose.ui.theme.BrandColor


@Composable
fun JapaListScreen(navController: NavController) {
    JapaListContent(
        onItemClick = { item ->
            navController.navigate(item.name)
        },
        onBackClick = {
            navController.popBackStack()
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JapaListContent(
    onItemClick: (JapaAppScreens) -> Unit,
    onBackClick : () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(
                text = stringResource(id = R.string.japa_list),
                fontFamily = FontFamily.Monospace)},
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
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

            }
        }
    )
}

@Composable
fun DisplayEmptyListMessage() {
    Text(text = stringResource(id = R.string.empty_japa_list_message))
}

@Preview
@Composable
fun JapaListContentPreview() {
    JapaListContent(onItemClick = {}, onBackClick = {})
}