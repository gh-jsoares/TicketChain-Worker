package ticketchain.mobile.worker.views.partials

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.flow.collect

@Composable
fun TicketDropdownMenu(
    items: List<String>,
    onValueChange: (Int, String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    defaultValue: Int = -1
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(defaultValue) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val clickableInteractionSource = remember { MutableInteractionSource() }

    // Since text field is readonly, clickable modifier does not work
    // This is a workaround for that
    LaunchedEffect(clickableInteractionSource) {
        clickableInteractionSource.interactions.collect {
            if (it is PressInteraction.Release) {
                // Works as onClick
                expanded = true
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = items.elementAtOrNull(selectedIndex) ?: "",
            readOnly = true,
            onValueChange = {},
            label = {
                Text(text = label)
            },
            interactionSource = clickableInteractionSource,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            trailingIcon = {
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    Icon(imageVector = Icons.Filled.ArrowDropUp, "close dropdown")
                }

                AnimatedVisibility(
                    visible = !expanded,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    Icon(imageVector = Icons.Filled.ArrowDropDown, "open dropdown")
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            items.forEachIndexed { index, lbl ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        expanded = false
                        onValueChange(index, lbl)
                    },
                ) {
                    Text(
                        text = lbl,
                    )
                }
            }
        }
    }
}
