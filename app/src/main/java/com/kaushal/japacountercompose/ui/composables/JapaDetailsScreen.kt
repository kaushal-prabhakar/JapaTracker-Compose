package com.kaushal.japacountercompose.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.ui.theme.BrandColor

@Composable
fun JapaDetailsScreen(navController: NavController) {
    JapaDetailsContent(onUpdateClick = {}, onDeleteClick = {}, onBackClick = {
        navController.popBackStack()
    })
}

@Preview
@Composable
fun JapaDetailsScreenPreview() {
    JapaDetailsContent(onUpdateClick = {}, onDeleteClick = {}, onBackClick = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JapaDetailsContent(
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Details Screen", fontFamily = FontFamily.Monospace)
            },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrandColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { onBackClick.invoke() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            CustomLargeButton(onClick = { onUpdateClick.invoke() }, label = stringResource(id = R.string.update_count) )
        },

        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .background(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.brand_logo_bg),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = stringResource(id = R.string.details_screen_target_message, 10000),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 40.dp, 8.dp, 0.dp)
                            .align(Alignment.TopCenter),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = stringResource(id = R.string.current_count, 5000),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Start,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Text(
                        text = stringResource(
                            id = R.string.last_updated,
                            54,
                            "19th May 2025 13:30"
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Start,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Text(
                        text = stringResource(
                            id = R.string.active_status,
                            54,
                            "19th May 2025 13:30"
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Start,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    )

}

@Preview
@Composable
fun UpdateCountDialog() {
    Dialog(onDismissRequest = {  }) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Column {
                Text(text =  "Update Count",
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
                    value = "current count",
                    onValueChange = {  },
                    label = {
                        Text("Current Count", fontFamily = FontFamily.Monospace)
                    })

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 2.dp),
                    value = "",
                    onValueChange = {  },
                    label = {
                        Text("Enter New Count", fontFamily = FontFamily.Monospace)
                    }
                )

                Text(text =  stringResource(id = R.string.new_count_value_description),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomSmallButton(
                        onClick = { /*TODO*/ },
                        stringResource(id = R.string.add),
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )

                    CustomSmallButton(
                        onClick = { /*TODO*/ },
                        stringResource(id = R.string.deduct),
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )

                }
            }
        }
    }
}