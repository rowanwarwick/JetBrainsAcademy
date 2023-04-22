package client

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.net.InetAddress
import java.net.Socket

object Client {
    init {
        val sep = File.separator
        System.setProperty("client.data", "${System.getProperty("user.dir")}${sep}src${sep}main${sep}kotlin${sep}client${sep}data${sep}") //
    }

    private const val address = "127.0.0.1"
    private const val port = 23456
    private val socket = Socket(InetAddress.getByName(address), port)
    private val input = DataInputStream(socket.getInputStream())
    private val output = DataOutputStream(socket.getOutputStream())

    private fun put() {
        println("Enter name of the file")
        val name = readln()
        val filePath = System.getProperty("client.data") + name
        if (File(filePath).exists()) {
            val format = "[.].+".toRegex().findAll(name).last().value
            val message = File(filePath).readBytes()
            println("Enter name of the file to be saved on server:")
            output.apply {
                writeUTF("PUT $name $format ${readln()}")
                writeInt(message.size)
                write(message)
            }.also { println("The request was sent.") }
            val answer = input.readUTF().split(" ")
            println(if (answer[0] == "200") "Response says that file is saved! ID = ${answer[1]}" else "The response says that creating the file was forbidden!")
        } else {
            println("File not found")
        }
    }

    private fun get() {
        println("Do you want to get the file by name or by id (1 - name, 2 - id):")
        when (val user = readln()) {
            "1", "2" -> {
                println(if (user == "1") "Enter name of the file:" else "Enter id:")
                output.writeUTF("GET $user${readln()}").also { println("The request was sent.") }
                val answer = input.readUTF().split(" ")
                if (answer[0] == "200") {
                    val length = input.readInt()
                    val message = ByteArray(length)
                    input.readFully(message, 0, message.size)
                    println("The file was downloaded! Specify a name for it:")
                    File(System.getProperty("client.data") + readln() + answer[1]).writeBytes(message)
                    println("File saved on the hard drive!")
                } else {
                    println("The response says that this file is not found!")
                }
            }

            else -> println("Invalid input")
        }
    }

    private fun delete() {
        println("Do you want to delete the file by name or by id (1 - name, 2 - id):")
        when (val user = readln()) {
            "1", "2" -> {
                println(if (user == "1") "Enter name of the file:" else "Enter id:")
                output.writeUTF("DELETE $user${readln()}").also { println("The request was sent.") }
                println(if (input.readUTF() == "200") "The response says that the file was successfully deleted!" else "The response says that the file was not found!")
            }

            else -> println("Invalid input")
        }
    }


    fun query() {
        println("Enter action (1 - get a file, 2 - save a file, 3 - delete a file):")
        when (readln().uppercase()) {
            "2" -> put()
            "1" -> get()
            "3" -> delete()
            "EXIT" -> output.writeUTF("EXIT")
        }
        socket.close()
    }
}

fun main() {
    Client.query()
}