package ticketchain.mobile.worker.views.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ticketchain.mobile.worker.controllers.SnackbarController
import ticketchain.mobile.worker.data.Issue
import ticketchain.mobile.worker.services.AccountService
import ticketchain.mobile.worker.state.AppState
import ticketchain.mobile.worker.ui.theme.TransparentBlack
import ticketchain.mobile.worker.ui.theme.transparentBlue
import ticketchain.mobile.worker.views.partials.ConfirmDialog
import ticketchain.mobile.worker.views.partials.SplitLayout
import ticketchain.mobile.worker.views.partials.TicketButton
import ticketchain.mobile.worker.views.screens.dashboard.Greeter
import ticketchain.mobile.worker.views.screens.drawers.DashboardDrawer
import ticketchain.mobile.worker.views.screens.headers.DashboardHeader

@ExperimentalAnimationApi
object DashboardScreen : ApplicationScreen {
    override val route = "dashboard"
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
    private fun IssueRow(
        issue: Issue,
        selectIssue: (Issue?) -> Unit,
        background: Color
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color = background)
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxHeight()
                    .weight(weight = 2f)
            ) {
                Text(
                    text = issue.type.toString(),
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(

                        imageVector = Icons.Default.Schedule, contentDescription = null,
                        tint = MaterialTheme.colors.secondaryVariant,
                        modifier = Modifier
                            .padding(end = 5.dp)
                    )
                    Text(
                        text = issue.reportedString(),
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colors.secondaryVariant,
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(48.dp)
                    .weight(weight = 2f, fill = false)
            ) {
                IconButton(
                    onClick = {
                        selectIssue(issue)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete, contentDescription = null,
                        tint = MaterialTheme.colors.secondaryVariant,
                        modifier = Modifier
                            .size(28.dp)
                    )
                }
            }
        }
    }

    @ExperimentalCoilApi
    @Composable
    private fun LoggedInView(
        navController: NavController,
        snackbarController: SnackbarController,
        accountService: AccountService,
        state: AppState,
        coroutineScope: CoroutineScope
    ) {
        val (selectedIssue, selectIssue) = remember { mutableStateOf(null as Issue?) }

        Spacer(modifier = Modifier.height(20.dp))
        SplitLayout(
            leftTopText = state.countTickets?.toString() ?: "-",
            leftBottomText = "Ticket Amount",
            rightTopText = "${state.counter ?: "?"}",
            rightBottomText = "My Counter",
            leftIcon = Icons.Default.ConfirmationNumber,
            rightIcon = Icons.Default.Storefront,
            color = transparentBlue,
            paddingVertical = 10.dp
        )
        Spacer(modifier = Modifier.height(20.dp))

        TicketButton(
            text = "SCAN TICKET",
            onClick = {
                navController.navigate(ScanScreen.route)
            },
            small = true,
            icon = Icons.Default.QrCode,
            width = 260.dp
        )

        Text(
            "Issues",
            modifier = Modifier
                .padding(top = 20.dp, bottom = 10.dp)
                .fillMaxWidth()
                .padding(start = 20.dp),
            textAlign = TextAlign.Left
        )

        state.issues.forEachIndexed { index, issue ->
            IssueRow(
                issue = issue,
                selectIssue = selectIssue,
                background = if (index % 2 == 0) TransparentBlack.copy(alpha = 0.1f) else Color.Unspecified
            )
        }

        TicketButton(
            text = "REPORT ISSUE",
            onClick = {
                navController.navigate(NewIssueScreen.route)
            },
            small = true,
            icon = Icons.Default.BugReport,
            width = 260.dp,
            color = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier.padding(vertical = 20.dp)
        )

        AnimatedVisibility(visible = selectedIssue != null) {
            ConfirmDialog(
                text = "Are you sure you want to delete this issue?",
                onClose = {
                    selectIssue(null)
                },
                onSave = {
                    val index = state.issues.indexOf(selectedIssue)
                    selectIssue(null)
                    coroutineScope.launch {
                        accountService.deleteIssue(index)
                    }
                })
        }
    }

    @Composable
    private fun LoggedOutView(
        navController: NavController,
        snackbarController: SnackbarController,
        accountService: AccountService,
        state: AppState,
        coroutineScope: CoroutineScope
    ) {
        var enabled by remember { mutableStateOf(true) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            Text("${state.counter ?: "?"}", fontWeight = FontWeight.Bold, fontSize = 80.sp)
            Row(modifier = Modifier.padding(top = 10.dp)) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 5.dp),
                    tint = MaterialTheme.colors.secondaryVariant
                )
                Text("My Counter", color = MaterialTheme.colors.secondaryVariant)
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.large)
                    .background(TransparentBlack.copy(alpha = 0.3f))
                    .size(260.dp)
                    .clickable(enabled = enabled) {
                        enabled = false
                        coroutineScope.launch {
                            accountService.login()
                        }
                    }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        imageVector = Icons.Default.Login,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .padding(bottom = 10.dp),
                        tint = MaterialTheme.colors.secondaryVariant
                    )
                    Text("SIGN IN", color = MaterialTheme.colors.secondaryVariant)
                }
            }
        }
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
        val coroutineScope = rememberCoroutineScope()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState!!)
        ) {
            Greeter(state)

            if (state.checkedIn == true) {
                LoggedInView(
                    navController,
                    snackbarController,
                    accountService,
                    state,
                    coroutineScope
                )
            } else {
                LoggedOutView(
                    navController,
                    snackbarController,
                    accountService,
                    state,
                    coroutineScope
                )
            }
        }
    }
}