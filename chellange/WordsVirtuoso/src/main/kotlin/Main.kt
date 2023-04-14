import java.io.File
import java.io.FileReader
import kotlin.random.Random

class Words(private val fileCandidate: String, private val fileAll: String) {

    private val listCandidates = mutableListOf<String>()
    val listAllWords = mutableListOf<String>()
    val check: Boolean by lazy {
        fileCanOpen(fileAll) && fileCanOpen(fileCandidate, true) &&
                numberIncorrectWords() && numberIncorrectWords(true) && isIncludeAllCandidate()
    }

    private fun fileCanOpen(name: String, isCandidate: Boolean = false): Boolean {
        val result = File(name).exists()
        if (!result) println("Error: The ${if (isCandidate) "candidate " else ""}words file $name doesn't exist.")
        else {
            FileReader(name).use {
                if (isCandidate) listCandidates.addAll(it.readLines().map { word -> word.lowercase() })
                else listAllWords.addAll(it.readLines().map { word -> word.lowercase() })
            }
        }
        return result
    }

    private fun isIncludeAllCandidate(): Boolean {
        val result = listAllWords.containsAll(listCandidates)
        if (!result) {
            var countNotInclude = 0
            for (candidate in listCandidates) if (!listAllWords.contains(candidate)) countNotInclude++
            println("Error: $countNotInclude candidate words are not included in the $fileAll file.")
        }
        return result
    }

    private fun conditionRightWord(word: String): Boolean {
        word.apply { return !(length != 5 || contains("[^a-zA-Z]".toRegex()) || toSet().size != length) }
    }

    private fun numberIncorrectWords(isCandidate: Boolean = false): Boolean {
        var countIncorrect = 0
        for (word in if (!isCandidate) listAllWords else listCandidates) {
            if (!conditionRightWord(word)) countIncorrect++
        }
        if (countIncorrect != 0) println("Error: $countIncorrect invalid words were found in the ${if (!isCandidate) fileAll else fileCandidate} file.")
        return countIncorrect == 0
    }

    fun generateRandomWord() = listCandidates[Random.Default.nextInt(listCandidates.size)]
}

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

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Error: Wrong number of arguments.")
    } else {
        val inputData = Words(args[1], args[0])
        if (inputData.check) {
            Game.randomWord = inputData.generateRandomWord()
            Game.dataWords = inputData.listAllWords
            Game.start()
        }
    }
}
