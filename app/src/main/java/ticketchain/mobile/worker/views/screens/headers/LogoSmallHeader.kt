package ticketchain.mobile.worker.views.screens.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import ticketchain.mobile.worker.views.partials.TicketChainLogo
import ticketchain.mobile.worker.views.partials.shapes.BannerShape
import ticketchain.mobile.worker.views.partials.shapes.SmallTrapezoidShape


@Composable
fun LogoSmallHeader() {
    val height = 170.dp
    val offset = 20f
    val secondaryColor = MaterialTheme.colors.secondary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .graphicsLayer {
                shadowElevation = 8.dp.toPx()
                shape = SmallTrapezoidShape
                clip = true
            }
            .background(color = MaterialTheme.colors.onSecondary)
            .drawBehind {
                translate(top = offset) {
                    drawPath(
                        path = BannerShape.generatePath(size),
                        color = secondaryColor,
                    )
                }
            }
    )
    {
        TicketChainLogo(
            modifier = Modifier
                .padding(height / 8, height / 4),
            size = 100.dp,
            full = false
        )
    }
}