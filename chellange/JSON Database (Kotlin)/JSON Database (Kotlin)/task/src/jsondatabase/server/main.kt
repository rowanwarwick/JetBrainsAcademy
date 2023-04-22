package jsondatabase.server

import jsondatabase.ClientRequest
import jsondatabase.ServerAnswer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
    init {
        println("Server started!")
        val s = File.separator
        val dir = System.getProperty("user.dir") + s + "server" + s + "data" + s
        System.setProperty("server", dir)
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
            executor.submit {
                val socket = server.accept()
                query(socket)
            }
        }
    }

    private fun query(socket: Socket) {
        do {
            val input = DataInputStream(socket.getInputStream())
            val output = DataOutputStream(socket.getOutputStream())
            val query = Json.decodeFromString<ClientRequest>(input.readUTF())
            when (query.type) {
                "set" -> {
                    writeLock.lock()
                    if (query.key != null) {
                        File(System.getProperty("server"))
                        json[query.key] = query.value.also {
                            output.writeUTF(Json.encodeToString(ServerAnswer("OK")))
                        }
                    } else {
                        output.writeUTF(Json.encodeToString(ServerAnswer("ERROR", reason = "No such key")))
                    }
                    writeLock.unlock()
                }

                "get" -> {
                    readLock.lock()
                    if (query.key != null && json.containsKey(query.key)) {
                        output.writeUTF(Json.encodeToString(ServerAnswer("OK", json[query.key] ?: "null")))
                    } else {
                        output.writeUTF(Json.encodeToString(ServerAnswer("ERROR", reason = "No such key")))
                    }
                    readLock.unlock()
                }

                "delete" -> {
                    writeLock.lock()
                    if (query.key != null && json.containsKey(query.key)) {
                        json.remove(query.key).also {
                            output.writeUTF(Json.encodeToString(ServerAnswer("OK")))
                        }
                    } else {
                        output.writeUTF(Json.encodeToString(ServerAnswer("ERROR", reason = "No such key")))
                    }
                    readLock.unlock()
                }

                "exit" -> {
                    output.writeUTF(Json.encodeToString(ServerAnswer("OK")))
                    executor.shutdown()
                    server.close()
                }
            }
            socket.close()
        } while (query.type != "exit")
    }

}

fun main() {
    Server.requestFromClient()
}