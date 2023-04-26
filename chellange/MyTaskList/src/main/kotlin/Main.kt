import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

fun main() {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val type = Types.newParameterizedType(MutableList::class.java, Task::class.java)
    val adapter = moshi.adapter<MutableList<Task>>(type)
    if (File(System.getProperty("user.dir") + File.separator + "tasklist.json").exists()) {
        val fromFile =
            adapter.fromJson(File(System.getProperty("user.dir") + File.separator + "tasklist.json").readText())
        if (fromFile != null) TaskList.list.addAll(fromFile)
    }
    do {
        println("Input an action (add, print, edit, delete, end):")
        val user = readln().lowercase()
        when (user) {
            "add" -> TaskList.addTask()

            "print" -> {
                if (TaskList.list.isEmpty()) println("No tasks have been input")
                else TaskList.printTask()
            }

            "delete" -> {
                TaskList.printTask()
                if (TaskList.list.isEmpty()) println("No tasks have been input")
                else TaskList.deleteTask()
            }

            "edit" -> {
                TaskList.printTask()
                if (TaskList.list.isEmpty()) println("No tasks have been input")
                else TaskList.editTask()
            }

            "end" -> {
                File(System.getProperty("user.dir") + File.separator + "tasklist.json").writeText(
                    adapter.toJson(
                        TaskList.list
                    )
                )
                println("Tasklist exiting!")
            }

            else -> println("The input action is invalid")
        }
    } while (user != "end")
    if (TaskList.list.isEmpty()) {
        println("No tasks have been input")
    }
}