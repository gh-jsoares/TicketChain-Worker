package ticketchain.mobile.worker.views.screens.headers

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ticketchain.mobile.worker.views.partials.TicketChainLogo


@Composable
fun DashboardHeader(scaffoldState: ScaffoldState, showShadow: Boolean = false) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .shadow(if (showShadow) 2.dp else 0.dp, shape = object : Shape {
                override fun createOutline(
                    size: Size,
                    layoutDirection: LayoutDirection,
                    density: Density
                ): Outline {
                    val path = Path()
                    path.addRect(Rect(-10f, -10f, size.width + 10f, size.height))
                    return Outline.Generic(path)
                }
            })
            .padding(vertical = 10.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                coroutineScope.launch {
                    scaffoldState.drawerState.open()
                }
            },
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu, "menu",
                modifier = Modifier
                    .size(30.dp)
            )
        }
        TicketChainLogo(
            size = 30.dp,
            full = false,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )
    }
}