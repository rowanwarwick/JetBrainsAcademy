import kotlin.math.*

fun coder() {
    println("Input string:")
    val str = readln()
    var numStr = ""
    for (i in str.indices) {
        val num = String.format("%07d", Integer.toBinaryString(str[i].code).toInt())
        numStr += num
    }
    println("Encoded string:")
    var message = 2
    for (i in numStr.indices) {
        if (message == 1) {
            print("0")
            if (numStr[i] == '0') {
                message = 2
                print(" ")
            }
        } else if (message == 0) {
            print("0")
            if (numStr[i] == '1') {
                print(" ")
                message = 2
            }
        }
        if (message == 2) {
            message = if (numStr[i] == '0') {
                print("00 ")
                0
            } else {
                print("0 ")
                1
            }
        }
    }
    print("0")
    println()
}

fun decoder() {
    var numStr = ""
    val numberZero = '0'
    val numberOne = '1'
    var printMod = 3
    var countBs = 0
    var numberBlock = 1
    var firstBlock = 0
    var error = 0
    var index = 0
    println("Input encoded string:")
    val str = readln()
    for (begin in 0..2) {
        if (str[begin] == '0') firstBlock++
    }
    if (firstBlock > 2) {
        println("Encoded string is not valid.")
        error = 1
    }
    while (index < str.length && error == 0) {
        if (str[index] != '0' && str[index] != ' ') {
            println("Encoded string is not valid.")
            error = 1
        }
        if (str[index] == ' ' && index != (str.length - 1)) numberBlock++
        index++
    }
    if (error == 0 && numberBlock % 2 == 1) {
        println("Encoded string is not valid.")
        error = 1
    }
    if (error == 0) {
        for (i in 0 until str.length - 1) {
            if (str[i] == '0') {
                when (printMod) {
                    0 -> printMod++
                    1 -> numStr += numberZero
                    2 -> numStr += numberOne
                }
            }
            if (str[i] == '0' && str[i + 1] == '0' && printMod == 3) printMod = 0
            if (str[i] == '0' && str[i + 1] == ' ' && printMod == 3) printMod = 2
            if (str[i] == ' ') {
                if (countBs == 1) {
                    countBs = 0
                    printMod = 3
                } else countBs++
            }
        }
        if (str[str.length - 1] != ' ') {
            numStr += if (printMod == 2) numberOne
            else numberZero
        }
        if (numStr.length % 7 != 0) {
            println("Encoded string is not valid.")
        } else {
            println("Decoded string:")
            var schar = 0
            for (i in numStr.indices) {
                if (i % 7 == 0 && i != 0) {
                    print(schar.toChar())
                    schar = 0
                }
                schar += ((numStr[i] - '0') * 2.0.pow(6 - i % 7)).toInt()
            }
            print(schar.toChar())
            println()
        }
    }
}

fun main() {
    var exitFor = 1
    do {
        println("Please input operation (encode/decode/exit):")
        val str = readln()
        when (str) {
            "encode" -> coder()
            "decode" -> decoder()
            "exit" -> {
                exitFor = 0
                println("Bye!")
            }
            else -> println("There is no '$str' operation")
        }
    } while (exitFor == 1)
}