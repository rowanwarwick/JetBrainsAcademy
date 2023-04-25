package data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ClientRequest(val type: String?, val key: JsonElement? = null, val value: JsonElement? = null)

@Serializable
data class ServerAnswer(val response: String, val value: JsonElement? = null, val reason: String = "")