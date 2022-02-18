package ticketchain.mobile.worker.views.screens.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ticketchain.mobile.worker.state.AppState

@Composable
fun Greeter(state: AppState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Hello, ${state.name}!",
            fontSize = MaterialTheme.typography.h1.fontSize,
            textAlign = TextAlign.Left
        )
    }
}
