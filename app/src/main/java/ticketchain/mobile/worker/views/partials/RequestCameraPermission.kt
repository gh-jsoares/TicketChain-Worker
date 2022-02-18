package ticketchain.mobile.worker.views.partials

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import ticketchain.mobile.worker.controllers.SnackbarController
import ticketchain.mobile.worker.utils.launcherForActivityResult

@SuppressLint("RestrictedApi")
@ExperimentalAnimationApi
@Composable
fun RequestCameraPermission(
    snackbarController: SnackbarController,
    onFail: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(true) }
    var canUseCamera by remember { mutableStateOf(false) }

    /**
     * If there is a list in my location show list
     */

    val launcher =
        launcherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                snackbarController.showDismissibleSnackbar("Cannot access camera")
                loading = false
                onFail()
            }

            canUseCamera = granted
        }


    AnimatedVisibility(visible = loading) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) -> {
                canUseCamera = true
            }
            else -> {
                LaunchedEffect(null) {
                    // needs to wait for launcher to finish initializing
                    delay(100L)
                    launcher.launch(Manifest.permission.CAMERA)
                }
            }
        }
        loading = false

        Text("Loading")
    }

    AnimatedVisibility(visible = !loading && canUseCamera) {
        content()
    }
}