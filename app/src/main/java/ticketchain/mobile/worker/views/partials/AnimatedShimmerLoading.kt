package ticketchain.mobile.worker.views.partials

import androidx.compose.animation.core.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity

object ShimmerAnimationDefinitions {

    fun calcGradientWidth(height: Float): Float {
        return 0.2f * height
    }

    @Composable
    fun animateOffsets(
        width: Float,
        height: Float,
        gradientWidth: Float = calcGradientWidth(height),
    ): Pair<Float, Pair<Float, Float>> {
        val transition = rememberInfiniteTransition()
        val xOffset by transition.animateFloat(
            initialValue = 0f, //- gradientWidth,
            targetValue = width + gradientWidth,
            animationSpec = infiniteRepeatable(
                animation = tween(1300, delayMillis = 300, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        val yOffset by transition.animateFloat(
            initialValue = 0f, // - gradientWidth,
            targetValue = height + gradientWidth,
            animationSpec = infiniteRepeatable(
                animation = tween(1300, delayMillis = 300, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        return gradientWidth to Pair(xOffset, yOffset)
    }
}

@Composable
fun ShimmerLoading(
    colors: List<Color> = listOf(
        Color.LightGray.copy(alpha = 0.9f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.9f),
    )
) {

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val (width, height) = with(LocalDensity.current) {
            Pair(maxWidth.toPx(), maxHeight.toPx())
        }

        val (gradientWidth, offsets) = ShimmerAnimationDefinitions.animateOffsets(
            width = width,
            height = height,
        )

        val (xOffset, yOffset) = offsets

        val brush = Brush.linearGradient(
            colors,
            start = Offset(xOffset - gradientWidth, yOffset - gradientWidth),
            end = Offset(xOffset, yOffset)
        )

        Surface(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = brush)
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedShimmerLoading(visible: Boolean = true) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        ShimmerLoading()
    }
}