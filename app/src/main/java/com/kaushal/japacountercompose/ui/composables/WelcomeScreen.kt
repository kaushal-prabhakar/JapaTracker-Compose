package com.kaushal.japacountercompose.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kaushal.japacountercompose.R
import com.kaushal.japacountercompose.ui.JapaAppScreens
import com.kaushal.japacountercompose.ui.theme.BrandColor

@Composable
fun WelcomeScreen(navController: NavHostController) {

   WelcomeScreenContent(
       onClick = {
           navController.navigate(JapaAppScreens.japaList.name)
       }
   )
}

@Composable
fun WelcomeScreenContent(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
    ) {

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.brand_logo_bg),
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )

        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp, 32.dp)
        ) {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )

            Text(
                text = stringResource(id = R.string.welcome_screen_text_1),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = FontFamily.Monospace
            )

            Text(
                text = stringResource(id = R.string.welcome_screen_text_2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .clickable(onClick = {
                    onClick.invoke()
                })
                .background(color = BrandColor)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.Center,
            ) {

                Text(
                    text = stringResource(id = R.string.welcome_screen_button_text),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(
                    modifier = Modifier
                        .width(8.dp)
                )

                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewWelcomeScreen() {
    WelcomeScreenContent(
        onClick = {}
    )
}