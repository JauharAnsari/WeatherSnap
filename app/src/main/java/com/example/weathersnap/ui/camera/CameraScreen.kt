package com.example.weathersnap.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

@Composable
fun CameraScreen(
    onCaptureComplete: (imagePath: String, originalSizeKb: Long, compressedSizeKb: Long) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }
    
    var isCapturing by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )
                    } catch (exc: Exception) {
                        Log.e("CameraScreen", "Use case binding failed", exc)
                    }
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp).statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Custom Camera",
                color = Color.White,
                fontSize = 20.sp
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }
        
        Button(
            onClick = {
                if (!isCapturing) {
                    isCapturing = true
                    captureAndCompressImage(context, imageCapture) { path, orig, comp ->
                        isCapturing = false
                        if (path != null) {
                            onCaptureComplete(path, orig, comp)
                        }
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .height(56.dp)
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(28.dp),
            enabled = !isCapturing
        ) {
            Text(if (isCapturing) "Capturing..." else "Capture", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

private fun captureAndCompressImage(
    context: Context,
    imageCapture: ImageCapture,
    onResult: (String?, Long, Long) -> Unit
) {
    val photoFile = File(context.cacheDir, "temp_photo_${System.currentTimeMillis()}.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val originalSize = photoFile.length() / 1024
                
                val compressedFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
                val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                
                val maxDim = 1024
                val scale = if (bitmap.width > maxDim || bitmap.height > maxDim) {
                    maxDim.toFloat() / Math.max(bitmap.width, bitmap.height)
                } else 1.0f
                
                val scaledBitmap = if (scale < 1.0f) {
                    Bitmap.createScaledBitmap(
                        bitmap,
                        (bitmap.width * scale).toInt(),
                        (bitmap.height * scale).toInt(),
                        true
                    )
                } else bitmap
                
                FileOutputStream(compressedFile).use { out ->
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 60, out)
                }
                
                if (scaledBitmap != bitmap) {
                    scaledBitmap.recycle()
                }
                bitmap.recycle()
                
                val compressedSize = compressedFile.length() / 1024
                photoFile.delete()
                
                onResult(compressedFile.absolutePath, originalSize, compressedSize)
            }

            override fun onError(exc: ImageCaptureException) {
                Log.e("CameraScreen", "Photo capture failed: ${exc.message}", exc)
                onResult(null, 0L, 0L)
            }
        }
    )
}
