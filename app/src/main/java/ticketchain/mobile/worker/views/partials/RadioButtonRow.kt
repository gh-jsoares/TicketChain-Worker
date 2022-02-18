package ticketchain.mobile.worker.views.partials

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RadioButtonRow(selected: Boolean, onClick: () -> Unit, label: String) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        RadioButton(
            selected = selected,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
            ),
            interactionSource = interactionSource,
            onClick = onClick
        )
        Text(
            text = label,
            color = if (selected) MaterialTheme.colors.primary else Color.Unspecified
        )
    }
}
