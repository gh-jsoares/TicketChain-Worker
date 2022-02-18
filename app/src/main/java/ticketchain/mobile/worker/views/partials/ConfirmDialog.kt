package ticketchain.mobile.worker.views.partials

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmDialog(
    text: String,
    onClose: () -> Unit,
    onSave: () -> Unit,
    cancelText: String = "Close",
    cancelIcon: ImageVector = Icons.Default.Cancel,
    confirmText: String = "Confirm",
    confirmIcon: ImageVector = Icons.Default.Save,
) {
    var enabled by remember { mutableStateOf(true) }
    val close = {
        onClose()
    }

    AlertDialog(
        onDismissRequest = {
            close()
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.Default.Dangerous,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(
                        text = text,
                        modifier = Modifier.padding(start = 12.dp),
                        fontSize = 18.sp
                    )
                }

                Divider()

            }
        },
        dismissButton = {
            TicketButton(
                text = cancelText,
                color = MaterialTheme.colors.secondary,
                small = true,
                icon = cancelIcon,
                width = 140.dp,
                onClick = { close() },
                modifier = Modifier.padding(end = 5.dp)
            )
        },
        confirmButton = {
            TicketButton(
                text = confirmText,
                color = MaterialTheme.colors.primary,
                small = true,
                icon = confirmIcon,
                enabled = enabled,
                width = 140.dp,
                onClick = {
                    enabled = false
                    onSave()
                },
                modifier = Modifier.padding(start = 5.dp)
            )
        },
    )
}
