package ticketchain.mobile.worker.api

import ticketchain.mobile.worker.api.requests.CreateIssueRequest
import ticketchain.mobile.worker.api.requests.DeleteIssueRequest
import ticketchain.mobile.worker.api.requests.TicketOwnerRequest
import ticketchain.mobile.worker.api.responses.SuccessResponse
import ticketchain.mobile.worker.api.responses.ticket.CountTicketResponse
import ticketchain.mobile.worker.api.responses.ticket.CurrentTicketResponse
import ticketchain.mobile.worker.api.responses.ticket.IssueResponse
import ticketchain.mobile.worker.api.responses.ticket.IssuesResponse
import ticketchain.mobile.worker.state.AppState

class TicketChainApi(
    store: AppState
) : ApiHandler(store) {

    suspend fun getCurrentTicket(): CurrentTicketResponse {
        return get(Routes.TICKET_CURRENT)
    }

    suspend fun countTickets(): CountTicketResponse {
        return get(Routes.TICKET_COUNT)
    }

    suspend fun useTicket(owner: String): SuccessResponse {
        val request = TicketOwnerRequest(owner)
        return post(Routes.TICKET_USE, request)
    }

    suspend fun getIssues(): IssuesResponse {
        return get(Routes.ISSUES_GET)
    }

    suspend fun createIssue(type: Int, description: String?, date: String): IssueResponse {
        val request = CreateIssueRequest(type, description, date)
        return post(Routes.ISSUES_CREATE, request)
    }

    suspend fun deleteIssue(id: Int): SuccessResponse {
        val request = DeleteIssueRequest(id)
        return post(Routes.ISSUES_DELETE, request)
    }
}

