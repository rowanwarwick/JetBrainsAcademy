import java.io.File
import kotlin.math.abs
import kotlin.random.Random

fun main(args: Array<String>) {
    var out = 0
    if (args.size == 2) {
        out = openFile(args[0], 0)
        if (out == 0) out = openFile(args[1], 1)
        if (out == 0) out = conditionWord(args[0])
        if (out == 0) out = conditionWord(args[1])
        if (out == 0) out = conditionFile(args[0], args[1])
        if (out == 0) println("Words Virtuoso")
    } else {
        println("Error: Wrong number of arguments.")
        out = 1
    }
    if (out == 0) game(args)
}

fun openFile(name: String, mode:Int): Int {
    var result = 0
    if (!File(name).exists()) {
        println(if (mode == 0) "Error: The words file $name doesn't exist." else "Error: The candidate words file $name doesn't exist.")
        result = 1
    }
    return result
}

fun conditionWord(name: String): Int {
    var incurrect = 0
    File(name).forEachLine { it ->
        if (it.length != 5 || !it.matches(Regex("[a-z]{5}",RegexOption.IGNORE_CASE)) || it.length != it.toSet().size)
            incurrect++
    }
    if (incurrect != 0) println("Error: $incurrect invalid words were found in the $name file.")
    return incurrect
}

fun conditionFile(nameAll: String, nameCand: String): Int {
    var incurrect = 0
    File(nameCand).forEachLine { it1 ->
        var correct = 1
        File(nameAll).forEachLine { it2 ->
            if (it1.lowercase() == it2.lowercase()) correct = 0
        }
        if (correct == 1) incurrect++
    }
    if (incurrect != 0) println("Error: $incurrect candidate words are not included in the $nameAll file.")
    return incurrect
}

fun game(args: Array<String>) {
    var out = 0
    var countWord = 0
    File(args[1]).forEachLine { countWord++ }
    var wordSeed = abs(Random.nextInt() % countWord)
    var word = ""
    File(args[1]).forEachLine { it ->
        if (wordSeed == 0) word = it
        wordSeed--
    }
    val bigWordRiddle = mutableListOf<String>()
    val incorrectChar = mutableSetOf<Char>()
    val start = System.currentTimeMillis()
    var turn = 0
    do {
        println("Input a 5-letter word:")
        val wordPlayer = readln()
        if (wordPlayer == "exit") {
            println("The game is over.")
            out = 1
        }
        if (out == 0) {
            var input = 0
            File(args[0]).forEachLine {if (it == wordPlayer) input = 1}
            when {
                wordPlayer.length != 5 -> println("The input isn't a 5-letter word.")
                !wordPlayer.matches(Regex("[a-z]{5}",RegexOption.IGNORE_CASE)) -> println("One or more letters of the input aren't valid.")
                wordPlayer.length != wordPlayer.toSet().size -> println("The input has duplicate letters.")
                input == 0 -> println("The input word isn't included in my words list.")
                else -> out = 2
            }
        }
        if (out == 2) {
            var wordRiddle = ""
            for (index in wordPlayer.indices) {
                if (wordPlayer[index] == word[index])
                    wordRiddle += "\u001B[48:5:10m${wordPlayer[index].uppercaseChar()}\u001B[0m"
                else {
                    var ex = 0
                    for (ch in word) {
                        if (wordPlayer[index] == ch) {
                            wordRiddle += "\u001B[48:5:11m${wordPlayer[index].uppercaseChar()}\u001B[0m"
                            ex = 1
                        }
                    }
                    if (ex == 0) {
                        incorrectChar += wordPlayer[index].uppercaseChar()
                        wordRiddle += "\u001B[48:5:7m${wordPlayer[index].uppercaseChar()}\u001B[0m"
                    }
                }
            }
            bigWordRiddle += wordRiddle
            for (i in bigWordRiddle.indices) println(bigWordRiddle[i])
            if (wordPlayer == word) println("Correct!")
            else println("\u001B[48:5:14m${incorrectChar.sorted().joinToString("")}\u001B[0m")
            turn++
        }
    } while (wordPlayer != "exit" && wordPlayer != word)
    val end = System.currentTimeMillis()
    println(if (turn != 1) "The solution was found after $turn tries in ${(end - start)/1000} seconds." else "Amazing luck! The solution was found at once.")
}