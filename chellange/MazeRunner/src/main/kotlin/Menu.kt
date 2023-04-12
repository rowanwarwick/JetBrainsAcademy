import java.io.File
import java.io.FileReader
import java.io.FileWriter

object Menu {
    private lateinit var field: Field
    private var isGeneratedOrLoad = false

    private fun menu() {
        println("=== Menu ===")
        println("1. Generate a new maze")
        println("2. Load a maze")
        if (isGeneratedOrLoad) println("3. Save the maze")
        if (isGeneratedOrLoad) println("4. Display the maze")
        if (isGeneratedOrLoad) println("5. Find the escape")
        println("0. Exit")
    }

    private fun genLab() {
        println("Please, enter the size of a maze more then 5")
        val size = readln().toInt()
        if (size > 5) {
            field = Field(size)
            field.generateLab()
            println(field)
            isGeneratedOrLoad = true
        } else {
            println("size very small")
        }
    }

    private fun loadLab() {
        val nameFile = readln()
        if (File(nameFile).exists()) {
            FileReader(nameFile).use {
                field = Field(it.readText())
                isGeneratedOrLoad = true
            }
        } else {
            println("The file $nameFile does not exist")
        }
    }

    private fun saveLab() {
        val nameFile = readln()
        if (nameFile.isNotEmpty()) {
            FileWriter(nameFile).use { it.write(field.toString()) }
        } else {
            println("Incorrect name file. Please try again;")
        }
    }

    fun start() {
        menu()
        var userEnter = readln().toIntOrNull()
        while (userEnter != 0) {
            when {
                userEnter == 1 -> genLab()
                userEnter == 2 -> loadLab()
                userEnter == 3 && isGeneratedOrLoad -> saveLab()
                userEnter == 4 && isGeneratedOrLoad -> print(field)
                userEnter == 5 && isGeneratedOrLoad -> field.findPath()
                userEnter != 0 -> println("Incorrect option. Please try again;")
            }
            if (userEnter != 0) {
                menu()
                userEnter = readln().toIntOrNull()
            }
        }
        println("Bye!")
    }
}
