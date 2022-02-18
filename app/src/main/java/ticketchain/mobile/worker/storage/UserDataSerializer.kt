package ticketchain.mobile.worker.storage


import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import ticketchain.mobile.worker.storage.proto.UserData
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object UserDataSerializer : Serializer<UserData> {
    override val defaultValue: UserData = UserData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserData {
        return try {
            UserData.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserData, output: OutputStream) {
        t.writeTo(output)
    }
}