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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import com.goldenraven.gargoyle.R
import com.goldenraven.gargoyle.ui.theme.GargoyleTypography
import com.goldenraven.gargoyle.ui.theme.standardBorder
import com.goldenraven.gargoyle.ui.theme.standardShadow
import com.goldenraven.gargoyle.utils.QrCodeAnalyzer

@Composable
internal fun ScanScreen() {
    var scannerOpen: Boolean by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Scan a QR Code",
            style = GargoyleTypography.headlineSmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Scan a QR code containing a SeedAuth (lnurl-auth) URL",
            style = GargoyleTypography.bodyMedium,
            color = Color(0xff787878)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 90.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            border = standardBorder,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .standardShadow(20.dp)
                .align(Alignment.CenterHorizontally)
                .size(300.dp, 400.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xffd9d9d9)
            ) {
                ScannerBox(scannerOpen)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        if (!scannerOpen) {
            Button(
                onClick = { scannerOpen = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xfff6cf47)),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .padding(all = 4.dp)
                    .standardShadow(20.dp)
                    // .weight(weight = 0.5f)
                    .height(70.dp)
                    .width(140.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "Scan",
                        style = GargoyleTypography.labelLarge,
                        color = Color(0xff000000)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.hicon_scan_1),
                        contentDescription = "Scan icon",
                        tint = Color(0xff000000)
                    )
                }
            }
        } else {
            Button(
                onClick = { scannerOpen = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xfff6cf47)),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .padding(all = 4.dp)
                    .standardShadow(20.dp)
                    .height(70.dp)
                    .width(140.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "Cancel",
                        style = GargoyleTypography.labelLarge,
                        color = Color(0xff000000)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.hicon_close_circle),
                        contentDescription = "Cancel scan",
                        tint = Color(0xff000000)
                    )
                }
            }
        }
    }
}

@Composable
fun ScannerBox(scannerOpen: Boolean) {
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
    var code by remember {
        mutableStateOf("")
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

    ConstraintLayout(
        // modifier = Modifier.size(300.dp, 300.dp)
    ) {
        val (camera) = createRefs()

        Box(
            modifier = Modifier
                .constrainAs(camera) {
                    top.linkTo(parent.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                    bottom.linkTo(parent.bottom)
                }
                .padding(0.dp).clip(RoundedCornerShape(20.dp))
        ) {
            Column(
                // modifier = Modifier.fillMaxSize()
            ) {
                if (hasCameraPermission && scannerOpen) {
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
                                    Log.i("LogScreen", "QR code is $result")
                                    code = result
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
                    // Text(
                    //     text = "QR code value: $code",
                    //     modifier = Modifier.fillMaxWidth()
                    // )
                }
            }
        }
    }
}
