object Game {
    var randomWord = ""
        set(value) {
            println("Words Virtuoso")
            field = value
        }
    var dataWords = mutableListOf<String>()
    private val hint = mutableSetOf<Char>()
    private val allVariant = mutableListOf<String>()

    fun start() {
        val startTime = System.currentTimeMillis()
        var countRound = 1
        do {
            println("Input a 5-letter word:")
            val input = readln().lowercase()
            if (input != "exit") println(workWithPlayerInput(input))
            when (input) {
                randomWord -> println(
                    "Correct!\n" +
                            if (countRound != 1) "The solution was found after $countRound tries in ${System.currentTimeMillis() - startTime} seconds."
                            else "Amazing luck! The solution was found at once."
                )

                "exit" -> println("The game is over.")
                else -> {
                    if (hint.isNotEmpty()) println("\u001B[48:5:14m${hint.sorted().joinToString("")}\u001B[0m")
                    countRound++
                }
            }
        } while (input != randomWord && input != "exit")
    }

    private fun workWithPlayerInput(input: String): String {
        return when {
            input.length != 5 -> "The input isn't a 5-letter word."
            input.contains("[^a-zA-Z]".toRegex()) -> "One or more letters of the input aren't valid."
            input.toSet().size != input.length -> "The input has duplicate letters."
            !dataWords.contains(input) -> "The input word isn't included in my words list."
            else -> {
                val output = StringBuilder()
                for ((index, char) in input.withIndex()) {
                    if (char in randomWord) {
                        output.append(if (index == randomWord.indexOf(char)) "\u001B[48:5:10m${char.uppercase()}\u001B[0m" else "\u001B[48:5:11m${char.uppercase()}\u001B[0m")
                    } else {
                        output.append("\u001B[48:5:7m${char.uppercase()}\u001B[0m")
                        hint.add(char.uppercaseChar())
                    }
                }
                allVariant.add(output.toString())
                allVariant.joinToString("\n", "\n")
            }
        }
    }
}