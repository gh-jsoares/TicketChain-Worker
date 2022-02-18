package ticketchain.mobile.worker.controllers

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SnackbarController(
    private val snackbarHostState: SnackbarHostState,
    private val lifecycleScope: LifecycleCoroutineScope
) {
    private var snackbarJob: Job? = null
    private var currentOnDismiss: (() -> Unit)? = null

    fun dismissActiveSnackbar() {
        snackbarJob?.let { job ->
            job.cancel()
            currentOnDismiss?.let {
                it()
                currentOnDismiss = null
            }
        }
    }

    fun showDismissibleSnackbar(
        message: String,
        actionLabel: String? = "Dismiss",
        duration: SnackbarDuration = SnackbarDuration.Short,
        onDismiss: (() -> Unit)? = null
    ) {
        if (snackbarJob != null) {
            dismissActiveSnackbar()
        }
        snackbarJob = lifecycleScope.launch {
            currentOnDismiss = onDismiss
            snackbarHostState.showSnackbar(message, actionLabel, duration)
            dismissActiveSnackbar()
        }
    }
}
