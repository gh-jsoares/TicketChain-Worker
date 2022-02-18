package ticketchain.mobile.worker.services


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import io.ktor.util.*
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class CameraService {

    private var previewView: PreviewView? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var cameraSelector: CameraSelector? = null
    private var preview: Preview? = null
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null

    companion object {

        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    private var failTimeout = -1

    private val screenAspectRatio: Int
        get() {
            // Get screen metrics used to setup camera for full screen resolution
            val metrics = DisplayMetrics().also { previewView?.display?.getRealMetrics(it) }
            return aspectRatio(metrics.widthPixels, metrics.heightPixels)
        }

    /**
     *  [androidx.camera.core.ImageAnalysis],[androidx.camera.core.Preview] requires enum value of
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *
     *  @param width - preview width
     *  @param height - preview height
     *  @return suitable aspect ratio
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    fun initCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        onSuccess: (String) -> Unit,
        onFail: () -> Unit
    ) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture?.addListener({
            val cameraProvider = cameraProviderFuture?.get()
            bindAnalysis(
                lifecycleOwner,
                previewView,
                cameraProvider!!,
                onSuccess,
                onFail
            )
        }, context.mainExecutor)
    }

    fun bindAnalysis(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        cameraProvider: ProcessCameraProvider,
        onSuccess: (String) -> Unit,
        onFail: () -> Unit
    ) {

        this.previewView = previewView

        if (cameraSelector == null) {
            cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
        }

        if (preview == null) {
            preview = Preview.Builder().build()
        }

        preview!!.setSurfaceProvider(previewView.surfaceProvider)

        try {
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector!!, preview!!)
        } catch (e: IllegalStateException) {
            e.message?.let { Log.e(ContentValues.TAG, it) }
        } catch (e: IllegalArgumentException) {
            e.message?.let { Log.e(ContentValues.TAG, it) }
        }

        val barcodeScanner = BarcodeScanning.getClient()
        analysisUseCase?.let {
            cameraProvider.unbind(it)
        }

        analysisUseCase = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(previewView.display.rotation)
            .build()
        val cameraExecutor = Executors.newSingleThreadExecutor()

        analysisUseCase?.setAnalyzer(cameraExecutor, { imageProxy ->
            processImageProxy(
                barcodeScanner,
                imageProxy,
                onSuccess,
                onFail
            )
        })

        try {
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector!!, analysisUseCase!!)
        } catch (e: IllegalStateException) {
            e.message?.let { Log.e(ContentValues.TAG, it) }
        } catch (e: IllegalArgumentException) {
            e.message?.let { Log.e(ContentValues.TAG, it) }
        }
    }

    @SuppressLint("UnsafeExperimentalUsageError", "RestrictedApi", "UnsafeOptInUsageError")
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy,
        onSuccess: (String) -> Unit,
        onFail: () -> Unit
    ) {
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                barcodes.forEach { barcode ->
                    barcode.rawValue?.let { it ->
                        if (true) {
                            onSuccess(it)
                            imageProxy.close()
                            barcodeScanner.close()
                        } else {
                            if (failTimeout < 0) {
                                onFail()
                                failTimeout = 60 // TO AVOID SPAMMING
                            }
                        }
                    }

                    if (failTimeout >= 0) {
                        failTimeout -= 1
                    }
                }
            }.addOnCompleteListener {
                barcodeScanner.close()
                imageProxy.close()
            }
    }
}
