data class Car(val number: String, val color: String)

var spots = mutableMapOf<Int, Car?>()
fun getOccupied() = spots.filterValues { it != null }
fun getFirstFreeSpot() = spots.filterValues { it == null }.minOf { it.key }

fun main() {
    while (true) {
        val input = readln().split(" ")
        if (input[0] !in "createxit" && spots.isEmpty()) println("Sorry, a parking lot has not been created.")
        else when (input[0]) {
            "create" ->
                spots = mutableMapOf<Int, Car?>().apply { (1..input[1].toInt()).forEach { this[it] = null } }
                    .also { println("Created a parking lot with ${input[1]} spots.") }
            "park" ->
                if (getOccupied().size == spots.size) println("Sorry, the parking lot is full.")
                else getFirstFreeSpot().also { spots[it] = Car(input[1], input[2]) }
                    .also { println("${spots[it]?.color} car parked in spot $it.") }
            "status" ->
                if (getOccupied().isEmpty()) println("Parking lot is empty.")
                else getOccupied().forEach { println("${it.key} ${it.value?.number} ${it.value?.color}") }
            "reg_by_color" ->
                getOccupied().filter { it.value?.color.equals(input[1], true) }.map { it.value?.number }.joinToString()
                    .let { println(it.ifBlank { "No cars with color ${input[1]} were found." }) }
            "spot_by_reg" ->
                getOccupied().filter { spots[it.key]?.number.equals(input[1], true) }.map { it.key }.joinToString()
                    .let { println(it.ifBlank { "No cars with registration number ${input[1]} were found." }) }
            "spot_by_color" ->
                getOccupied().filter { spots[it.key]?.color.equals(input[1], true) }.map { it.key }.joinToString()
                    .let { println(it.ifBlank { "No cars with color ${input[1]} were found." }) }
            "leave" -> println("Spot ${input[1]} is free.").also { spots[input[1].toInt()] = null }
            "exit" -> return
        }
    }
}