package ticketchain.mobile.worker.data

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale

enum class Theme {
    DARK,
    LIGHT;

    override fun toString(): String {
        return name.lowercase().replace("_", " ").capitalize(Locale.current)
    }

    companion object {
        fun fromValue(value: Int): Theme {
            return values().getOrNull(value) ?: DARK
        }
    }
}