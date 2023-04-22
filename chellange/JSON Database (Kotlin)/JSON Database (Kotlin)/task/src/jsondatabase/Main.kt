package jsondatabase

import kotlinx.serialization.EncodeDefault // нужно, если будешь печатать значения по умолчанию
import kotlinx.serialization.Serializable
@Serializable
data class ClientRequest(val type: String?, val key: String?, val value: String = "")

@Serializable
data class ServerAnswer(val response: String, val value: String = "", val reason: String = "")

fun main() {
}