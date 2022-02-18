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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import ticketchain.mobile.worker.views.partials.TicketChainLogo
import ticketchain.mobile.worker.views.partials.shapes.TrapezoidShape


@Composable
fun LogoLargeHeader() {
    val height = 450.dp
    val offset = 200f
    val secondaryColor = MaterialTheme.colors.secondary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .graphicsLayer {
                shadowElevation = 8.dp.toPx()
                shape = TrapezoidShape
                clip = true
            }
            .background(color = MaterialTheme.colors.onSecondary)
            .drawBehind {
                drawPath(
                    path = TrapezoidShape.generatePath(
                        size.run {
                            copy(height = this.height - offset)
                        }
                    ),
                    color = secondaryColor,
                )
            }
    ) {
        TicketChainLogo(
            modifier = Modifier
                .padding(height / 8, height / 7),
            size = 125.dp
        )
    }
}