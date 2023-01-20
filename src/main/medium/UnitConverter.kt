import java.lang.Exception

fun main() {
    do {
        print("Enter what you want to convert (or exit): ")
        val a = readln()
        if (a != "exit") {
            val words:Array<String>  = a.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            try {
                var index = 0
                val numberFirst = words[0].toDouble()
                if (words[1].lowercase() == "degree" || words[1].lowercase() == "degrees") index++
                val parametrFirst = si(words[1 + index])
                if (words[3 + index].lowercase() == "degree" || words[3 + index].lowercase() == "degrees") index++
                val parametrSecond = si(words[3 + index])
                if (parametrFirst[0] != parametrSecond[0] || parametrFirst[0] == "???") {
                    println("Conversion from ${parametrFirst[3]} to ${parametrSecond[3]} is impossible")
                } else if (numberFirst < 0 && parametrFirst[0] != "temp") {
                    println("${parametrFirst[0]} shouldn't be negative")
                } else {
                    val convert = ((parametrFirst[1].toDouble() * (numberFirst + parametrFirst[4].toDouble()) / parametrSecond[1].toDouble())) - parametrSecond[4].toDouble()
                    println(
                        "$numberFirst ${if (numberFirst != 1.0) parametrFirst[3] else parametrFirst[2]} is $convert " +
                                if (convert != 1.0) parametrSecond[3] else parametrSecond[2]
                    )
                }
            } catch (e:NumberFormatException) {
                println("Parse error")
            }
        }
    } while (a != "exit")
}

fun si(str:String) = when (str.lowercase()) {
    "m", "meter", "meters" -> listOf<String>(LengthMy.Meter.messuare, LengthMy.Meter.koef, LengthMy.Meter.one, LengthMy.Meter.many, "0")
    "km", "kilometer", "kilometers" -> listOf<String>(LengthMy.Kilometer.messuare, LengthMy.Kilometer.koef, LengthMy.Kilometer.one, LengthMy.Kilometer.many, "0")
    "cm", "centimeter", "centimeters" -> listOf<String>(LengthMy.Centimeter.messuare, LengthMy.Centimeter.koef, LengthMy.Centimeter.one, LengthMy.Centimeter.many, "0")
    "mm", "millimeter", "millimeters" -> listOf<String>(LengthMy.Millimeter.messuare, LengthMy.Millimeter.koef, LengthMy.Meter.one,LengthMy.Millimeter.many, "0")
    "mi", "mile", "miles" -> listOf<String>(LengthMy.Mile.messuare, LengthMy.Mile.koef, LengthMy.Mile.one, LengthMy.Mile.many, "0")
    "yd", "yard", "yards" -> listOf<String>(LengthMy.Yard.messuare, LengthMy.Yard.koef, LengthMy.Yard.one, LengthMy.Yard.many, "0")
    "ft", "foot", "feet" -> listOf<String>(LengthMy.Foot.messuare, LengthMy.Foot.koef, LengthMy.Foot.one, LengthMy.Foot.many, "0")
    "in", "inch", "inches" -> listOf<String>(LengthMy.Inch.messuare, LengthMy.Inch.koef, LengthMy.Inch.one, LengthMy.Inch.many, "0")
    "g", "gram", "grams" -> listOf<String>(WeightMy.Gramm.messuare, WeightMy.Gramm.koef, WeightMy.Gramm.one, WeightMy.Gramm.many, "0")
    "kg", "kilogram", "kilograms" -> listOf<String>(WeightMy.Kilogramm.messuare, WeightMy.Kilogramm.koef, WeightMy.Kilogramm.one, WeightMy.Kilogramm.many, "0")
    "mg", "milligram", "milligrams" -> listOf<String>(WeightMy.Milligramm.messuare, WeightMy.Milligramm.koef, WeightMy.Milligramm.one, WeightMy.Milligramm.many, "0")
    "lb", "pound", "pounds" -> listOf<String>(WeightMy.Pound.messuare, WeightMy.Pound.koef, WeightMy.Pound.one, WeightMy.Pound.many, "0")
    "oz", "ounce", "ounces" -> listOf<String>(WeightMy.Ounce.messuare, WeightMy.Ounce.koef, WeightMy.Ounce.one, WeightMy.Ounce.many, "0")
    "celsius", "dc", "c" -> listOf<String>(TempMy.Celsius.messuare, TempMy.Celsius.koef, TempMy.Celsius.one, TempMy.Celsius.many, TempMy.Celsius.addNum)
    "fahrenheit", "df", "f" -> listOf<String>(TempMy.Fahrenheit.messuare, TempMy.Fahrenheit.koef, TempMy.Fahrenheit.one, TempMy.Fahrenheit.many, TempMy.Fahrenheit.addNum)
    "kelvin", "kelvins", "k" -> listOf<String>(TempMy.Kelvins.messuare, TempMy.Kelvins.koef, TempMy.Kelvins.one, TempMy.Kelvins.many, TempMy.Kelvins.addNum)
    else -> listOf<String>("???", "???", "???", "???")
}

enum class LengthMy(val koef:String, val messuare:String, val one:String, val many:String) {
    Meter("1.0", "length", "meter", "meters"),
    Kilometer("1000.0" ,"length", "kilometer", "kilometers"),
    Centimeter("0.01", "length", "centimeter", "centimeters"),
    Millimeter("0.001", "length", "millimeter", "millimeters"),
    Mile("1609.35", "length", "mile", "miles"),
    Yard("0.9144", "length", "yard", "yards"),
    Foot("0.3048", "length", "foot", "feet"),
    Inch("0.0254", "length", "inch", "inches")
}

enum class WeightMy(val koef:String, val messuare:String, val one:String, val many:String) {
    Gramm("1.0", "weight", "gram", "grams"),
    Kilogramm("1000.0", "weight", "kilogram", "kilograms"),
    Milligramm("0.001", "weight", "milligram", "milligrams"),
    Pound("453.592", "weight", "pound", "pounds"),
    Ounce("28.3495", "weight", "ounce", "ounces")
}

enum class TempMy(val koef:String, val addNum: String, val messuare:String, val one:String, val many:String) {
    Celsius("1.0", "0", "temp", "degree Celsius", "degrees Celsius"),
    Fahrenheit((5.0/9).toString(), "-32", "temp", "degree Fahrenheit", "degrees Fahrenheit"),
    Kelvins("1.0", "-273.15", "temp", "kelvin", "kelvins"),
}