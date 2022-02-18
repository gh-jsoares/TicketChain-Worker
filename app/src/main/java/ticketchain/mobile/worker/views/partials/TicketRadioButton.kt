package ticketchain.mobile.worker.views.partials

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TicketRadioButton(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (selected) {
        MaterialTheme.colors.primaryVariant
    } else {
        Color.Unspecified
    }

    val clickableInteractionSource = remember { MutableInteractionSource()}

    Row(
        modifier = modifier
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primaryVariant
            )
        )
        Text(
            text = label,
            modifier = Modifier
                .clickable(
                    interactionSource = clickableInteractionSource,
                    indication = null,
                    onClick = onSelect
                )
                .padding(start = 18.dp),
            color = color
        )
    }
}
