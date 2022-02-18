package ticketchain.mobile.worker.views.partials

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ticketchain.mobile.worker.R

@Composable
fun TicketChainLogo(modifier: Modifier = Modifier, full: Boolean = true, size: Dp = 100.dp) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier.size(size),
            tint = Color.Unspecified
        )

        if (full) {
            Text(
                text = stringResource(id = R.string.app_name_first),
                fontWeight = FontWeight.Medium,
                fontSize = 26.sp,
                color = MaterialTheme.colors.onSecondary,
            )
            Text(
                text = stringResource(id = R.string.app_name_last),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.offset(y = (-10).dp)
            )
            Text(
                text = "WORKER",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.offset(y = (-10).dp)
            )
        }
    }
}