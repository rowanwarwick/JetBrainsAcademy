// kotlinc hello.kt -include-runtime -d hello.jar
// java -jar a.jar

fun condition(arr:MutableList<MutableList<Char>>):Int {
    var x = 0
    var o = 0
    var space = 0
    var res = 1
    for (i in 0..8) {
        if (arr[i / 3][i % 3] == 'X') {
            x++
        } else if (arr[i / 3][i % 3] == 'O') {
            o++
        } else {
            space++
        }
    }
    if (x > o + 1 || o > x + 1 || (winX(arr)) == 1 && winO(arr) == 1) {
        print("Impossible")
    } else if (winX(arr) == 1) {
        print("X wins")
    } else if (winO(arr) == 1) {
        print("O wins")
    } else if (space == 0) {
        print("Draw")
    } else {
        res = 0
    }
    return res
}

fun winO(arr:MutableList<MutableList<Char>>):Int {
    var x = 0
    if ("" + arr[0][0] + arr[0][1] + arr[0][2] == "OOO" || "" + arr[1][0] + arr[1][1] + arr[1][2] == "OOO" ||
        "" + arr[2][0] + arr[2][1] + arr[2][2] == "OOO" || "" + arr[0][0] + arr[1][0] + arr[2][0] == "OOO" ||
        "" + arr[0][1] + arr[1][1] + arr[2][1] == "OOO" || "" + arr[0][2] + arr[1][2] + arr[2][2] == "OOO" ||
        "" + arr[0][0] + arr[1][1] + arr[2][2] == "OOO" || "" + arr[0][2] + arr[1][1] + arr[2][0] == "OOO") {
        x = 1
    }
    return x
}

fun winX(arr:MutableList<MutableList<Char>>):Int {
    var o = 0
    if ("" + arr[0][0] + arr[0][1] + arr[0][2] == "XXX" || "" + arr[1][0] + arr[1][1] + arr[1][2] == "XXX" ||
        "" + arr[2][0] + arr[2][1] + arr[2][2] == "XXX" || "" + arr[0][0] + arr[1][0] + arr[2][0] == "XXX" ||
        "" + arr[0][1] + arr[1][1] + arr[2][1] == "XXX" || "" + arr[0][2] + arr[1][2] + arr[2][2] == "XXX" ||
        "" + arr[0][0] + arr[1][1] + arr[2][2] == "XXX" || "" + arr[0][2] + arr[1][1] + arr[2][0] == "XXX") {
        o = 1
    }
    return o
}

fun printField(arr:MutableList<MutableList<Char>>) {
    print("\u001b[H\u001b[2J")
    println("---------")
    for (i in 0..8) {
        if (i % 3 == 0) {
            print("|")
        }
        print(" ${arr[i/3][i%3]}")
        if ((i + 1) % 3 == 0) {
            print(" |\n")
        }
    }
    println("---------")
}

fun conditionTurn(arr:MutableList<MutableList<Char>>, turn:Int) {
    var flag:Int
    do {
        val (coordX, coordY) = readln().split(" ")
        if (!coordX.all{char ->  char.isDigit()} || !coordY.all{char ->  char.isDigit()}) {
            flag = 0
            println("You should enter numbers!")
        } else {
            flag = 1
        }
        if (flag == 1 && (coordX.toInt() !in 1..3 || coordY.toInt() !in 1..3)) {
            flag = 0
            println("Coordinates should be from 1 to 3!")
        }
        if (flag == 1) {
            if (arr[coordX.toInt() - 1][coordY.toInt() - 1] != '_') {
                flag = 0
                println("This cell is occupied! Choose another one!")
            } else if (turn%2 == 0){
                arr[coordX.toInt() - 1][coordY.toInt() - 1] = 'X'
            } else {
                arr[coordX.toInt() - 1][coordY.toInt() - 1] = 'O'
            }
        }
    } while (flag == 0)
}

fun main() {
    val field = mutableListOf(MutableList(3) {'_'},MutableList(3) {'_'}, MutableList(3) {'_'})
    var turn = 0
    var endGame = 0
    printField(field)
    while(endGame == 0) {
        conditionTurn(field, turn)
        turn++
        printField(field)
        endGame = condition(field)
    }
}
