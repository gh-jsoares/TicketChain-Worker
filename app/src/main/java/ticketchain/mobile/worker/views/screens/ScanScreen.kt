package ticketchain.mobile.worker.views.screens

import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import kotlinx.coroutines.launch
import ticketchain.mobile.worker.R
import ticketchain.mobile.worker.controllers.SnackbarController
import ticketchain.mobile.worker.services.AccountService
import ticketchain.mobile.worker.services.CameraService
import ticketchain.mobile.worker.state.AppState
import ticketchain.mobile.worker.ui.theme.transparentBlue
import ticketchain.mobile.worker.utils.QRCodeUtils
import ticketchain.mobile.worker.views.partials.RequestCameraPermission
import ticketchain.mobile.worker.views.partials.SplitLayout
import ticketchain.mobile.worker.views.partials.TicketButton
import ticketchain.mobile.worker.views.partials.TicketImage
import ticketchain.mobile.worker.views.screens.drawers.DashboardDrawer
import ticketchain.mobile.worker.views.screens.headers.DashboardHeader

@ExperimentalCoilApi
@ExperimentalAnimationApi
object ScanScreen : ApplicationScreen {
    override val route = "scan"
    override val hasDrawer = true
    private var scrollState: ScrollState? = null
    private val cameraService = CameraService()

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

        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val coroutineScope = rememberCoroutineScope()

        var openScanner by remember { mutableStateOf(true) }

        val ticketNumber = state.currentTicket
        val ticket = if (ticketNumber != null) "$ticketNumber" else "-"

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState!!)
        ) {
            ScreenTitle(title = "Scan", icon = Icons.Default.QrCode)

            SplitLayout(
                leftTopText = ticket,
                leftBottomText = "Current Ticket",
                leftIcon = Icons.Default.ConfirmationNumber,
                rightIcon = Icons.Default.Storefront,
                rightTopText = "${state.counter ?: "?"}",
                rightBottomText = "My Counter",
                paddingVertical = 10.dp,
                color = transparentBlue
            )

            AnimatedVisibility(visible = openScanner) {
                RequestCameraPermission(
                    snackbarController,
                    onFail = {
                        openScanner = false
                    }
                ) {
                    AndroidView(
                        factory = {
                            LayoutInflater.from(it).inflate(R.layout.camera_layout, null)
                        },
                        modifier = Modifier
                            .padding(vertical = 60.dp)
                            .height(260.dp)
                            .width(260.dp)
                            .clip(MaterialTheme.shapes.large)
                            .border(
                                width = 6.dp,
                                color = MaterialTheme.colors.primary,
                                shape = MaterialTheme.shapes.large
                            )
                    ) { inflatedLayout ->
                        cameraService.initCamera(
                            context,
                            lifecycleOwner,
                            inflatedLayout as PreviewView,
                            onSuccess = {
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                coroutineScope.launch {
                                    try {
                                        accountService.useTicket(it.split(":").firstOrNull() ?: "NONE")
                                    } catch(e: Exception) {
                                        Log.d("SOMETHING", e.message ?: "Error Oops")
                                    }
                                    openScanner = false
                                }
                            },
                            onFail = {
                                Toast.makeText(context, "No correct code found", Toast.LENGTH_SHORT).show()
                                openScanner = false
                                //snackbarController.showDismissibleSnackbar("No correct code found")
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .width(260.dp)
            ) {
                TicketButton(
                    text = if (openScanner) "CLOSE SCANNER" else "OPEN SCANNER",
                    onClick = {
                        openScanner = !openScanner
                    },
                    color = if (openScanner) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.primary,
                    icon = if (openScanner) Icons.Default.VideocamOff else Icons.Default.Videocam,
                )
            }
        }
    }
}