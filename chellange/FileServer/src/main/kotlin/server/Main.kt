package server

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.net.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random

object Server {
    private const val address = "127.0.0.1"
    private const val port = 23456
    private val server = ServerSocket(port, 50, InetAddress.getByName(address))
    private val decoderFileName = mutableMapOf<Int, Pair<String, String>>()
    private val executor: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    init {
        val sep = File.separator
        System.setProperty("server.data", "${System.getProperty("user.dir")}${sep}src${sep}main${sep}kotlin${sep}server${sep}data${sep}") //
        if (File(System.getProperty("server.data") + "saveData").exists()) {
            File(System.getProperty("server.data") + "saveData").forEachLine {
                val data = it.split(" ")
                decoderFileName[data[0].toInt()] = Pair(data[1], data[2])
            }
        }
    }

    private fun generate(): String {
        val size = Random.Default.nextInt(1, 20)
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..size).map { allowedChars.random() }.joinToString("")
    }

    private fun put(input: DataInputStream, output: DataOutputStream, fromUser: List<String>) {
        val nameGenerate = generate()
        val fileNameOnServer = System.getProperty("server.data") + fromUser.getOrElse(3) { nameGenerate }
        if (!File(fileNameOnServer).exists()) {
            var id = 0
            val length = input.readInt()
            val message = ByteArray(length)
            input.readFully(message, 0, message.size)
            File(fileNameOnServer + fromUser.getOrElse(2) { "" }).writeBytes(message)
            while (decoderFileName.containsKey(id)) {
                id++
            }
            decoderFileName[id] =
                Pair(fromUser[1], fromUser.getOrElse(3) { nameGenerate } + fromUser.getOrElse(2) { "" })
            output.writeUTF("200 $id")
        } else {
            output.writeUTF("403")
        }
    }

    private fun get(output: DataOutputStream, fromUser: List<String>) {
        val fileName = if (fromUser[1].first() == '1') {
            fromUser[1].substring(1)
        } else {
            decoderFileName[fromUser[1].substring(1).toInt()]?.second
        }
        if (fileName != null && File(System.getProperty("server.data") + fileName).exists()) {
            val format = "[.].+".toRegex().findAll(fileName).last().value
            val message = File(System.getProperty("server.data") + fileName).readBytes()
            output.apply {
                writeUTF("200 $format")
                writeInt(message.size)
                write(message)
            }
        } else {
            output.writeUTF("404")
        }
    }

    private fun delete(output: DataOutputStream, fromUser: List<String>) {
        val fileName = if (fromUser[1].first() == '1') {
            fromUser[1].substring(1)
        } else {
            decoderFileName[fromUser[1].substring(1).toInt()]?.second
        }
        if (fileName != null && File(System.getProperty("server.data") + fileName).exists()) {
            File(System.getProperty("server.data") + fileName).delete()
            output.writeUTF("200")
        } else {
            output.writeUTF("404")
        }
    }

    @Synchronized
    private fun operation(socket: Socket) {
        val input = DataInputStream(socket.getInputStream())
        val output = DataOutputStream(socket.getOutputStream())
        val fromUser = input.readUTF().trimEnd().split(" ")
        when (fromUser[0]) {
            "PUT" -> put(input, output, fromUser)
            "GET" -> get(output, fromUser)
            "DELETE" -> delete(output, fromUser)
            "EXIT" -> {
                executor.shutdown()
                File(System.getProperty("server.data") + "saveData").writeText(decoderFileName.map { "${it.key} ${it.value.first} ${it.value.second}" }
                    .joinToString("\n"))
                server.close()
            }
        }
        socket.close()
    }

    fun workServer() {
        while (true) {
            val socket = server.accept()
            executor.submit { operation(socket) }
        }
    }
}

fun main() {
    try {
        Server.workServer()
    } catch (_: ConnectException) {
        println("server/socket off")
    } catch (_: SocketException) {
        println("server/socket off")
    }
}