/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeNavigation(
    navController: NavHostController,
) {
    val animationDuration = 400

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.ScanScreen.route,
    ) {

        // Scan
        composable(
            route = Screen.ScanScreen.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
        ) { ScanScreen() }

        // Logins List
        composable(
            route = Screen.LoginsScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    "scan_screen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    "menu_screen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    else          -> fadeIn(animationSpec = tween(1000))
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "scan_screen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    "menu_screen" -> slideIntoContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    else          -> fadeIn(animationSpec = tween(1000))
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    "scan_screen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    "menu_screen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    else          -> fadeOut(animationSpec = tween(1000))
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    "scan_screen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
                    "menu_screen" -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
                    else          -> fadeOut(animationSpec = tween(1000))
                }
            }
        ) { LoginsScreen() }


        // Menu
        composable(
            route = Screen.MenuScreen.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Start, animationSpec = tween(animationDuration))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.End, animationSpec = tween(animationDuration))
            }
        ) { MenuScreen() }
    }
}
