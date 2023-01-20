import java.lang.Exception

fun cinema(row:Int, seat:Int, kino:MutableList<MutableList<String>>) {
    val str = mutableListOf<String>(" ")
    for (j in 1..seat) {
        str.add("$j")
    }
    kino[0] = MutableList(str.size) {" "}
    for(k in 0 until str.size) {
        kino[0][k] = str[k]
    }
    for (i in 1..row) {
        str[0] = i.toString()
        for (j in 1..seat) {
            str[j] = "S"
        }
        kino.add(MutableList(str.size) {" "})
        for(k in 0 until str.size) {
            kino[i][k] = str[k]
        }
    }
}

fun printCinema(kino:MutableList<MutableList<String>>) {
    println("Cinema:")
    for (i in 0 until kino.size) {
        for (j in 0 until kino[i].size) {
            print(kino[i][j])
            if (j != kino[i].size - 1) {
                print(" ")
            }
        }
        print("\n")
    }
}
fun price(row:Int, seat:Int, myRow:Int):Int {
    val res = (when {
        row * seat <= 60 -> 10
        else -> {
            if (myRow > row / 2) {
                8
            } else {
                10
            }
        }
    }
            )
    return res
}

fun buyTicket(kino:MutableList<MutableList<String>>, row:Int, seat:Int) {
    var myRow = 0
    var mySeat = 0
    var ex = 1
    do {
        try {
            println("Enter a row number:")
            myRow = readln().toInt()
            println("Enter a seat number in that row:")
            mySeat = readln().toInt()
            if (myRow == 0 || mySeat == 0) {
                throw IndexOutOfBoundsException()
            }
            if (kino[myRow][mySeat] == "B") {
                println("That ticket has already been purchased!")
            } else {
                ex = 0
            }
        } catch (e:IndexOutOfBoundsException) {
            println("Wrong input!")
        }
    } while (ex == 1)
    println("Ticket price: $${price(row, seat, myRow)}")
    kino[myRow][mySeat] = "B"
}

fun statistic(kino: MutableList<MutableList<String>>, row:Int, seat:Int) {
    var numberSellTicket = 0
    var value = 0
    var valueMax = 0
    for (i in 1..row) {
        for (j in 1..seat) {
            val ticket = price(row, seat, i)
            if (kino[i][j] == "B") {
                numberSellTicket++
                value += ticket
            }
            valueMax += ticket
        }
    }
    println("Number of purchased tickets: $numberSellTicket")
    println("Percentage: ${"%.2f".format(numberSellTicket * 100.0 / (row * seat))}%")
    println("Current income: $$value")
    println("Total income: $$valueMax")
}

fun menu(row:Int, seat:Int, cinemaH:MutableList<MutableList<String>>):Int {
    println("\n1. Show the seats")
    println("2. Buy a ticket")
    println("3. Statistics")
    println("0. Exit")
    val num = readln().toInt()
    when (num) {
        1 -> printCinema(cinemaH)
        2 -> buyTicket(cinemaH, row, seat)
        3 -> statistic(cinemaH, row, seat)
    }
    return num
}

fun main() {
    val cinemaH = mutableListOf(mutableListOf<String>(" "))
    println("Enter the number of rows:")
    val row = readln().toInt()
    println("Enter the number of seats in each row:")
    val seat = readln().toInt()
    cinema(row, seat, cinemaH)
    while (menu(row, seat, cinemaH) != 0) {}
}