import java.io.File
import java.io.PrintWriter
import java.lang.Exception
import java.util.Scanner

fun sortedList(dataType:String, list:MutableList<String>) {
    if (dataType == "long") println("Sorted data: " + list.filter { it.matches(Regex("-?\\d++")) }.map{it.toInt()}.sorted().joinToString(" "))
    else if (dataType == "word") println("Sorted data: " + list.sorted().joinToString(" "))
    else println("Sorted data:\n" + list.sorted().joinToString("\n"))
}

fun sortedMap(dataType:String, list:MutableList<String>) {
    val newMap = mutableMapOf<String, Int>()
    val result: List<Pair<String, Int>>
    list.forEach {
        if (newMap.contains(it)) newMap[it] = newMap[it]!! + 1
        else newMap += it to 1
    }
    if (dataType == "long") {
        result = newMap.toList().filter { it.first.matches(Regex("-?\\d++")) }.sortedBy { it.first.toInt() }.sortedBy { it.second }
    } else {
        result = newMap.toList().sortedBy { it.first }.sortedBy { it.second }
    }
    result.forEach {
        println("${it.first}: ${it.second} time(s), ${100 * it.second / list.size}%")
    }
}

fun sortedListFile(dataType:String, list:MutableList<String>, file:PrintWriter) {
    if (dataType == "long") file.println("Sorted data: " + list.filter { it.matches(Regex("-?\\d++")) }.map{it.toInt()}.sorted().joinToString(" "))
    else if (dataType == "word") file.println("Sorted data: " + list.sorted().joinToString(" "))
    else file.println("Sorted data:\n" + list.sorted().joinToString("\n"))
}

fun sortedMapFile(dataType:String, list:MutableList<String>, file:PrintWriter) {
    val newMap = mutableMapOf<String, Int>()
    val result: List<Pair<String, Int>>
    list.forEach {
        if (newMap.contains(it)) newMap[it] = newMap[it]!! + 1
        else newMap += it to 1
    }
    if (dataType == "long") {
        result = newMap.toList().filter { it.first.matches(Regex("-?\\d++")) }.sortedBy { it.first.toInt() }.sortedBy { it.second }
    } else {
        result = newMap.toList().sortedBy { it.first }.sortedBy { it.second }
    }
    result.forEach {
        file.println("${it.first}: ${it.second} time(s), ${100 * it.second / list.size}%")
    }
}

fun main(args:Array<String>) {
    try {
        val keyWord = arrayOf("long", "word", "line", "byCount", "natural", "-dataType", "-sortingType", "-inputFile", "-outputFile")
        var messege = ""
        val inputFile = if (args.indexOf("-inputFile") > -1) args[args.indexOf("-inputFile") + 1] ?: throw Exception("No file") else 0
        val outputFile = if (args.indexOf("-inputFile") > -1) args[args.indexOf("-inputFile") + 1] ?: throw Exception("No file") else 0
        args.forEach { if (it !in keyWord && !((args.indexOf("-inputFile") > -1 && args[args.indexOf("-inputFile") + 1] == it) || (args.indexOf("-outputFile") > -1 && args[args.indexOf("-outputFile") + 1] == it)))
            messege += "\"$it\" is not a valid parameter. It will be skipped.\n"}
        if (messege.isNotEmpty()) print(messege)
        val scanner = Scanner(System.`in`)
        val dataType = if (args.indexOf("-dataType") != -1) args[args.indexOf("-dataType") + 1]
            ?: throw Exception("No data type defined!") else "long"
        val element = when (dataType) {
            "long" -> "numbers"
            "word" -> "words"
            "line" -> "lines"
            else -> "shit"
        }
        val sortingType = if (args.indexOf("-sortingType") != -1) args[args.indexOf("-sortingType") + 1]
            ?: throw Exception("No sorting type defined!") else "natural"
        val result = mutableListOf<String>()
        if (!args.contains("-inputFile")) {
            while (scanner.hasNext()) result += if (dataType != "line") scanner.next() else scanner.nextLine()
        } else {
            File(args[args.indexOf("-inputFile") + 1]).forEachLine {
                if (dataType == "line") result += it
                else Regex("\\S+").findAll(it).forEach { word -> result += word.value }
            }
        }
        if (!args.contains("-outputFile")) {
            if (dataType == "long") result.forEach { if (!it.matches(Regex("-?\\d++"))) println("\"${it}\" is not a long. It will be skipped.")}
            println("Total $element: ${result.size}.")
            when (sortingType) {
                "natural" -> sortedList(dataType, result)
                "byCount" -> sortedMap(dataType, result)
            }
        } else {
            PrintWriter(args[args.indexOf("-outputFile") + 1]).use {
                if (dataType == "long") result.forEach { word -> if (!word.matches(Regex("-?\\d++"))) it.println("\"${it}\" is not a long. It will be skipped.")}
                it.println("Total $element: ${result.size}.")
                when (sortingType) {
                    "natural" -> sortedListFile(dataType, result, it)
                    "byCount" -> sortedMapFile(dataType, result, it)
                }
            }
        }
    } catch (e:Exception) {
        println(e.message)
    }
}


