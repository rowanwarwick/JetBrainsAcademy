package search

import java.io.File

fun main(args: Array<String>) {
    val nameFile = args.getOrNull(args.indexOf("--data") + 1)
    if (nameFile == null || !File(nameFile).exists()) {
        println("File not found")
    } else {
        val inverseIndex = mutableMapOf<String, MutableList<Int>>()
        val list = File(nameFile).readLines()
        for ((index, string) in list.withIndex()) {
            val words = string.lowercase().split(" ")
            for (word in words) {
                inverseIndex[word] =
                    (inverseIndex.getOrDefault(word, mutableListOf()) + mutableListOf(index)).toMutableList()
            }
        }
        do {
            println("=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit")
            val user = readln().toInt()
            when (user) {
                1 -> {
                    val resultIndices = mutableSetOf<Int>()
                    println("Select a matching strategy: ALL, ANY, NONE")
                    val method = readln()
                    println("Enter a name or email to search all matching people.")
                    val user = readln().lowercase().split(" ")
                    when (method) {
                        "ANY" -> {
                            for (elem in user) {
                                if (inverseIndex[elem] != null) resultIndices.addAll(inverseIndex[elem]!!)
                            }
                        }

                        "ALL" -> {
                            for ((index, elem) in user.withIndex()) {
                                if (inverseIndex[elem] != null) {
                                    if (index == 0) resultIndices.addAll(inverseIndex[elem]!!)
                                    else resultIndices.removeIf { !inverseIndex[elem]!!.contains(it) }
                                }
                            }
                        }

                        "NONE" -> {
                            resultIndices.addAll(MutableList(list.size) { it })
                            for (elem in user) {
                                if (inverseIndex[elem] != null) {
                                    resultIndices.removeIf { inverseIndex[elem]!!.contains(it) }
                                }
                            }
                        }
                    }
                    if (resultIndices.isEmpty()) println("No matching people found.")
                    else println(list.filterIndexed { index, _ -> index in resultIndices }.joinToString("\n"))
                }

                2 -> {
                    println("=== List of people ===")
                    for (elem in list) println(elem)
                }

                0 -> println("Bye!")
                else -> println("Incorrect option! Try again.")
            }
        } while (user != 0)
    }
}
