import java.io.File

fun inverseIndex(contentFile: List<String>): MutableMap<String, MutableList<Int>> {
    val inverseIndex = mutableMapOf<String, MutableList<Int>>()
    for ((index, string) in contentFile.withIndex()) {
        val words = string.lowercase().split(" ")
        for (word in words) {
            inverseIndex[word] =
                (inverseIndex.getOrDefault(word, mutableListOf()) + mutableListOf(index)).toMutableList()
        }
    }
    return inverseIndex
}

fun waitingAnswerFromUser(title: String) = println(title).run { readln() }

fun workMethodSearch(
    method: String,
    userInput: List<String>,
    inverseIndex: MutableMap<String, MutableList<Int>>,
    size: Int
): MutableSet<Int> {
    val resultIndices = mutableSetOf<Int>()
    when (method) {
        "ANY" -> {
            for (elem in userInput) {
                if (inverseIndex[elem] != null) resultIndices.addAll(inverseIndex[elem]!!)
            }
        }

        "ALL" -> {
            for ((index, elem) in userInput.withIndex()) {
                if (inverseIndex[elem] != null) {
                    if (index == 0) resultIndices.addAll(inverseIndex[elem]!!)
                    else resultIndices.removeIf { !inverseIndex[elem]!!.contains(it) }
                }
            }
        }

        "NONE" -> {
            resultIndices.addAll(MutableList(size) { it })
            for (elem in userInput) {
                if (inverseIndex[elem] != null) {
                    resultIndices.removeIf { inverseIndex[elem]!!.contains(it) }
                }
            }
        }
    }
    return resultIndices
}

fun main(args: Array<String>) {
    val nameFile = args.getOrNull(args.indexOf("--data") + 1)
    if (nameFile == null || !File(nameFile).exists()) {
        println("File not found")
    } else {
        val contentFile = File(nameFile).readLines()
        val inverseIndex = inverseIndex(contentFile)
        do {
            val action =
                waitingAnswerFromUser("=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit").toIntOrNull()
            when (action) {
                1 -> {
                    val method = waitingAnswerFromUser("Select a matching strategy: ALL, ANY, NONE")
                    val userInput =
                        waitingAnswerFromUser("Enter a name or email to search all matching people.").lowercase()
                            .split(" ")
                    val resultIndices = workMethodSearch(method, userInput, inverseIndex, contentFile.size)
                    if (resultIndices.isEmpty()) println("No matching people found.")
                    else println(contentFile.filterIndexed { index, _ -> index in resultIndices }.joinToString("\n"))
                }

                2 -> {
                    println("=== List of people ===")
                    for (elem in contentFile) println(elem)
                }

                0 -> println("Bye!")
                else -> println("Incorrect option! Try again.")
            }
        } while (action != 0)
    }
}
