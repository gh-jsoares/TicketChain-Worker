package ticketchain.mobile.worker.views.partials

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TicketButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    small: Boolean = false,
    icon: ImageVector? = null,
    width: Dp? = null,
    enabled: Boolean = true
) {
    val modifierComb = if (width != null) modifier.width(width) else modifier.fillMaxWidth()
    Button(
        onClick = onClick,
        colors = ButtonDefaults
            .buttonColors(backgroundColor = color),
        modifier = modifierComb,
        enabled = enabled,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            icon?.let {
                Icon(
                    imageVector = it, contentDescription = text,
                    modifier = Modifier.padding(end = 10.dp)
                )
            }

            Text(
                text = text.uppercase(),
                modifier = Modifier
                    .padding(
                        vertical =
                        if (!small) 10.dp else 5.dp
                    )
            )
        }
    }
}
