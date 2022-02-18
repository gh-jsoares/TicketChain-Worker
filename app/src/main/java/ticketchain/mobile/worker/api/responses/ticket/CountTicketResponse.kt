package ticketchain.mobile.worker.api.responses.ticket

data class CountTicketResponse(
    val ticketCount: Int,
    val waitTime: Float,
)