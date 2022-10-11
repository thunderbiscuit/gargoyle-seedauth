/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.ui.extras

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.goldenraven.gargoyle.data.SecretsRepository
import com.goldenraven.gargoyle.ui.theme.GargoyleTheme
import com.goldenraven.gargoyle.ui.theme.GargoyleTypography

@Composable
internal fun RecoveryPhrase() {
    Column(
        modifier = Modifier
            .padding(top = 48.dp)
            .fillMaxSize()
    ) {
        // Title
        Text(
            text = "Recovery Phrase",
            style = GargoyleTypography.headlineSmall,
            color = Color(0xff1f0208),
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
        )
        Text(
            text = "Keep a back up of this on paper somewhere safe",
            style = GargoyleTypography.bodyMedium,
            color = Color(0xff787878),
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
        )
        Text(
            text = SecretsRepository.decryptRecoveryPhrase(),
            style = GargoyleTypography.bodyMedium,
            color = Color(0xff787878),
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
        )
    }
}

// Doesn't work, not sure why
// @Preview(device = Devices.PIXEL_4, showBackground = true)
// @Composable
// internal fun PreviewRecoveryPhraseScreen() {
//     GargoyleTheme {
//         RecoveryPhrase()
//     }
// }
