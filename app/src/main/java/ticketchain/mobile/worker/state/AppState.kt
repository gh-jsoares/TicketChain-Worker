package ticketchain.mobile.worker.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ticketchain.mobile.worker.data.Issue
import ticketchain.mobile.worker.data.Theme

class AppState {

    var loaded: Boolean by mutableStateOf(false)

    var name: String? by mutableStateOf(null)
    var counter: Int? by mutableStateOf(null)
    var theme: Theme by mutableStateOf(Theme.DARK) // Default
    var checkedIn: Boolean? by mutableStateOf(null)

    /*
     * API STUFF
     */
    var countTickets: Int? by mutableStateOf(null)
    var currentTicket: Int? by mutableStateOf(null)

    val issues = mutableStateListOf<Issue>()
}