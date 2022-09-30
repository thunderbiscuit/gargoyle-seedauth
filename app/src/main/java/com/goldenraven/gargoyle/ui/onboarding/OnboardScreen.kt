package com.goldenraven.gargoyle.ui.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun OnboardScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xffbbaabb)
    ) {
        Text(
            "Onboard Screen"
        )
    }
}



