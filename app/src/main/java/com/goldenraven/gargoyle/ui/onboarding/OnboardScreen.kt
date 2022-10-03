/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.ui.onboarding

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.goldenraven.gargoyle.R
import com.goldenraven.gargoyle.ui.theme.GargoyleTypography
import com.goldenraven.gargoyle.ui.theme.standardBorder
import com.goldenraven.gargoyle.ui.theme.standardShadow

@Composable
fun OnboardScreen() {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (header, body, recover) = createRefs()
        val verticalChain =
            createVerticalChain(header, body, recover, chainStyle = ChainStyle.SpreadInside)

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp)
                .constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(body.top)
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_keyring),
                contentDescription = "Keyring logo",
                tint = Color(0xff000000),
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = "Gargoyle",
                style = GargoyleTypography.headlineLarge,
                fontSize = 48.sp,
                color = Color(0xff1f0208),
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
            )
            Text(
                text = "Authenticator",
                style = GargoyleTypography.titleLarge,
                color = Color(0xff1f0208),
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .constrainAs(body) {
                    top.linkTo(header.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(recover.top)
                }
        ) {
            Text(
                text = "Welcome!\n\n" +
                        "Gargoyle is an application that implements the SeedAuth (also called lnurl-auth) standard to allow you to authenticate to websites and services.\n\n" +
                        "If you don’t have a keychain already, you’ll need to create one:\n\n",
                style = GargoyleTypography.bodyLarge,
                fontSize = 18.sp,
                color = Color(0xff787878),
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
            )

            Button(
                onClick = {
                    Log.i("OnboardScreen", "Creating a keychain")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xfff6cf47)),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 24.dp)
                    .standardShadow(20.dp)
                    .height(70.dp)
                    .width(240.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "Create a keychain",
                        style = GargoyleTypography.labelLarge,
                        color = Color(0xff000000)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_hicon_lock_3),
                        contentDescription = "Lock icon",
                        tint = Color(0xff000000)
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .constrainAs(recover) {
                    top.linkTo(body.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Already have a keychain?",
                style = GargoyleTypography.bodyMedium,
                color = Color(0xff787878),
                modifier = Modifier
                    .padding(start = 24.dp, bottom = 8.dp)
                    .height(40.dp)
            )
            TextButton(
                onClick = {
                    Log.i("OnboardScreen", "Creating a keychain")
                },
                modifier = Modifier
                    .width(120.dp)
                    .padding(0.dp)
                    .height(70.dp)
            ) {
                Text(
                    text = "Recover it here",
                    color = Color(0xff787878),
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }
    }
}