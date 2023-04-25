package server

import data.ClientRequest
import data.ServerAnswer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

object Server {
    private val s = File.separator
    private val json = HashMap<String, JsonElement>()

    init {
        println("Server started!")
        val dir =
            System.getProperty("user.dir")  + s + "src" + s + "main" + s + "kotlin" + s + "server" + s + "data" + s +  "db.json"
        System.setProperty("db", dir)
        if (File(System.getProperty("db")).exists()) {
            val fromDB = Json.decodeFromString<HashMap<String, JsonElement>>(File(System.getProperty("db")).readText())
            json.putAll(fromDB)
        }
    }

    private const val address = "127.0.0.1"
    private const val port = 23456
    private val server = ServerSocket(port, 50, InetAddress.getByName(address))
    private val lock: ReadWriteLock = ReentrantReadWriteLock()
    private val readLock: Lock = lock.readLock()
    private val writeLock: Lock = lock.writeLock()
    private val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    fun requestFromClient() {
        while (true) {
            val socket = server.accept()
            executor.submit {
                query(socket)
            }
        }
    }

    private fun setValueJson(
        json: MutableMap<String, JsonElement>,
        value: JsonElement,
        path: List<String>
    ): JsonObject {
        if (path.size == 1) {
            json[path[0]] = value
        } else {
            json.putIfAbsent(path[0], Json.encodeToJsonElement(""))
            if (json[path[0]] !is JsonObject) {
                json[path[0]] = JsonObject(mutableMapOf(path[1] to Json.encodeToJsonElement("")))
            }
            json[path[0]] = setValueJson(json[path[0]]!!.jsonObject.toMutableMap(), value, path.drop(1))
        }
        return JsonObject(json)
    }

    private fun setElement(output: DataOutputStream, query: ClientRequest) {
        writeLock.lock()
        if (query.key != null && query.value != null) {
            val element = Json.encodeToJsonElement(query.key)
            val keys = (if (element is JsonPrimitive) JsonArray(listOf(element)) else element).jsonArray
            println(keys)
            if (keys.size == 1) {
                json[Json.decodeFromJsonElement(keys[0])] = query.value
            } else {
                val jsonFromDB = json[Json.decodeFromJsonElement(keys[0])]
                    ?: JsonObject(mapOf(Json.decodeFromJsonElement<String>(keys[1]) to Json.encodeToJsonElement("")))
                val changeJSON = JsonObject(jsonFromDB.jsonObject.toMutableMap().apply {
                    setValueJson(this, query.value, Json.decodeFromJsonElement<List<String>>(keys).drop(1))
                })
                json[Json.decodeFromJsonElement(keys[0])] = changeJSON
            }
            output.writeUTF(Json.encodeToString(ServerAnswer("OK")))
            println(json)
        } else {
            output.writeUTF(Json.encodeToString(ServerAnswer("ERROR", reason = "incorrect input")))
        }
        writeLock.unlock()
    }

    private fun detectObject(query: ClientRequest): JsonElement? {
        val keys = (if (query.key is JsonPrimitive) JsonArray(listOf(query.key)) else query.key)!!.jsonArray
        var element = json[Json.decodeFromJsonElement(keys[0])]
        for (index in 1 until keys.size) {
            if (element != null) {
                element = element.jsonObject[Json.decodeFromJsonElement(keys[index])]
            } else break
        }
        return element
    }

    private fun getElement(output: DataOutputStream, query: ClientRequest) {
        readLock.lock()
        if (query.key != null) {
            val element = detectObject(query)
            if (element != null) output.writeUTF(Json.encodeToString(ServerAnswer("OK", element)))
            else output.writeUTF(Json.encodeToString(ServerAnswer("ERROR", reason = "No such key")))
        } else {
            output.writeUTF(Json.encodeToString(ServerAnswer("ERROR", reason = "No such key")))
        }
        readLock.unlock()
    }

    private fun deleteValueJson(
        json: MutableMap<String, JsonElement>,
        path: List<String>
    ): JsonObject {
        if (path.size == 1) {
            json.remove(path[0])
        } else {
            json[path[0]] = deleteValueJson(json[path[0]]!!.jsonObject.toMutableMap(), path.drop(1))
        }
        return JsonObject(json)
    }

    private fun deleteElement(output: DataOutputStream, query: ClientRequest) {
        writeLock.lock()
        if (query.key != null && detectObject(query) != null) {
            val keys = (if (query.key is JsonPrimitive) JsonArray(listOf(query.key)) else query.key).jsonArray
            val jsonFromDB = json[Json.decodeFromJsonElement(keys[0])]!!
            val changeJSON = JsonObject(jsonFromDB.jsonObject.toMutableMap().apply {
                deleteValueJson(this, Json.decodeFromJsonElement<List<String>>(keys).drop(1))
            })
            json[Json.decodeFromJsonElement(keys[0])] = changeJSON
            output.writeUTF(Json.encodeToString(ServerAnswer("OK")))
            println(json)
        } else {
            output.writeUTF(Json.encodeToString(ServerAnswer("ERROR", reason = "No such key")))
        }
        writeLock.unlock()
    }

    private fun query(socket: Socket) {
        val input = DataInputStream(socket.getInputStream())
        val output = DataOutputStream(socket.getOutputStream())
        val query = Json.decodeFromString<ClientRequest>(input.readUTF())
        when (query.type) {
            "set" -> setElement(output, query)
            "get" -> getElement(output, query)
            "delete" -> deleteElement(output, query)

            "exit" -> {
                output.writeUTF(Json.encodeToString(ServerAnswer("OK")))
                val myJson = Json {prettyPrint = true}
                if (json.isNotEmpty()) File(System.getProperty("db")).writeText(myJson.encodeToString(json))
                server.close()
            }
        }
        socket.close()
    }
}