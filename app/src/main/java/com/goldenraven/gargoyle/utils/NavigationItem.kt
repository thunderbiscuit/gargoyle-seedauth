/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.utils

import com.goldenraven.gargoyle.R
import com.goldenraven.gargoyle.ui.Screen

sealed class NavigationItem(val route: String, val icon_filled: Int, val icon_outline: Int, val title: String) {
    object Scan : NavigationItem(route = Screen.ScanScreen.route, icon_filled = R.drawable.ic_hicon_scan_6, icon_outline = R.drawable.ic_hicon_scan_6, title = "Scan")
    object Logins : NavigationItem(route = Screen.LoginsScreen.route, icon_filled = R.drawable.ic_hicon_password_1, icon_outline = R.drawable.ic_hicon_password_1, title = "Logins")
    object Menu : NavigationItem(route = Screen.MenuScreen.route, icon_filled = R.drawable.ic_hicon_menu, icon_outline = R.drawable.ic_hicon_menu, title = "Menu")
}
