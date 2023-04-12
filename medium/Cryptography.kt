fun main() {
    do {
        println("Task (hide, show, exit):")
        val input = readln()
        println(when (input) {
            "hide" -> "Hiding message in image."
            "show" -> "Obtaining message from image."
            "exit" -> "Bye!"
            else -> "Wrong task: $input"
        })
    } while (input != "exit")
}