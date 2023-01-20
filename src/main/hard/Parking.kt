fun main() {
    var ourParking = Parking(0)
    do {
        val command = readln().split(' ').toMutableList()
        when (command[0]) {
            "park" -> ourParking.park(command.subList(1, 3))
            "leave" -> ourParking.leave(command[1].toInt())
            "create" -> {
                ourParking = Parking(command[1].toInt())
                ourParking.create = 1
                println("Created a parking lot with ${command[1].toInt()} spots.")
            }
            "status" -> ourParking.status()
            "spot_by_color" -> ourParking.color(command[1])
            "reg_by_color" -> ourParking.colorNum(command[1])
            "spot_by_reg" -> ourParking.reg(command[1])
        }
    } while (command[0] != "exit")
}

class Parking(size:Int) {
    val place = MutableList(size) {mutableListOf<String>()}
    var create = 0
    var check = 0
    fun park(car:MutableList<String>) {
        if (this.create != 0) {
            for (i in place.indices) {
                if (place[i].isEmpty()) {
                    place[i] = car
                    this.check++
                    println("${car[1]} car parked in spot ${i + 1}.")
                    return
                }
            }
            println("Sorry, the parking lot is full.")
        } else {
            println("Sorry, a parking lot has not been created.")
        }
    }
    fun leave(car:Int) {
        if (this.create != 0) {
            if (place[car - 1].isEmpty()) {
                println("There is no car in spot $car.")
            } else {
                println("Spot $car is free.")
                place[car - 1] = mutableListOf()
                this.check--
            }
        } else {
            println("Sorry, a parking lot has not been created.")
        }
    }
    fun status() {
        if (this.check != 0)
            for (i in place.indices) {
                if (place[i].isNotEmpty()) println("${i+1} ${place[i][0]} ${place[i][1]}")
            }
        else
            println(if (this.create != 0) "Parking lot is empty." else "Sorry, a parking lot has not been created.")
    }
    fun color(c:String) {
        if (this.create != 0) {
            var value = 0
            for (i in place.indices) {
                if (place[i].isNotEmpty() && place[i][1].lowercase() == c.lowercase()) {
                    print(if (value == 0) "${i + 1}" else ", ${i + 1}")
                    value++
                }
            }
            println(if (value == 0) "No cars with color $c were found." else "")
        } else {
            println("Sorry, a parking lot has not been created.")
        }
    }
    fun colorNum(c:String) {
        if (this.create != 0) {
            var value = 0
            for (i in place.indices) {
                if (place[i].isNotEmpty() && place[i][1].lowercase() == c.lowercase()) {
                    print(if (value == 0) place[i][0] else ", ${place[i][0]}")
                    value++
                }
            }
            println(if (value == 0) "No cars with color $c were found." else "")
        } else {
            println("Sorry, a parking lot has not been created.")
        }
    }
    fun reg(c:String) {
        if (this.create != 0) {
            var value = 0
            for (i in place.indices) {
                if (place[i].isNotEmpty() && place[i][0] == c) {
                    print(if (value == 0) "${i + 1}" else ", ${i + 1}")
                    value++
                }
            }
            println(if (value == 0) "No cars with registration number $c were found." else "")
        } else {
            println("Sorry, a parking lot has not been created.")
        }
    }
}