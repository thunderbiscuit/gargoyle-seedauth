/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.goldenraven.gargoyle.ui.HomeScreen
import com.goldenraven.gargoyle.ui.theme.GargoyleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GargoyleTheme {
                HomeScreen()
            }
        }
    }
}
