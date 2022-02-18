package ticketchain.mobile.worker.views.partials

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import ticketchain.mobile.worker.R

@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
fun TicketImage(
    data: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val painter = rememberImagePainter(
        data = data,
        builder = {
            crossfade(true)
        }
    )

    Box(
        modifier = modifier
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        when (painter.state) {
            is ImagePainter.State.Loading -> {
                // Display a circular progress indicator whilst loading
                AnimatedShimmerLoading()
            }
            is ImagePainter.State.Error -> {
                // If you wish to display some content if the request fails
                Image(
                    painter = painterResource(id = R.drawable.default_image),
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            ImagePainter.State.Empty -> Unit
            is ImagePainter.State.Success -> Unit
        }
    }

}
