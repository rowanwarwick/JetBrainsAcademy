package jsondatabase.client

import jsondatabase.ClientRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.Socket
import java.net.SocketException

object Client {
    private const val address = "127.0.0.1"
    private const val port = 23456
    private val socket = Socket(InetAddress.getByName(address), port)
    private val input = DataInputStream(socket.getInputStream())
    private val output = DataOutputStream(socket.getOutputStream())

    fun requestClient(inputUser: Array<String>) {
        val command = inputUser.getOrNull(1)
        val key = inputUser.getOrNull(3)
        val message = inputUser.drop(5).joinToString(" ")
        val jsonRequest = Json.encodeToString(ClientRequest(command, key, message))
        println("Client started!")
        output.writeUTF(jsonRequest)
        println("Sent: $jsonRequest")
        val answer = input.readUTF()
        println("Received: $answer")
    }

}

fun main(args: Array<String>) {
    try {
        Client.requestClient(args)
    } catch (_: SocketException) {
        println("server/socket off")
    }
}