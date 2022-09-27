/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.goldenraven.gargoyle.ui.theme.GargoyleTypography
import com.goldenraven.gargoyle.utils.NavigationItem
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
internal fun HomeScreen() {
    val navController: NavHostController = rememberAnimatedNavController()
    // the splash screen hides the system bars
    // we need to bring them back on before continuing
    // ShowBars()

    Scaffold(bottomBar = { BottomNavigationBar(navController) }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HomeNavigation(
                navController = navController
            )
        }
    }
}

@Composable
internal fun BottomNavigationBar(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(
        NavigationItem.Scan,
        NavigationItem.Logins,
        NavigationItem.Menu
    )

    NavigationBar(
        tonalElevation = 0.dp,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    if (selectedItem == index) {
                        Icon(painter = painterResource(id = item.icon_filled), contentDescription = item.title)
                    } else {
                        Icon(painter = painterResource(id = item.icon_outline), contentDescription = item.title)
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        style = GargoyleTypography.labelSmall
                    )
                },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route)
                        }
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xff1f0208),
                    selectedTextColor = Color(0xff1f0208),
                    unselectedIconColor = Color(0xff8a8a8a),
                    unselectedTextColor = Color(0xff8a8a8a),
                    indicatorColor = Color(0xffffffff),
                )
            )
        }
    }
}
