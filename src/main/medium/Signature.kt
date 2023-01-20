import java.io.File

fun main() {
    print("Enter name and surname:")
    val name = readln()
    print("Enter person's status:")
    val status = readln()
    val massiveName = mutableListOf(mutableListOf(""))
    val massiveStatus = mutableListOf(mutableListOf(""))
    val lenName = createList(name, massiveName, 9)
    val lenStatus = createList(status, massiveStatus, 2)
    val len = if (lenName >= lenStatus) lenName + 8 else lenStatus + 8
    repeat(len) {
        print('8')
    }
    println()
    printName(massiveName, len - lenName - 8, 9)
    printName(massiveStatus, len - lenStatus - 8, 2)
    repeat(len) {
        print('8')
    }
}

fun createList(name:String, massiveName:MutableList<MutableList<String>>, opt:Int):Int {
    var lenName = 0
    var indexName = 0
    val separator = File.separator
    val fileName = if (opt == 9)
        ".${separator}src${separator}main${separator}medium${separator}roman.txt"
    else
        ".${separator}src${separator}main${separator}medium${separator}medium.txt"
    for (i in name) {
        val massive = mutableListOf<String>()
        if (i != ' ') {
            File(fileName).forEachLine {
                if (i == it.first() && it.matches("\\w \\d\\d?".toRegex())) {
                    var len = 0
                    for (j in it) if (j.isDigit()) len = len * 10 + j.code - 48
                    indexName = 1
                    lenName += len
                } else if (indexName in 1..opt + 1) {
                    massive += it
                    indexName++
                } else indexName = 0
            }
        } else {
            massive += if (opt == 9) {
                mutableListOf("          ", "          ", "          ", "          ", "          ", "          ", "          ", "          ", "          ", "          ")
                }
            else
                mutableListOf("     ", "     ", "     ")
            lenName += opt + 1
        }
        if (massive.isNotEmpty()) massiveName += massive
    }
    massiveName.removeAt(0)
    return lenName
}

fun printName(massiveName:MutableList<MutableList<String>>, addSpace:Int, count:Int) {
    for (str in 0..count) {
        if (addSpace % 2 == 0) print("88   ") else print("88  ")
        repeat(addSpace / 2 + addSpace % 2) {
            print(" ")
        }
        for (index in massiveName) print(index[str])
        repeat(addSpace / 2 + addSpace % 2) {
            print(" ")
        }
        println(" 88")
    }
}