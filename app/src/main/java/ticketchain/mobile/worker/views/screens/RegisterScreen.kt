package ticketchain.mobile.worker.views.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ticketchain.mobile.worker.R
import ticketchain.mobile.worker.controllers.SnackbarController
import ticketchain.mobile.worker.services.AccountService
import ticketchain.mobile.worker.state.AppState
import ticketchain.mobile.worker.views.partials.TicketButton
import ticketchain.mobile.worker.views.partials.TicketDropdownMenu
import ticketchain.mobile.worker.views.screens.headers.LogoSmallHeader


@ExperimentalAnimationApi
object RegisterScreen : ApplicationScreen {

    override val route = "register"

    @Composable
    override fun Header(scaffoldState: ScaffoldState) {
        LogoSmallHeader()
    }

    @Composable
    override fun Body(
        navController: NavController,
        snackbarController: SnackbarController,
        accountService: AccountService,
        state: AppState
    ) {
        val coroutineScope = rememberCoroutineScope()

        var name: String? by remember { mutableStateOf(null) }
        var age: Int? by remember { mutableStateOf(null) }
        var counter: Int? by remember { mutableStateOf(null) }
        var enabled: Boolean by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp, vertical = 25.dp)
        ) {
            Text(
                text = stringResource(R.string.register_title).uppercase(),
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                modifier = Modifier
                    .offset(x = (-20).dp)
            )

            // Input column
            Column(
                modifier = Modifier.padding(vertical = 25.dp)
            ) {
                OutlinedTextField(
                    label = {
                        Text(text = "* ${stringResource(R.string.register_label_name)}")
                    },
                    value = name ?: "",
                    onValueChange = {
                        name = it
                    },
                    modifier = Modifier.padding(vertical = 15.dp)
                )
                OutlinedTextField(
                    label = {
                        Text(text = "* ${stringResource(R.string.register_label_age)}")
                    },
                    value = if (age != null) "$age" else "",
                    onValueChange = {
                        val tempInt = it.toIntOrNull() ?: -1
                        age = if (tempInt >= 0) tempInt else null
                    },
                    modifier = Modifier.padding(vertical = 15.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                TicketDropdownMenu(
                    label = "* Counter",
                    onValueChange = { index, _ ->
                        counter = index + 1
                    },
                    items = (1..5).map { it.toString() },
                    modifier = Modifier.padding(vertical = 15.dp)
                )
            }

            enabled = !name.isNullOrBlank() && age != null && counter != null

            TicketButton(
                text = stringResource(R.string.register_button_next),
                onClick = {
                    enabled = false
                    coroutineScope.launch {
                        try {
                            accountService.setup(name!!, age!!, counter!!)
                        } catch (e: Exception) {
                            enabled = true
                            snackbarController.showDismissibleSnackbar(
                                e.message ?: "An error occurred"
                            )
                        }
                    }
                },
                modifier = Modifier.padding(vertical = 15.dp),
                enabled = enabled,
                icon = Icons.Default.CheckCircleOutline
            )
        }
    }
}
