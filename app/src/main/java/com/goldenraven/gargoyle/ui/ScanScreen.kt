/*
 * Copyright 2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.gargoyle.ui

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import com.goldenraven.gargoyle.R
import com.goldenraven.gargoyle.network.KtorClient
import com.goldenraven.gargoyle.ui.theme.GargoyleTypography
import com.goldenraven.gargoyle.ui.theme.standardBorder
import com.goldenraven.gargoyle.ui.theme.standardShadow
import com.goldenraven.gargoyle.utils.QrCodeAnalyzer
import kotlinx.coroutines.runBlocking

@Composable
internal fun ScanScreen() {
    val scannerOpen = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }
    var code by remember { mutableStateOf("") }

    fun showDialog(domain: String) {
        code = domain
        scannerOpen.value = false
        openDialog.value = true
    }

    if (openDialog.value) {
        LoginDialog(
            domain = code,
            openDialog = openDialog
        )
    }
    
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (title, scanner, button) = createRefs()
        val verticalChain = createVerticalChain(title, scanner, button, chainStyle = ChainStyle.SpreadInside)

        Column(

            modifier = Modifier
                .padding(top = 48.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.absoluteLeft)
                    end.linkTo(parent.absoluteRight)
                    bottom.linkTo(scanner.top)
                }
        ) {
            // Title
            Text(
                text = "Scan a QR Code",
                style = GargoyleTypography.headlineSmall,
                color = Color(0xff1f0208),
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp, bottom = 8.dp)
            )
            Text(
                text = "Scan a QR code containing a SeedAuth (lnurl-auth) URL",
                style = GargoyleTypography.bodyMedium,
                color = Color(0xff787878),
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp)
            )
        }

        // Scanner
        Card(
            border = standardBorder,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .standardShadow(20.dp)
                .size(300.dp, 340.dp)
                .constrainAs(scanner) {
                    top.linkTo(title.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                    bottom.linkTo(button.top)
                }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xffd9d9d9)
            ) {
                ScannerBox(scannerOpen, ::showDialog)
            }
        }

        // Scan/Cancel button
        Button(
            onClick = {
                Log.i("ScanScreen", "We're changing the value of scannerOpen")
                scannerOpen.value = !scannerOpen.value
                // runBlocking {
                //     val client = KtorClient()
                //     client.login()
                // }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xfff6cf47)),
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier
                // .padding(all = 4.dp)
                .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 24.dp)
                .standardShadow(20.dp)
                .height(70.dp)
                .width(140.dp)
                .constrainAs(button) {
                    start.linkTo(parent.absoluteLeft)
                    end.linkTo(parent.absoluteRight)
                    bottom.linkTo(parent.bottom, 48.dp)
                }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = if (!scannerOpen.value) "Scan" else "Cancel",
                    style = GargoyleTypography.labelLarge,
                    color = Color(0xff000000)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = if (!scannerOpen.value) painterResource(id = R.drawable.ic_hicon_scan_1) else painterResource(id = R.drawable.ic_hicon_close_circle),
                    contentDescription = if (!scannerOpen.value) "Scan icon" else "Cancel icon",
                    tint = Color(0xff000000)
                )
            }
        }
    }
}

@Composable
fun ScannerBox(scannerOpen: MutableState<Boolean>, showDialog: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )
    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    ConstraintLayout {
        val (camera) = createRefs()

        Box(
            modifier = Modifier
                .constrainAs(camera) {
                    top.linkTo(parent.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                    bottom.linkTo(parent.bottom)
                }
                .padding(0.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Column {
                if (hasCameraPermission && scannerOpen.value) {
                    AndroidView(
                        factory = { context ->
                            val previewView = PreviewView(context)
                            val preview = Preview.Builder().build()
                            val selector = CameraSelector.Builder()
                                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                .build()
                            preview.setSurfaceProvider(previewView.surfaceProvider)
                            val imageAnalysis = ImageAnalysis.Builder()
                                // this is in the video here https://www.youtube.com/watch?v=asl1mFtkMkc
                                // but prevents the code analyzer from seeing the image as a QR code
                                // .setTargetResolution(Size(
                                //     previewView.width,
                                //     previewView.height)
                                // )
                                .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                                .build()
                            imageAnalysis.setAnalyzer(
                                ContextCompat.getMainExecutor(context),
                                QrCodeAnalyzer { result ->
                                    Log.i("ScanScreen", "QR code is $result")
                                    showDialog(result)
                                }
                            )

                            try {
                                cameraProviderFuture.get().bindToLifecycle(
                                    lifecycleOwner,
                                    selector,
                                    preview,
                                    imageAnalysis
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            previewView
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// TODO: add icon
@Composable
private fun LoginDialog(domain: String, openDialog: MutableState<Boolean>) {
    AlertDialog(
        // modifier = Modifier.height(400.dp),
        onDismissRequest = {},
        title = {
            Text(
                text = "Login",
                style = GargoyleTypography.headlineMedium,
                color = Color(0xff1f0208)
            )
        },
        text = {
            Text(
                text = "You are about to login to the following domain: $domain",
                color = Color(0xff2f2f2f)
            )
        },

        dismissButton = {
            Button(
                onClick = {
                    Log.i("ScanScreen", "User declined the login")
                    openDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xfffc4f4f)),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 24.dp)
                    .standardShadow(20.dp)
                    .height(70.dp)
                    .width(100.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "",
                        style = GargoyleTypography.labelMedium,
                        color = Color(0xff000000)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_hicon_dislike),
                        contentDescription = "Do not log in icon",
                        tint = Color(0xff000000)
                    )
                }
            }
        },

        confirmButton = {
            Button(
                onClick = {
                    Log.i("ScanScreen", "User accepted the login")
                    runBlocking {
                        val client = KtorClient()
                        client.login()
                    }
                    openDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff78c5b2)),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 24.dp)
                    .standardShadow(20.dp)
                    .height(70.dp)
                    .width(100.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "",
                        style = GargoyleTypography.labelMedium,
                        color = Color(0xff000000)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_hicon_like),
                        contentDescription = "Proceed icon",
                        tint = Color(0xff000000)
                    )
                }
            }
        },
    )
}
