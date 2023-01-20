//print("\u001b[H\u001b[2J")
//print("\033c");
// kotlinc hello.kt -include-runtime -d hello.jar
// java -jar a.jar
import kotlin.math.abs

fun fieldStart(chess:Array<CharArray>) {
    for (row in chess.indices) {
        val num = 8
        if (row % 2 == 0) {
            chess[row] = "  +---+---+---+---+---+---+---+---+".toCharArray()
        } else if (row < 17){
            chess[row] = "${num - row/2} |   |   |   |   |   |   |   |   |".toCharArray()
        }
        chess[3] = "7 | B | B | B | B | B | B | B | B |".toCharArray()
        chess[13] = "2 | W | W | W | W | W | W | W | W |".toCharArray()
        chess[17] = "    a   b   c   d   e   f   g   h  ".toCharArray()
        println(chess[row])
    }
}

fun printField(chess:Array<CharArray>) {
    print("\u001b[H\u001b[2J")
    for (i in 0..17) {
        for (j in 0..34) {
            print(chess[i][j])
        }
        println()
    }
}

fun victory(chess:Array<CharArray>, index:Int):Int {
    var result = 1
    var flag_w = 0
    var flag_b = 0
    var pat = 1
    for (i in 3..13) {
        for (j in 0..34) {
            if (chess[i][j] == 'W') {
                flag_w = 1
                if (j > 3 && j < 31) {
                    if ((chess[i-2][j] == ' ' || chess[i-2][j - 4] == 'B' || chess[i-2][j + 4] == 'B') && index % 2 == 0) pat = 0
                } else if (j < 4) {
                    if ((chess[i-2][j] == ' ' || chess[i-2][j + 4] == 'B') && index % 2 == 0) pat = 0
                } else {
                    if ((chess[i-2][j] == ' ' || chess[i-2][j - 4] == 'B') && index % 2 == 0) pat = 0
                }
            }
            else if (chess[i][j] == 'B') {
                flag_b = 1
                if (j > 3 && j < 31) {
                    if ((chess[i+2][j] == ' ' || chess[i+2][j - 4] == 'W' || chess[i+2][j + 4] == 'W') && index % 2 == 1) pat = 0
                } else if (j < 4) {
                    if ((chess[i+2][j] == ' ' || chess[i+2][j + 4] == 'W') && index % 2 == 1) pat = 0
                } else {
                    if ((chess[i+2][j] == ' ' || chess[i+2][j - 4] == 'W') && index % 2 == 1) pat = 0
                }
            }
        }
    }
    if (chess[1].indexOf('W') != -1 || (flag_w == 1 && flag_b == 0)) {
        println("White Wins!")
        result = 0
    } else if (chess[15].indexOf('B') != -1 || (flag_w == 0 && flag_b == 1)) {
        println("Black Wins!")
        result = 0
    } else if (pat == 1) {
        println("Stalemate!")
        result = 0
    }
    return result
}

fun game(chess: Array<CharArray>, first: String, second: String) {
    var input: String
    var index = 0
    val regex = Regex("[a-h][1-8][a-h][1-8]")
    var flag = 1
    var coord1 = ' '
    var coord2 = '0'
    while (flag == 1 && victory(chess, index) == 1) {
        val nut = index % 2
        println(if (nut == 0) "$first's turn:" else "$second's turn:")
        input = readln()
        if (input == "exit") flag = 0
        else if (!input.matches(regex)) println("Invalid Input")
        else if (chess[16 - 2 * (input[1] - '0') + 1][chess[17].indexOf(input[0])] != (if (nut == 0) 'W' else 'B'))
            println("No ${if (nut == 0) "white" else "black"} pawn at ${input[0]}${input[1]}")
        else if (nut == 0 && ((input[1].code - input[3].code) > -1 || (input[1].code - input[3].code) < -2 ||
                    ((input[1].code - input[3].code) < -1 && ((input[1] - '0') != 2 && (input[1] - '0') != 7)))) println("Invalid Input")
        else if (nut == 1 && ((input[1].code - input[3].code) < 1 || (input[1].code - input[3].code) > 2 ||
                    ((input[1].code - input[3].code) > 1 && ((input[1] - '0') != 2 && (input[1] - '0') != 7)))) println("Invalid Input")
        else if ((16 - 2 * (input[1] - '0') + 1) == (16 - 2 * (coord2 - '0') + 1) && coord1 == input[2] && abs(input[2].code - input[0].code) < 2) {
            chess[16 - 2 * (input[1] - '0') + 1][chess[17].indexOf(input[0])] = ' '
            chess[16 - 2 * (coord2 - '0') + 1][chess[17].indexOf(coord1)] = ' '
            chess[16 - 2 * (input[3] - '0') + 1][chess[17].indexOf(input[2])] = (if (nut == 0) 'W' else 'B')
            index++
            printField(chess)
            coord1 = ' '
            coord2 = '0'
        }
        else if ((abs(input[0].code - input[2].code) > 0 && chess[16 - 2 * (input[3] - '0') + 1][chess[17].indexOf(input[2])] != if (nut == 0) 'B' else 'W') ||
            abs(input[0].code - input[2].code) > 1 || (input[0] == input[2] && chess[16 - 2 * (input[3] - '0') + 1][chess[17].indexOf(input[2])] != ' ')) println("Invalid Input")
        else {
            chess[16 - 2 * (input[1] - '0') + 1][chess[17].indexOf(input[0])] = ' '
            chess[16 - 2 * (input[3] - '0') + 1][chess[17].indexOf(input[2])] = (if (nut == 0) 'W' else 'B')
            index++
            printField(chess)
            if (abs(input[1].code - input[3].code) > 1) {
                coord1 = input[2]
                coord2 = input[3]
            } else {
                coord1 = ' '
                coord2 = '0'
            }
        }
    }
}

fun main() {
    val chessField = Array(18) { CharArray(35) { ' ' } }
    println("Pawns-Only Chess")
    println("First Player's name:")
    val firstName = readln()
    println("Second Player's name:")
    val secondName = readln()
    print("\u001b[H\u001b[2J")
    fieldStart(chessField)
    game(chessField, firstName, secondName)
    println("Bye!")
}