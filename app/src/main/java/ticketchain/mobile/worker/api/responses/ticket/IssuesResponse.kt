package ticketchain.mobile.worker.api.responses.ticket

data class Issue(
    val Id: Int,
    val Type: Int,
    val Description: String?,
    val Date: String
)
data class IssuesResponse(
    val issues: List<Issue>
)

data class IssueResponse(
    val issue: Issue
)
