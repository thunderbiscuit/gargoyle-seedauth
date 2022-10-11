/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.ui.onboarding

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.goldenraven.gargoyle.ui.theme.GargoyleTheme
import com.goldenraven.gargoyle.ui.theme.GargoyleTypography
import com.goldenraven.gargoyle.ui.theme.standardBorder
import com.goldenraven.gargoyle.ui.theme.standardShadow
import com.goldenraven.gargoyle.utils.KeychainCreateType
import com.goldenraven.gargoyle.utils.WordCheckResult
import com.goldenraven.gargoyle.utils.checkWords
import kotlinx.coroutines.launch

private const val TAG = "RecoveryScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecoveryScreen(
    onBuildKeychainButtonPressed: (KeychainCreateType) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    // val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(Color.White),
                    containerColor = Color.Red,
                    ) {
                    Text(
                        text = data.visuals.message,
                        style = TextStyle(Color.Black)
                    )
                }
            }
        },
    ) { innerPadding ->
        ConstraintLayout(
            modifier = Modifier.fillMaxHeight(1f)
        ) {
            val (screenName, body) = createRefs()

            val emptyRecoveryPhrase: Map<Int, String> = mapOf(
                1 to "", 2 to "", 3 to "", 4 to "", 5 to "", 6 to "",
                7 to "", 8 to "", 9 to "", 10 to "", 11 to "", 12 to ""
            )
            val (recoveryPhraseWordMap, setRecoveryPhraseWordMap) = remember { mutableStateOf(emptyRecoveryPhrase) }

            // Screen name
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(innerPadding)
                    .padding(top = 48.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .constrainAs(screenName) {
                        top.linkTo(parent.top)
                    }
            ) {
                Text(
                    text = "Recover a keychain",
                    style = GargoyleTypography.headlineSmall,
                    color = Color(0xff1f0208),
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
                )
                Text(
                    text = "Enter your 12-word recovery phrase below.",
                    style = GargoyleTypography.bodyMedium,
                    color = Color(0xff787878),
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
                )
            }

            // Body
            MyList(
                recoveryPhraseWordMap,
                setRecoveryPhraseWordMap,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .constrainAs(body) {
                        top.linkTo(screenName.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
                onBuildKeychainButtonPressed,
                snackbarHostState
            )
        }
    }
}

@Composable
fun MyList(
    recoveryPhraseWordMap: Map<Int, String>,
    setRecoveryPhraseWordMap: (Map<Int, String>) -> Unit,
    modifier: Modifier,
    onBuildKeychainButtonPressed: (KeychainCreateType) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column(
        modifier
            .fillMaxWidth(1f)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val focusManager = LocalFocusManager.current
        for (i in 1..12) {
            WordField(wordNumber = i, recoveryPhraseWordMap, setRecoveryPhraseWordMap, focusManager)
        }

        Button(
            onClick = {
                when (val wordCheck = checkWords(recoveryPhraseWordMap)) {
                    is WordCheckResult.SUCCESS -> {
                        Log.i(TAG, "All words passed the first check")
                        Log.i(TAG, "Recovery phrase is \"${wordCheck.recoveryPhrase}\"")
                        onBuildKeychainButtonPressed(KeychainCreateType.RECOVER(wordCheck.recoveryPhrase))
                    }
                    is WordCheckResult.FAILURE -> {
                        Log.i(TAG, "Not all words are valid")
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = wordCheck.errorMessage,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(Color(0xfff6cf47)),
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier
                .padding(top = 12.dp, start = 4.dp, end = 4.dp, bottom = 12.dp)
                .standardShadow(20.dp)
                .height(70.dp)
                .width(240.dp)
        ) {
            Text(
                "Recover keychain",
                style = GargoyleTypography.labelLarge,
                color = Color(0xff000000),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordField(
    wordNumber: Int,
    recoveryWordMap: Map<Int, String>,
    setRecoveryPhraseWordMap: (Map<Int, String>) -> Unit,
    focusManager: FocusManager
) {
    OutlinedTextField(
        value = recoveryWordMap[wordNumber] ?: "elvis is here",
        onValueChange = { newText ->
            val newMap: MutableMap<Int, String> = recoveryWordMap.toMutableMap()
            newMap[wordNumber] = newText

            val updatedMap = newMap.toMap()
            setRecoveryPhraseWordMap(updatedMap)
        },
        label = {
            Text(
                text = "Word $wordNumber",
                color = Color.LightGray,
            )
        },
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xfff6cf47),
            unfocusedBorderColor = Color.LightGray,
        ),
        modifier = Modifier
            .padding(8.dp),
        keyboardOptions = when (wordNumber) {
            12 -> KeyboardOptions(imeAction = ImeAction.Done)
            else -> KeyboardOptions(imeAction = ImeAction.Next)
        },
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = { focusManager.clearFocus() }
        ),
        singleLine = true,
    )
}

// @Preview(device = Devices.PIXEL_4, showBackground = true)
// @Composable
// internal fun RecoveryScreenPreview() {
//     GargoyleTheme {
//         RecoveryScreen(
//             // onBuildKeychainButtonPressed =
//         )
//     }
// }
