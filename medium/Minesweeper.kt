import java.lang.Exception
import kotlin.random.Random

class Cell() {
    var view = '.'
    var sign = 0
    var mine = '.'
}

class Field {
    private val square = 9

    fun game() {
        val field = Array(square + 3) {Array (square + 3) {Cell()} }
        println("How many mines do you want on the field?")
        val mine = readln().toInt()
        printField(field)
        var first = 1
        do {
            val mineOnField = if (first == 1) 1 else monitoring(field)
            try {
                if (mineOnField > 0) {
                    println("Set/unset mine marks or claim a cell as free:")
                    val (coordX, coordY, action) = readln().split(' ')
                    val y = coordX.toInt() + 1
                    val x = coordY.toInt() + 1
                    if (x < 2 || x > 10 || y < 2 || y > 10) throw Exception("out of range")
                    if (action == "mine") {
                        if (field[x][y].view == '*') field[x][y].view = '.'
                        else if (field[x][y].view == '.') field[x][y].view = '*'
                    } else if (action == "free") {
                        if (first == 1) {
                            setMine(x, y, mine, field)
                            first = 2
                        }
                        else free(x, y, field)
                    }
                    else throw Exception()
                    printField(field)
                } else if (mineOnField == 0) println("Congratulations! You found all the mines!")
                else println("You stepped on a mine and failed!")

            } catch (e:Exception) {
                println("Incorrect input")
            }
        } while (mineOnField > 0)
    }

    private fun free(x:Int, y:Int, field:Array<Array<Cell>>) {
        if (field[x][y].mine == 'X') field[x][y].view = 'X'
        else if (field[x][y].sign != 0) field[x][y].view = (field[x][y].sign + 48).toChar()
        else if (field[x][y].sign == 0 && (field[x][y].view == '.' || field[x][y].view == '*')) {
            field[x][y].view = '/'
            free(x - 1, y - 1, field)
            free(x, y - 1, field)
            free(x + 1, y - 1, field)
            free(x - 1, y, field)
            free(x + 1, y, field)
            free(x - 1, y + 1, field)
            free(x, y + 1, field)
            free(x + 1, y + 1, field)
        }
    }

    private fun monitoring(field:Array<Array<Cell>>):Int {
        var result = 0
        loop@for (i in 2 until square + 2) {
            for (j in 2 until square + 2) {
                if (field[i][j].mine == 'X' && field[i][j].view == '.') result++
                if (field[i][j].mine == '.' && field[i][j].view == '*') result++
                if (field[i][j].mine == 'X' && field[i][j].view == 'X') {
                    result = -1
                    break@loop
                }
            }
        }
        return result
    }

    private fun setMine(xF:Int, yF:Int, mine:Int, field:Array<Array<Cell>>) {
        var mineField = 0
        while (mineField != mine) {
            val x = Random.nextInt(2, square + 2)
            val y = Random.nextInt(2, square + 2)
            if (field[x][y].mine != 'X' && x != xF && y != yF) {
                mineField++
                field[x][y].mine = 'X'
            }
        }
        numbers(field)
        free(xF, yF, field)
    }

    private fun numbers(field:Array<Array<Cell>>) {
        for (i in 2 until square + 2) {
            for (j in 2 until square + 2) {
                if (field[i][j].mine == '.') {
                    val cell1 = if ((i - 1) < 2 || (j - 1) < 2) 0 else if (field[i - 1][j - 1].mine == 'X') 1 else 0
                    val cell2 = if ((j - 1) < 2) 0 else if (field[i][j - 1].mine == 'X') 1 else 0
                    val cell3 = if ((i + 1) > (square + 1) || (j - 1) < 2) 0 else if (field[i + 1][j - 1].mine == 'X') 1 else 0
                    val cell4 = if ((i - 1) < 2) 0 else if (field[i - 1][j].mine == 'X') 1 else 0
                    val cell5 = if ((i + 1) > (square + 1)) 0 else if (field[i + 1][j].mine == 'X') 1 else 0
                    val cell6 = if ((i - 1) < 2 || (j + 1) > (square + 1)) 0 else if (field[i - 1][j + 1].mine == 'X') 1 else 0
                    val cell7 = if ((j + 1) > (square + 1)) 0 else if (field[i][j + 1].mine == 'X') 1 else 0
                    val cell8 = if ((i + 1) > (square + 1) || (j + 1) > (square + 1)) 0 else if (field[i + 1][j + 1].mine == 'X') 1 else 0
                    field[i][j].sign = cell1 + cell2 + cell3 + cell4 + cell5 + cell6 + cell7 + cell8
                }
            }
        }
    }

    private fun printField(field:Array<Array<Cell>>) {
        print("\u001b[H\u001b[2J")
        field[0][0].view = ' '
        for (i in 2 until square + 2) {
            field[i][0].view = (i + 47).toChar()
            field[0][i].view = (i + 47).toChar()
        }
        for (i in 0 until square + 3) {
            field[1][i].view = '-'
            field[square + 2][i].view = '-'
            field[i][1].view = '|'
            field[i][square + 2].view = '|'
        }
        field[1][square + 2].view = '|'
        for (i in 0 until square + 3) {
            for (j in 0 until square + 3)
                print(field[i][j].view)
            println()
        }
    }
}

fun main () {
    val myField = Field()
    myField.game()
}