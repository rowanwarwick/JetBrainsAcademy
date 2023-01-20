fun main() {
    val variance = mutableMapOf<String, Int>()
    do {
        val num = readln()
        when (num) {
            "/exit" -> println("Bye!")
            "/help" -> println("The program calculates the sum and sub of numbers")
            else -> {
                if (num.isNotEmpty() && num[0] == '/') {
                    println("Unknown command")
                } else if (Regex(".*=.*").matches(num)) {
                    val key = Regex(" *[a-zA-Z]+ *=.*").matches(num)
                    val valueNum = Regex(".*= *\\d+ *").matches(num)
                    val valueAvail = Regex(".*= *[a-zA-Z]+ *").matches(num)
                    if (!key) println("Invalid identifier")
                    else if (valueNum == valueAvail || num.count { it == '=' }  != 1) println("Invalid assignment")
                    else {
                        val keyAvailable = num.substringBefore('=').replace(" ", "")
                        val valueAvailable = num.substringAfter('=').replace(" ", "")
                        if (Regex("[a-zA-Z]+").matches(valueAvailable) && variance.containsKey(valueAvailable))
                            variance += keyAvailable to (variance[valueAvailable] ?: 0)
                        else if (Regex("\\d+").matches(valueAvailable)) variance += keyAvailable to valueAvailable.toInt()
                        else println("Unknown variable")
                    }
                } else if (Regex("[a-zA-Z]+").matches(num)) {
                    val key = Regex("[a-zA-Z]+").find(num)
                    if (key != null && variance.containsKey(key.value)) {
                        println(variance[key.value])
                    } else {
                        println("Unknown variable")
                    }
                } else {
                    val arr = num.split(" ").map { it.replace(" ", "")}.toMutableList()
                    if (arr[0].isNotEmpty()) {
                        if (Regex("[+-]+").matches(arr[0])) arr.add(0, "0")
                        var res = if (variance.containsKey(arr[0])) variance[arr[0]] ?: 0 else arr[0].toIntOrNull()
                        if (res == null) {
                            println("Unknown variable")
                        } else {
                            for (index in arr.indices) {
                                if ((index % 2 == 0 && !Regex("[-+]?\\d+|[-+]?[a-zA-Z]+").matches(arr[index])) || (index % 2 == 1 && !Regex(
                                        "[+]+|[-]+").matches(arr[index]))
                                ) {
                                    println("Invalid expression")
                                    break
                                }
                                if (Regex("[+]+").matches(arr[index])) arr[index] = "+"
                                if (Regex("[-]+").matches(arr[index])) if (arr[index].length % 2 == 0) arr[index] =
                                    "+" else arr[index] = "-"
                                if (index % 2 == 1) {
                                    val argum = if (variance.containsKey(arr[index + 1])) variance[arr[index + 1]]
                                        ?: 0 else arr[index + 1].toIntOrNull()
                                    if (argum == null) {
                                        println("Unknown variable")
                                    } else {
                                        if (arr[index] == "+") res += argum
                                        if (arr[index] == "-") res -= argum
                                    }
                                }
                            }
                            println(res)
                        }
                    }
                }
            }
        }
    } while (num != "/exit")
}