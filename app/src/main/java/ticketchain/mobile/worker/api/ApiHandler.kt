package ticketchain.mobile.worker.api

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.network.sockets.*
import ticketchain.mobile.worker.config.Config
import ticketchain.mobile.worker.errors.TicketChainApiAuthorizationException
import ticketchain.mobile.worker.errors.TicketChainApiException
import ticketchain.mobile.worker.state.AppState
import java.text.DateFormat
import java.util.concurrent.TimeoutException

abstract class ApiHandler(open val store: AppState) {

    companion object {
        private val APIConfig = Config.load()
        val PROTOCOL = APIConfig.protocol
        val HOST = APIConfig.host
        val PORT = APIConfig.port
        val SECURE_PORT = APIConfig.secure_port
        val TIMEOUT = APIConfig.timeout
    }

    protected val api = HttpClient(Android) {
        install(JsonFeature) {
            serializer = JacksonSerializer {
                enable(SerializationFeature.INDENT_OUTPUT)
                dateFormat = DateFormat.getDateInstance()
                acceptContentTypes = listOf(ContentType.Application.Json)
            }
        }

        install(DefaultRequest) {
            contentType(ContentType.Application.Json)
            url.host = HOST
            url.protocol = if (PROTOCOL == "https") URLProtocol.HTTPS else URLProtocol.HTTP
            url.port = if (PROTOCOL == "https") SECURE_PORT else PORT
        }

        install(HttpTimeout) {
            requestTimeoutMillis = TIMEOUT
        }
    }

    protected suspend inline fun <reified T> get(path: String): T =
        try {
            api.get(path = path) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend inline fun <reified T> get(path: String, parameters: Map<String, Any>): T =
        try {
            api.get(path = path) {
                addJWTTokenToRequest(headers)
                parameters.forEach { (key, value) -> parameter(key, value) }
            }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend inline fun <reified T> get(path: String, id: String): T =
        try {
            api.get(path = path.replace("id", id)) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend inline fun <reified T> post(path: String): T =
        try {
            api.post(path = path) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend inline fun <reified T> post(path: String, data: Any): T =
        try {
            api.post(path = path, body = data) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend inline fun <reified T> post(path: String, id: String): T =
        try {
            api.post(path = path.replace("id", id)) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend inline fun <reified T> post(path: String, id: String, data: Any): T =
        try {
            api.post(path = path.replace("id", id), body = data) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend inline fun <reified T> put(path: String): T =
        try {
            api.put(path = path) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend inline fun <reified T> put(path: String, data: Any): T =
        try {
            api.put(path = path, body = data) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend inline fun <reified T> put(path: String, id: String): T =
        try {
            api.put(path = path.replace("id", id)) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend inline fun <reified T> delete(path: String, id: String): T =
        try {
            api.delete(path = path.replace("id", id)) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend inline fun <reified T> delete(path: String, data: Any): T =
        try {
            api.delete(path = path, body = data) { addJWTTokenToRequest(headers) }
        } catch (e: Exception) {
            handleApiException(e) as T
        }

    protected suspend fun handleApiException(e: Exception) {
        val err = when (e) {
            is HttpRequestTimeoutException,
            is SocketTimeoutException,
            is java.net.SocketTimeoutException,
            is TimeoutException,
            is ConnectTimeoutException -> Response.ErrorResponse("Can't reach the server")
            is ResponseException -> {
                if (e.response.status == HttpStatusCode.Unauthorized) {
                    throw TicketChainApiAuthorizationException("You need to login")
                }
                Response.ErrorResponse(e.response.receive())
            }
            else -> {
                e.printStackTrace()
                Response.ErrorResponse(e.message ?: "Unknown error")
            }
        }

        throw TicketChainApiException(err.message)
    }

    protected fun addJWTTokenToRequest(headers: HeadersBuilder) {
        //if (store.token.isNotBlank()) {
        //    headers["Authorization"] = "Bearer ${store.token}"
        //}
    }
}