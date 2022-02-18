package ticketchain.mobile.worker.views.screens

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import ticketchain.mobile.worker.controllers.SnackbarController
import ticketchain.mobile.worker.errors.TicketChainApiException
import ticketchain.mobile.worker.services.AccountService
import ticketchain.mobile.worker.state.AppState
import ticketchain.mobile.worker.views.partials.LoadingProgressIndicator


@ExperimentalAnimationApi
object LoadingScreen : ApplicationScreen {

    override val route = "loading"

    @Composable
    override fun Header(scaffoldState: ScaffoldState) {
        // Empty
    }

    @SuppressLint("RestrictedApi")
    @Composable
    override fun Body(
        navController: NavController,
        snackbarController: SnackbarController,
        accountService: AccountService,
        state: AppState,
    ) {
        val (loading, setLoading) = remember { mutableStateOf(true) }
        var attempt by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (loading) {
                LaunchedEffect(loading) {
                    try {
                        accountService.loadData()

                        while (!state.loaded) {
                            attempt++
                            delay(1000)
                            if (attempt >= 3) {
                                throw TicketChainApiException("Cannot load data")
                            }
                        }

                        navController.backQueue.clear()
                        if (accountService.isFirstSetup()) {
                            navController.navigate(InitialScreen.route)
                        } else {
                            navController.navigate(DashboardScreen.route)
                        }
                    } catch (e: TicketChainApiException) {
                        snackbarController.showDismissibleSnackbar(e.message)
                        setLoading(false)
                    }
                }
                LoadingProgressIndicator(
                    progressColor = MaterialTheme.colors.primary,
                    backgroundColor = MaterialTheme.colors.surface
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Loading"
                )
            } else {
                Button(onClick = { setLoading(true) }) {
                    Text("Try again")
                }
            }
        }
    }
}
