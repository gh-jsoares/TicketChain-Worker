package ticketchain.mobile.worker.views.screens.drawers

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.annotation.ExperimentalCoilApi
import kotlinx.coroutines.launch
import ticketchain.mobile.worker.R
import ticketchain.mobile.worker.services.AccountService
import ticketchain.mobile.worker.state.AppState
import ticketchain.mobile.worker.ui.theme.TransparentBlack
import ticketchain.mobile.worker.views.partials.TicketButton
import ticketchain.mobile.worker.views.partials.TicketChainLogo
import ticketchain.mobile.worker.views.screens.*

data class MenuItem(
    val displayName: String,
    val destination: String? = null,
    val visible: Boolean? = true,
    val action: suspend () -> Unit = {}
)

@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
fun DashboardDrawer(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    accountService: AccountService,
    state: AppState
) {
    val coroutineScope = rememberCoroutineScope()

    IconButton(
        onClick = {
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        },
        modifier = Modifier
            .padding(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Close, contentDescription = "close menu",
            modifier = Modifier
                .size(30.dp)
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, bottom = 50.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TicketChainLogo(
                modifier = Modifier.padding(vertical = 30.dp)
            )

            TicketMenu(navController, scaffoldState, accountService, state)
        }
        TicketButton(
            onClick = {
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                    navController.navigate(NewIssueScreen.route)
                }
            },
            text = "REPORT ISSUE",
            icon = Icons.Default.BugReport,
            modifier = Modifier
                .widthIn(max = 200.dp)
        )
    }
}

@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
fun TicketMenu(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    accountService: AccountService,
    state: AppState
) {
    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val menuItems = listOf(
        MenuItem("Dashboard", DashboardScreen.route),
        MenuItem("Scan ticket", ScanScreen.route, visible = state.checkedIn),
        //MenuItem("Change counter", SettingsScreen.route, visible = state.checkedIn),
        MenuItem("Settings", SettingsScreen.route, visible = state.checkedIn),
        MenuItem("Sign out", action = {
            accountService.logout()
        }, visible = state.checkedIn),
    )

    //var active by remember { mutableStateOf(currentRoute) }

    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        menuItems.forEach { item ->
            val isActive = item.destination?.let { currentRoute?.startsWith(it) } ?: false
            val color =
                if (isActive)
                    MaterialTheme.colors.primary
                else
                    Color.Unspecified

            if (item.visible == true) {
                Text(
                    text = item.displayName,
                    textAlign = TextAlign.Right,
                    color = color,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                                item.action()
                                if (item.destination != null) {
                                    navController.navigate(item.destination)
                                }
                            }
                        }
                        .then(
                            if (isActive)
                                Modifier.background(color = TransparentBlack.copy(alpha = 0.2f))
                            else Modifier
                        )
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .then(
                            if (isActive)
                                Modifier
                                    .drawBehind {
                                        val width = 50f
                                        val radius = 30f
                                        drawRoundRect(
                                            color = color,
                                            cornerRadius = CornerRadius(radius),
                                            size = size.copy(width = width),
                                            topLeft = Offset(size.width - width / 2, 0f)
                                        )
                                    }
                            else Modifier
                        )
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 20.dp)
                )
            }
        }
    }
}
