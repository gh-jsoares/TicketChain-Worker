package ticketchain.mobile.worker.views.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ticketchain.mobile.worker.controllers.SnackbarController
import ticketchain.mobile.worker.data.Theme
import ticketchain.mobile.worker.services.AccountService
import ticketchain.mobile.worker.state.AppState
import ticketchain.mobile.worker.ui.theme.TransparentBlack
import ticketchain.mobile.worker.views.partials.TicketButton
import ticketchain.mobile.worker.views.partials.TicketDropdownMenu
import ticketchain.mobile.worker.views.screens.drawers.DashboardDrawer
import ticketchain.mobile.worker.views.screens.headers.DashboardHeader
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@ExperimentalAnimationApi
object SettingsScreen : ApplicationScreen {
    override val route = "settings"
    override val hasDrawer = true
    private var scrollState: ScrollState? = null

    @Composable
    override fun drawer(
        navController: NavHostController,
        scaffoldState: ScaffoldState,
        accountService: AccountService,
        state: AppState
    ): @Composable (ColumnScope.() -> Unit) = {
        DashboardDrawer(navController, scaffoldState, accountService, state)
    }

    @Composable
    override fun Header(scaffoldState: ScaffoldState) {
        if (scrollState == null) {
            scrollState = rememberScrollState()
        }

        DashboardHeader(scaffoldState, scrollState!!.value > 0)
    }

    @Composable
    override fun Body(
        navController: NavController,
        snackbarController: SnackbarController,
        accountService: AccountService,
        state: AppState
    ) {
        if (scrollState == null) {
            scrollState = rememberScrollState()
        }

        var checked by remember { mutableStateOf(state.theme == Theme.DARK) }
        val coroutineScope = rememberCoroutineScope()

        var counter by remember { mutableStateOf(state.counter) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState!!)
        ) {
            ScreenTitle(title = "Settings", icon = Icons.Default.Settings, verticalPadding = 5.dp)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
            ) {
                Text(text = "Color Theme", modifier = Modifier.padding(bottom = 10.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (checked) Theme.DARK.name else Theme.LIGHT.name,
                        fontWeight = FontWeight.Bold,
                    )
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            coroutineScope.launch {
                                checked = !checked
                                accountService.changeTheme(if (checked) Theme.DARK else Theme.LIGHT)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = MaterialTheme.colors.onPrimary,
                            uncheckedTrackColor = MaterialTheme.colors.onSurface,
                            checkedThumbColor = MaterialTheme.colors.onPrimary, // when theme changes color also changes
                            checkedTrackColor = MaterialTheme.colors.onSurface
                        ),
                    )
                }
            }

            TicketDropdownMenu(
                label = "Counter",
                onValueChange = { index, _ ->
                    counter = index + 1
                    coroutineScope.launch {
                        accountService.changeCounter(counter!!)
                    }
                },
                defaultValue = (state.counter ?: 0) - 1,
                items = (1..5).map { it.toString() },
                modifier = Modifier.padding(vertical = 15.dp).width(260.dp)
            )
        }
    }
}