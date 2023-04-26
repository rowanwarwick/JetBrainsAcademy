import kotlinx.datetime.*

object TaskList {
    val list = mutableListOf<Task>()

    fun addTask() {
        val priority = priorityTask()
        val date = dateTask()
        val time = timeTask()
        val task = createNewTask()
        if (task.isNotEmpty()) list.add(Task(date, time, color(priority), task))
    }

    fun printTask() {
        if (list.isNotEmpty()) {
            println("+----+------------+-------+---+---+--------------------------------------------+")
            println("| N  |    Date    | Time  | P | D |                   Task                     |")
            println("+----+------------+-------+---+---+--------------------------------------------+")
        }
        val copyList = list.toMutableList()
        var index = 1
        copyList.forEach { task ->
            val indexString = index.toString()
            val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
            val due = currentDate.daysUntil(LocalDate.parse(task.date))
            val d = when {
                due == 0 -> color("T")
                due > 0 -> color("I")
                else -> color("O")
            }
            val splitTask = task.task.split("\n")
            var firstString = true
            for (string in splitTask) {
                val subString = string.chunked(44)
                for (sub in subString) {
                    if (firstString) {
                        println(
                            "| ${indexString}${" ".repeat(3 - indexString.length)}| ${task.date} " +
                                    "| ${task.time} | ${task.priority} | $d " +
                                    "|${sub}${" ".repeat(44 - sub.length)}|"
                        )
                        firstString = false
                    } else {
                        println("|    |            |       |   |   |${sub}${" ".repeat(44 - sub.length)}|")
                    }
                }
            }
            println("+----+------------+-------+---+---+--------------------------------------------+")
            index++
        }
    }

    fun deleteTask() {
        do {
            println("Input the task number (1-${list.size}):")
            var user = readln().toIntOrNull()
            if (user != null && (user > list.size || user < 1)) user = null
            if (user != null) {
                list.removeAt(user - 1)
                println("The task is deleted")
            } else {
                println("Invalid task number")
            }
        } while (user == null)
    }

    fun editTask() {
        var numberTask: Int?
        var fieldEdit: String?
        do {
            println("Input the task number (1-${list.size}):")
            numberTask = readln().toIntOrNull()
            if (numberTask != null && (numberTask > list.size || numberTask < 1)) numberTask = null
            if (numberTask == null) println("Invalid task number")
            else numberTask--
        } while (numberTask == null)
        do {
            println("Input a field to edit (priority, date, time, task):")
            fieldEdit = readln().lowercase()
            when (fieldEdit) {
                "priority" -> list[numberTask].priority = color(priorityTask())
                "date" -> list[numberTask].date = dateTask()
                "time" -> list[numberTask].time = timeTask()
                "task" -> list[numberTask].task = createNewTask()
                else -> println("Invalid field")
            }
        } while (fieldEdit !in listOf("priority", "date", "time", "task"))
        println("The task is changed")
    }

    private fun color(input: String?): String {
        return when (input) {
            "C", "O" -> "\u001B[101m \u001B[0m"
            "H", "T" -> "\u001B[103m \u001B[0m"
            "N", "I" -> "\u001B[102m \u001B[0m"
            "L" -> "\u001B[104m \u001B[0m"
            else -> " "
        }
    }

    private fun priorityTask(): String {
        println("Input the task priority (C, H, N, L):")
        var user = readln().uppercase()
        while (user !in listOf("C", "H", "N", "L")) {
            println("Input the task priority (C, H, N, L):")
            user = readln().uppercase()
        }
        return user
    }

    private fun dateTask(): String {
        var date: LocalDate? = null
        do {
            println("Input the date (yyyy-mm-dd):")
            try {
                val user = readln()
                val num = Regex("\\d+").findAll(user).map { it.value.toInt() }.toMutableList()
                if (user.matches("(\\d){1,4}-\\d{1,2}-\\d{1,2}".toRegex()) && num.size == 3) date =
                    LocalDate(num[0], num[1], num[2])
                else println("The input date is invalid")
            } catch (_: IllegalArgumentException) {
                println("The input date is invalid")
            }
        } while (date == null)
        return date.toString()
    }

    private fun timeTask(): String {
        var dateTime: LocalDateTime? = null
        do {
            println("Input the time (hh:mm):")
            try {
                val user = readln()
                val num = Regex("\\d+").findAll(user).map { it.value.toInt() }.toMutableList()
                if (user.matches("(\\d){1,2}:\\d{1,2}".toRegex()) && num.size == 2) dateTime =
                    LocalDateTime(1900, 1, 1, num[0], num[1])
                else println("The input time is invalid")
            } catch (_: IllegalArgumentException) {
                println("The input time is invalid")
            }
        } while (dateTime == null)
        return dateTime.toString().dropWhile { it != 'T' }.drop(1)
    }

    private fun createNewTask(): String {
        val newTask = mutableListOf<String>()
        println("Input a new task (enter a blank line to end):")
        do {
            val user = readln().trim()
            if (user.isNotEmpty()) newTask.add(user)
            else if (newTask.isEmpty()) println("The task is blank")
        } while (user.isNotEmpty())
        return newTask.joinToString("\n")
    }
}
