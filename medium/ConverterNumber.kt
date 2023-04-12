import java.math.BigDecimal
import kotlin.math.pow

fun main() {
    inputData()
//    print("%.20f".format(7.0 / 14.0))
}

fun inputData() {
    var input = ""
    while (input != "/exit") {
        print("Enter two numbers in format: {source base < 37} {target base < 37} (To quit type /exit)")
        input = readln()
        if (input != "/exit") {
            val (source, base) = input.split(' ')
            var menu = ""
            while (menu != "/back") {
                print("Enter number in base $source to convert to base $base (To go back type /back)")
                menu = readln()
                if (menu != "/back") {
                    val resultDec = converterToDec(menu, source.toInt())
                    //println(resultDec)
                    val result = converterFromDec(resultDec, base.toInt())
                    println("Conversion result: $result")
                }
            }
        }
    }
}


fun converterToDec(number:String, base:Int): BigDecimal {
    var result = BigDecimal("0")
    val indexMax = if (number.indexOf('.') == -1) number.length else number.indexOf('.')
    var index = 0
    while (index < indexMax) {
        val ch:Int = when (number[index]) {
            in 'a'..'z' -> number[index].code - 87
            '.' -> break
            else -> number[index] - '0'
        }
        result += ch.toBigDecimal() * ((base * 1.0).pow(indexMax - index - 1).toBigDecimal())
        index++
    }
    while (++index < number.length) {
        val ch:Int = when (number[index]) {
            in 'a'..'z' -> (number[index].code - 87)
            else -> (number[index] - '0')
        }
        result += (ch * ((base * 1.0).pow(indexMax - index))).toBigDecimal()
    }
    result = if (number.indexOf('.') == -1 || result.scale() > 5) result else result.setScale(5)
    return result
}

fun converterFromDec(number:BigDecimal, base:Int): String {
    val str = number.toString()
    var input = number.toBigInteger()
    var part = number - input.toBigDecimal()
    var result = ""
    var num = 0
    while ((part * base.toBigDecimal()).compareTo(BigDecimal.ZERO) != 0 && num++ < 5) {
        val new = part * base.toBigDecimal()
        result = when (new.toInt()) {
            in 10..36 -> "$result${(new.toInt() + 87).toChar()}"
            else ->  result + new.toInt().toString()
        }
        part = new - new.toBigInteger().toBigDecimal()
    }
    if (result != "" || (str.indexOf('.') <= str.length - 6)) {
        result = ".$result"
        if (result.length >= 6)
            result.substring(0..5)
        else {
            while (result.length != 6)
                result = "${result}0"
        }
    }
    while (input / base.toBigInteger() != 0.toBigInteger() || input % base.toBigInteger() != 0.toBigInteger()) {
        val ch = input % base.toBigInteger()
        result = when (ch.toInt()) {
            in 10..36 -> "${(ch.toInt() + 87).toChar()}$result"
            else -> ch.toString() + result
        }
        input /= base.toBigInteger()
    }
    return result
}