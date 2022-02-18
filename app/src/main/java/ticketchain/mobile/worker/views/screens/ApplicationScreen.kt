package ticketchain.mobile.worker.views.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ticketchain.mobile.worker.controllers.SnackbarController
import ticketchain.mobile.worker.services.AccountService
import ticketchain.mobile.worker.state.AppState

@ExperimentalAnimationApi
interface ApplicationScreen {

    val route: String
    val hasDrawer: Boolean
        get() = false

    @Composable
    fun Header(scaffoldState: ScaffoldState)

    @Composable
    fun Bottom(navController: NavHostController, scaffoldState: ScaffoldState) {}

    @Composable
    fun drawer(
        navController: NavHostController,
        scaffoldState: ScaffoldState,
        accountService: AccountService,
        state: AppState
    ): (@Composable ColumnScope.() -> Unit)? = null

    @Composable
    fun ScreenTitle(
        title: String,
        icon: ImageVector,
        verticalPadding: Dp = 20.dp,
        rightSide: (@Composable () -> Unit)? = null
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = verticalPadding)
                .padding(horizontal = 25.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "$title-screen",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(32.dp)
                )
                Text(
                    text = title,
                    fontSize = 22.sp
                )
            }
            rightSide?.let {
                rightSide()
            }
        }
    }

    @Composable
    fun Body(
        navController: NavController,
        snackbarController: SnackbarController,
        accountService: AccountService,
        state: AppState
    )
}