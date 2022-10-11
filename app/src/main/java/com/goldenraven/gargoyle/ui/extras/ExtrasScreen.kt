/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.ui.extras

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.gargoyle.R
import com.goldenraven.gargoyle.ui.Screen
import com.goldenraven.gargoyle.ui.theme.GargoyleTheme
import com.goldenraven.gargoyle.ui.theme.GargoyleTypography

@Composable
internal fun ExtrasScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .padding(top = 48.dp)
            .fillMaxSize()
    ) {
        // Title
        Text(
            text = "Extras",
            style = GargoyleTypography.headlineSmall,
            color = Color(0xff1f0208),
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
        )
        Text(
            text = "A collection of everything else",
            style = GargoyleTypography.bodyMedium,
            color = Color(0xff787878),
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
        )

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xfff0f0f0)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .size(width = 400.dp, height = 80.dp)
                .padding(start = 24.dp, end = 24.dp, top = 24.dp)
        ) {
            Text(
                text = "How to use this app",
                fontWeight = FontWeight.Normal,
                color = Color(0xff2f2f2f)

            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_hicon_right_2),
                contentDescription = "Scan icon",
                tint = Color(0xff000000)
            )
        }

        Button(
            onClick = {
                navController.navigate(Screen.RecoveryPhraseScreen.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route)
                    }
                    launchSingleTop = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xfff0f0f0)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .size(width = 400.dp, height = 80.dp)
                .padding(start = 24.dp, end = 24.dp, top = 24.dp)
        ) {
            Text(
                text = "Recovery phrase",
                fontWeight = FontWeight.Normal,
                color = Color(0xff2f2f2f)

            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_hicon_right_2),
                contentDescription = "Scan icon",
                tint = Color(0xff000000)
            )
        }

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xfff0f0f0)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .size(width = 400.dp, height = 80.dp)
                .padding(start = 24.dp, end = 24.dp, top = 24.dp)
        ) {
            Text(
                text = "About",
                fontWeight = FontWeight.Normal,
                color = Color(0xff2f2f2f)

            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_hicon_right_2),
                contentDescription = "Scan icon",
                tint = Color(0xff000000)
            )
        }

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xfff0f0f0)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .size(width = 400.dp, height = 80.dp)
                .padding(start = 24.dp, end = 24.dp, top = 24.dp)
        ) {
            Text(
                text = "Privacy",
                fontWeight = FontWeight.Normal,
                color = Color(0xff2f2f2f)

            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_hicon_right_2),
                contentDescription = "Scan icon",
                tint = Color(0xff000000)
            )
        }
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
internal fun PreviewExtrasScreen() {
    GargoyleTheme {
        ExtrasScreen(navController = rememberNavController())
    }
}
