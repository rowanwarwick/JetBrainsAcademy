fun check (fileContent: List<String>, argument: String): List<String> {
    val invalidWords = fileContent.count { !"""[a-zA-Z]{5}""".toRegex().matches(it) || it.count() != it.toSet().size }
    if (invalidWords != 0) println("Error: $invalidWords invalid words were found in the $argument file."). also { kotlin.system.exitProcess(1) }
    return fileContent.map { it.lowercase() }
}
fun virtuosoGame(candidateList: List<String>) {
    val candidateWord = candidateList.random()
    var counter = 0
    val clueStringStorage = mutableListOf<String>()
    val wrongCharset = mutableSetOf<Char>()
    val start = System.nanoTime()
    while (true) {
        counter++.also { println("Input a 5-letter word:") }
        readln().lowercase().run {
            when {
                this == "exit" -> println("The game is over.").also { return }
                length != 5 -> println("The input isn't a 5-letter word.")
                contains("[^a-z]".toRegex()) -> println("One or more letters of the input aren't valid.")
                count() != toSet().size -> println("The input has duplicate letters.")
                !candidateList.contains(this) && candidateList.size != 1 -> println("The input word isn't included in my words list.")
                else -> {
                    var clueString = ""
                    val (green, yellow, gray, azure, default) = listOf("\u001B[48:5:10m", "\u001B[48:5:11m", "\u001B[48:5:7m", "\u001B[48:5:14m", "\u001B[0m")
                    this.forEachIndexed { index, c ->
                        clueString += when {
                            candidateWord.contains(c) && index == candidateWord.indexOf(c) -> "$green${c.uppercase()}$default"
                            candidateWord.contains(c) -> "$yellow${c.uppercase()}$default"
                            else -> "$gray${c.uppercase()}$default"
                        }
                        if (!candidateWord.contains(c)) wrongCharset += c
                    }
                    clueStringStorage += clueString
                    println("\n${clueStringStorage.joinToString("\n")}\n")
                    when {
                        this == candidateWord && counter == 1 -> println("Correct!\nAmazing luck! The solution was found at once.").also { return }
                        this == candidateWord -> println("Correct!\nThe solution was found after $counter tries in ${(System.nanoTime() - start) / 1000_000_000} seconds.").also { return }
                        else -> println("$azure${wrongCharset.map { it.uppercase() }.sorted().joinToString("")}$default")
                    }
                }
            }
        }
    }
}
fun main(args: Array<String>) {
    val listOfWords = MutableList(0) {List(0) { "" }}
    var includedNumber = 0
    if (args.size != 2) println("Error: Wrong number of arguments.").also { return }
    args.forEach {
        if (!java.io.File(it).exists()) println("Error: The ${if (args.indexOf(it) % 2 == 0) "" else "candidate "}words file $it doesn't exist.").also { return }
        else listOfWords += check(java.io.File(it).readLines(), it)
    }
    for (i in listOfWords[1]) if (!listOfWords[0].contains(i)) includedNumber++
    if (includedNumber != 0) println("Error: $includedNumber candidate words are not included in the ${args[0]} file.").also { return }
    else println("Words Virtuoso\n")
    virtuosoGame(listOfWords[1])
}