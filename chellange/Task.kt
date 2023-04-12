import java.io.File
import kotlin.random.Random

fun main() {
    val game = GameWord(DeckCard())
    game.game()
}

class DeckCard() {
    val collection = mutableMapOf<String, String>()
}

class GameWord(val input: DeckCard) {
    var hard = mutableMapOf<String, Int>()
    var log = ""
    fun game() {
        do {
            println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):").also { log += "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):\n" }
            val user = readln()
            log += "$user\n"
            when (user) {
                "add" -> addCard()
                "remove" -> removeCard()
                "import" -> importCard()
                "export" -> exportCard()
                "ask" -> askCard()
                "log" -> logCard()
                "hardest card" -> hardest()
                "reset stats" -> resetStat()
                "print" -> {
                    println("collection card ${input.collection}")
                    println("stats ${hard}")
                    println("log consist: $log")
                }
            }
        } while (user != "exit")
        println("Bye bye!").also{ log += "Bye bye!" }
    }

    fun addCard() {
        println("The card:").also{ log += "The card:\n" }
        val term = readln()
        log += "$term\n"
        val condition = input.collection.filter { it.key == term }
        if (condition.size != 0) println("The card \"$term\" already exists.").also{ log += "The card \"$term\" already exists.\n" }
        else {
            println("The definition of the card:").also{ log += "The definition of the card:\n" }
            val def = readln()
            log += "$def\n"
            val condition = input.collection.filter { it.value == def }
            if (condition.size != 0) println("The definition \"$def\" already exists.").also{ log += "The definition \"$def\" already exists.\n" }
            else input.collection.put(term, def).also { println("The pair (\"$term\":\"$def\") has been added") }.also { log += "The pair (\"$term\":\"$def\") has been added\n" }
        }
    }

    fun removeCard() {
        val user = println("Which card?").also { log += "Which card?\n" }.run { readln() }
        log += "$user\n"
        if (input.collection.containsKey(user)) println("The card has been removed.").also { log += "The card has been removed.\n" }.run { input.collection.remove(user) }
        else println("Can't remove \"$user\": there is no such card.").also { log += "Can't remove \"$user\": there is no such card.\n" }
    }

    fun importCard() {
        val user = println("File name:").also { log += "File name:\n" }.run { readln() }
        log += "$user\n"
        if (File(user).exists()) {
            var index = 0
            File(user).forEachLine{
                val arr = Regex("\\S+").findAll(it).map{it.value}.toList()
                if (arr.isNotEmpty()) {
                    input.collection.put(arr[0], arr[1])
                    hard.put(arr[0], arr[2].toInt())
                    index++
                }
            }
            println("$index cards have been loaded.").also { log += "$index cards have been loaded.\n" }
        } else {
            println("File not found.").also { log += "File not found.\n" }
        }
    }

    fun exportCard() {
        val user = println("File name:").also { log += "File name:\n" }.run { readln() }
        log += "$user\n"
        var index = 0
        File(user).printWriter().use {
            writer -> input.collection.forEach { writer.println("${it.key} ${it.value} ${hard.getValue(it.key)}").also { index++ } }
        }
        println("$index cards have been saved.").also { log += "$index cards have been saved.\n" }
    }

    fun askCard() {
        val user = println("How many times to ask?").also { log += "How many times to ask?\n" }.run { readln().toInt() }
        log += "$user\n"
        for (index in 0 until user) {
            val random = input.collection.keys.toList()[Random.nextInt(input.collection.size)]
            if (!hard.containsKey(random)) hard += mapOf(random to 0)
            println("Print the definition of \"$random\":").also { log += "Print the definition of \"$random\":\n" }
            val userInput = readln()
            log += "$userInput\n"
            val condition = input.collection.filter { it.value == userInput }
            if (userInput == input.collection.get(random)) println("Correct!").also { log += "Correct!\n" }
            else if (condition.size != 0) println("Wrong. The right answer is \"${input.collection.get(random)}\", but your definition is correct for \"${condition.keys.toList()[0]}\".\"").also { log += "Wrong. The right answer is \"${
                input.collection.get(random)}\", but your definition is correct for \"${condition.keys.toList()[0]}\".\"\n" }.run { hard += mapOf(random to hard.getValue(random) + 1) }
            else println("Wrong. The right answer is \"${input.collection.get(random)}\".").also { log += "Wrong. The right answer is \"${
                input.collection.get(random)}\".\n" }.run { hard += mapOf(random to hard.getValue(random) + 1) }
        }
    }

    fun logCard() {
        val user = println("File name:").also { log += "File name:\n" }.run { readln() }
        log += "$user\n"
        File(user).writeText(log).also { println("The log has been saved.").also { log += "The log has been saved.\n" } }
    }

    fun hardest() {
        val hardest = hard.filter { cell -> cell.value == hard.maxBy { it.value }.value }
        if (hardest.isNotEmpty()) {
            if (hardest.values.toList()[0] == 0) println("There are no cards with errors.").also { log += "There are no cards with errors.\n" }
            else {
                if (hardest.size == 1) {
                    println("The hardest card is \"${hardest.keys.toList()[0]}\". You have ${hardest.values.toList()[0]} errors answering it.").also { log += "The hardest card is \"${hardest.keys.toList()[0]}\". You have ${hardest.values.toList()[0]} errors answering it.\n" }
                } else {
                    println("The hardest cards are ${hardest.keys.joinToString("\", \"", "\"", "\"")}").also{log += "The hardest cards are ${hardest.keys.joinToString("\", \"", "\"", "\"")}\n"}
                }
            }
        } else {
            println("Map card is empty").also { log += "Map card is empty\n" }
        }
    }

    fun resetStat() {
        hard.keys.forEach { cell -> hard += cell to 0 }
        println("Card statistics have been reset.").also { log += "Card statistics have been reset.\n" }
    }
}