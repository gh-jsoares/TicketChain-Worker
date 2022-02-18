package ticketchain.mobile.worker.config

import com.sksamuel.hoplite.ConfigLoader

data class Config(
    val protocol: String,
    val host: String,
    val port: Int,
    val secure_port: Int,
    val timeout: Long
) {
    companion object {
        fun load() = ConfigLoader().loadConfigOrThrow<Config>("/api_config.json")
    }
}