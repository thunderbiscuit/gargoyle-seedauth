/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.ui

sealed class Screen(val route: String) {
    object ScanScreen : Screen("scan_screen")
    object LoginsScreen : Screen("logins_screen")
    object MenuScreen : Screen("menu_screen")
}
