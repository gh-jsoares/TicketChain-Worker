package ticketchain.mobile.worker.views.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ticketchain.mobile.worker.controllers.SnackbarController
import ticketchain.mobile.worker.data.IssueType
import ticketchain.mobile.worker.services.AccountService
import ticketchain.mobile.worker.state.AppState
import ticketchain.mobile.worker.ui.theme.TransparentBlack
import ticketchain.mobile.worker.views.partials.RadioButtonRow
import ticketchain.mobile.worker.views.partials.TicketButton
import ticketchain.mobile.worker.views.screens.drawers.DashboardDrawer
import ticketchain.mobile.worker.views.screens.headers.DashboardHeader

@ExperimentalAnimationApi
object NewIssueScreen : ApplicationScreen {
    override val route = "issues.new"
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
    private fun SectionTitle(title: String, required: Boolean) {
        Text(
            text = (if (required) "*$title" else title).uppercase(),
            color = if (required) MaterialTheme.colors.onPrimary else Color.Unspecified,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
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

        var issueType by remember { mutableStateOf(IssueType.LESS_AVAILABLE_COUNTERS) }
        var description by remember { mutableStateOf<String?>(null) }
        var enabled: Boolean by remember { mutableStateOf(true) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState!!)
        ) {

            ScreenTitle(
                title = "Report Issue",
                icon = Icons.Default.BugReport,
                verticalPadding = 15.dp
            )

            Column(
                modifier = Modifier
                    .background(color = TransparentBlack.copy(alpha = 0.1f))
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 10.dp)
            ) {
                SectionTitle(title = "Type", required = true)

                IssueType.values().forEach { type ->
                    RadioButtonRow(
                        selected = issueType == type,
                        onClick = {
                            issueType = type
                        }, label = "$type"
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 10.dp)
            ) {
                SectionTitle(title = "Description", required = false)

                OutlinedTextField(value = description ?: "", onValueChange = {
                    description = it
                }, label = { Text("Body") },
                modifier = Modifier.fillMaxWidth().height(150.dp))
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .width(260.dp)
            ) {
                TicketButton(
                    enabled = enabled,
                    text = "Create".uppercase(),
                    onClick = {
                        enabled = false
                        coroutineScope.launch {
                            try {
                                accountService.createIssue(
                                    issueType = issueType,
                                    description = description
                                )
                            } catch (e: Exception) {
                                enabled = true
                                snackbarController.showDismissibleSnackbar(
                                    e.message ?: "An error occurred"
                                )
                            }
                        }
                    },
                    color = MaterialTheme.colors.primary,
                    icon = Icons.Default.BugReport
                )
            }
        }
    }
}