package ticketchain.mobile.worker.utils

object QRCodeUtils {
    fun generateQRCodeUrl(data: String): String {
        return "https://chart.googleapis.com/chart?cht=qr&chs=300x300&chl=$data"
    }
}