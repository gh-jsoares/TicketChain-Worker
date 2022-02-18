package ticketchain.mobile.worker.api.requests

data class CreateIssueRequest(
    val type: Int,
    val description: String?,
    val date: String
)