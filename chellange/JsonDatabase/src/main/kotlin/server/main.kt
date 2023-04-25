package server

import java.net.ConnectException
import java.net.SocketException

fun main() {
    try {
        Server.requestFromClient()
    } catch (_: ConnectException) {
        println("server/socket off")
    } catch (_: SocketException) {
        println("server/socket off")
    }
}