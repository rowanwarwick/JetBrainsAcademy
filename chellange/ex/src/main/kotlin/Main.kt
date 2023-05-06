fun main() {
    println("Enter the number of people:")
    val size = readln().toInt()
    println("Enter all people:")
    val list = MutableList(size) { readln().split(" ").map { it.trim() } }
    println("Enter the number of search queries:")
    val userNumber = readln().toInt()
    for (query in 0 until userNumber) {
        println("Enter data to search people:")
        val user = readln().lowercase()
        var check = false
        for (elem in list) {
            for (unit in elem) {
                if (unit.lowercase().contains(user)) {
                    println(elem.joinToString(" "))
                    check = true
                    break
                }
            }
        }
        if (!check) println("No matching people found.")
    }
}