package ticketchain.mobile.worker.data

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale

enum class IssueType {
    LESS_AVAILABLE_COUNTERS,
    ELECTRONIC_MALFUNCTION,
    OTHER;

    override fun toString(): String {
        return name.lowercase().replace("_", " ").capitalize(Locale.current)
    }

    companion object {
        fun fromValue(value: Int): IssueType {
            return values().getOrNull(value) ?: OTHER
        }
    }
}

data class Issue(val id: Int, val type: IssueType, val hour: Int, val minute: Int, val body: String? = null) {
    fun reportedString(): String {
        return "Reported at $hour:${if (minute < 10) "0$minute" else minute}"
    }
}