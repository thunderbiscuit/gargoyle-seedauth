/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.goldenraven.gargoyle.ui.theme.GargoyleTheme
import com.goldenraven.gargoyle.ui.theme.GargoyleTypography

@Composable
internal fun LoginsScreen() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (title, body) = createRefs()
        val verticalChain = createVerticalChain(title, body, chainStyle = ChainStyle.SpreadInside)

        Column(

            modifier = Modifier
                .padding(top = 48.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.absoluteLeft)
                    end.linkTo(parent.absoluteRight)
                    bottom.linkTo(body.top)
                }
        ) {
            // Title
            Text(
                text = "Login History",
                style = GargoyleTypography.headlineSmall,
                color = Color(0xff1f0208),
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp, bottom = 8.dp)
            )
            Text(
                text = "These are the domains you have previously logged into with this keychain",
                style = GargoyleTypography.bodyMedium,
                color = Color(0xff787878),
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp)
            )
        }
        Surface(
            modifier = Modifier
                .constrainAs(body) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
        ) {}
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
internal fun PreviewLoginsScreen() {
    GargoyleTheme {
        LoginsScreen()
    }
}
