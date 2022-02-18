package ticketchain.mobile.worker.utils

import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.app.ActivityOptionsCompat
import java.util.*

@Composable
fun <I, O> launcherForActivityResult(
    contract: ActivityResultContract<I, O>,
    onResult: (O) -> Unit
): ActivityResultLauncher<I> {
    // Keep track of the current onResult listener
    val currentOnResult = rememberUpdatedState(onResult)

    // It doesn't really matter what the key is, just that it is unique
    // and consistent across configuration changes
    val key = rememberSaveable { UUID.randomUUID().toString() }

    val activityResultRegistry = checkNotNull(LocalActivityResultRegistryOwner.current) {
        "No ActivityResultRegistryOwner was provided via LocalActivityResultRegistryOwner"
    }.activityResultRegistry
    val realLauncher = remember(contract) { ActivityResultLauncherHolder<I>() }
    val returnedLauncher = remember(contract) {
        object : ActivityResultLauncher<I>() {
            override fun launch(input: I, options: ActivityOptionsCompat?) {
                realLauncher.launch(input, options)
            }

            override fun unregister() {
                error("Registration is automatically handled by rememberLauncherForActivityResult")
            }

            @Suppress("UNCHECKED_CAST")
            override fun getContract() = contract as ActivityResultContract<I, *>
        }
    }

    // DisposableEffect ensures that we only register once
    // and that we unregister when the composable is disposed
    DisposableEffect(activityResultRegistry, key, contract) {
        realLauncher.launcher = activityResultRegistry.register(key, contract) {
            currentOnResult.value(it)
        }
        onDispose {
            //realLauncher.unregister()
        }
    }
    return returnedLauncher
}

private class ActivityResultLauncherHolder<I> {
    var launcher: ActivityResultLauncher<I>? = null

    fun launch(input: I?, options: ActivityOptionsCompat?) {
        launcher?.launch(input, options) ?: error("Launcher has not been initialized")
    }

    fun unregister() {
        launcher?.unregister() ?: error("Launcher has not been initialized")
    }
}