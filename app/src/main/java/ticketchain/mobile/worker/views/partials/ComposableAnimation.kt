package ticketchain.mobile.worker.views.partials

import android.os.Bundle
import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@ExperimentalAnimationApi
fun NavGraphBuilder.animatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable (Bundle?) -> Unit
) {
    composable(route, arguments) {
        AnimatedVisibility(
            visible = true,
            enter = slideInHorizontally(
                initialOffsetX = { -50 }
            ) + fadeIn(initialAlpha = 0.0f),
            exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut(),
            initiallyVisible = false
        ) {
            content(it.arguments)
        }
    }
}