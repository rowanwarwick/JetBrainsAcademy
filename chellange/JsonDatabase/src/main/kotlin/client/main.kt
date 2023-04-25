package client

fun main(args: Array<String>) {
    try {
        Client.requestClient(args)
    } catch (_: ExceptionInInitializerError) {
        println("server/socket off")
    }
}