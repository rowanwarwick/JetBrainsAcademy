package client

import data.ClientRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.net.InetAddress
import java.net.Socket

object Client {
    private val s = File.separator

    init {
        println("Client started!")
        val dir = System.getProperty("user.dir") + s  + "src" + s + "main" + s + "kotlin" + s + "client" + s + "data" + s
        System.setProperty("fileUser", dir)
    }

    private const val address = "127.0.0.1"
    private const val port = 23456
    private val socket = Socket(InetAddress.getByName(address), port)
    private val input = DataInputStream(socket.getInputStream())
    private val output = DataOutputStream(socket.getOutputStream())

    fun requestClient(inputUser: Array<String>) {
        var jsonRequest: String? = null
        if (inputUser[0] == "-in") {
            if (inputUser.getOrNull(1) != null && File(System.getProperty("fileUser") + inputUser[1]).exists()) {
                jsonRequest = File(System.getProperty("fileUser") + inputUser[1]).readText()
            }
        } else {
            val command = inputUser.getOrNull(1)
            val key = Json.encodeToJsonElement(inputUser.getOrNull(3))
            val message = Json.encodeToJsonElement(inputUser.drop(5).joinToString(" "))
            jsonRequest = Json.encodeToString(ClientRequest(command, key, message))
        }
        if (jsonRequest != null) {
            output.writeUTF(jsonRequest)
            println("Sent: $jsonRequest")
            val answer = input.readUTF()
            println("Received: $answer")
        } else {
            println("invalid input")
        }
    }
}