package ticketchain.mobile.worker.services

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import ticketchain.mobile.worker.api.TicketChainApi
import ticketchain.mobile.worker.data.*
import ticketchain.mobile.worker.state.AppState
import ticketchain.mobile.worker.storage.UserDataService
import ticketchain.mobile.worker.views.screens.DashboardScreen
import java.time.LocalTime

class AccountService(
    val api: TicketChainApi,
    private val userDataService: UserDataService,
    private val state: AppState
) {

    lateinit var navController: NavController

    /*
     * API STUFF
     */

    suspend fun countTickets() {
        val response = api.countTickets()
        state.countTickets = response.ticketCount
    }

    suspend fun getCurrentTicket() {
        val response = api.getCurrentTicket()
        state.currentTicket = response.currentTicket
    }

    suspend fun useTicket(owner: String) {
        val response = api.useTicket(owner)
    }

    suspend fun getIssues() {
        val response = api.getIssues()
        state.issues.clear()
        state.issues.addAll(response.issues.map {
            val hour = it.Date.split(":").first().toInt()
            val minute = it.Date.split(":").last().toInt()
            Issue(
                id = it.Id,
                type = IssueType.fromValue(it.Type),
                hour = hour,
                minute = minute,
                body = it.Description
            )
        })
    }

    /*
     * ACCOUNT STUFF
     */

    suspend fun loadData() {
        val userData = userDataService.getUserData()
        state.name = userData.name
        state.theme = Theme.fromValue(userData.theme)
        state.checkedIn = userData.checkIn
        state.counter = userData.counter

        state.loaded = true
    }

    fun isFirstSetup(): Boolean {
        return state.loaded && state.name.isNullOrBlank()
    }

    suspend fun changeTheme(theme: Theme) {
        state.theme = theme

        userDataService.storeUserData(
            theme = state.theme.ordinal,
        )
    }

    suspend fun changeCounter(counter: Int) {
        state.counter = counter

        userDataService.storeUserData(
            counter = state.counter
        )
    }

    suspend fun createIssue(issueType: IssueType, description: String?) {
        val time = LocalTime.now()

        val response =
            api.createIssue(issueType.ordinal, description, "${time.hour}:${time.minute}")
        val hour = response.issue.Date.split(":").first().toInt()
        val minute = response.issue.Date.split(":").last().toInt()
        state.issues.add(
            Issue(
                id = response.issue.Id,
                type = IssueType.fromValue(response.issue.Type),
                hour = hour,
                minute = minute,
                body = response.issue.Description
            )
        )

        navController.popBackStack()
    }

    suspend fun deleteIssue(index: Int) {
        val issue = state.issues[index]
        state.issues.removeAt(index)

        api.deleteIssue(issue.id)
    }

    suspend fun login() {
        state.checkedIn = true

        userDataService.storeUserData(
            checkedIn = state.checkedIn
        )
    }

    suspend fun logout() {
        state.checkedIn = false

        userDataService.storeUserData(
            checkedIn = state.checkedIn
        )
    }

    @ExperimentalAnimationApi
    suspend fun setup(name: String, age: Int, counter: Int) {
        state.name = name
        state.checkedIn = false
        state.counter = counter

        userDataService.storeUserData(
            name = state.name,
            age = age,
            counter = state.counter,
            theme = state.theme.ordinal,
            checkedIn = state.checkedIn
        )

        navController.navigate(DashboardScreen.route)
    }
}