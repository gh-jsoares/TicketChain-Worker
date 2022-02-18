package ticketchain.mobile.worker.storage

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import ticketchain.mobile.worker.data.Theme
import ticketchain.mobile.worker.storage.proto.UserData
import java.io.IOException

class UserDataService(
    private val dataStore: DataStore<UserData>
) {
    private val userDataFlow: Flow<UserData> = dataStore.data.catch { e ->
        if (e is IOException) {
            Log.e("UserDataService", "Error reading sort order preferences.", e)
            emit(UserData.getDefaultInstance())
        } else {
            throw e
        }
    }

    suspend fun getUserData(): UserData =
        userDataFlow.firstOrNull() ?: UserData.getDefaultInstance()

    suspend fun storeUserData(
        name: String? = null,
        age: Int? = null,
        counter: Int? = null,
        checkedIn: Boolean? = null,
        theme: Int? = null,
    ) {
        dataStore.updateData {
            val userDataBuilder = it.toBuilder()

            if (name != null) {
                userDataBuilder.name = name
            }

            if (age != null) {
                userDataBuilder.age = age
            }

            if (counter != null) {
                userDataBuilder.counter = counter
            }

            if (checkedIn != null) {
                userDataBuilder.checkIn = checkedIn
            }

            if (theme != null) {
                userDataBuilder.theme = theme
            }

            userDataBuilder.build()
        }
    }
}

val Context.userDataStore: DataStore<UserData> by dataStore(
    fileName = "user_data.pb",
    serializer = UserDataSerializer
)