package contacts

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

object Contacts {
    private val contacts = mutableListOf<BaseClass>()
    private val moshi = Moshi.Builder().add(
        PolymorphicJsonAdapterFactory.of(BaseClass::class.java, "contact")
            .withSubtype(Person::class.java, "person")
            .withSubtype(Organization::class.java, "org")
    ).add(KotlinJsonAdapterFactory()).build()
    private val type = Types.newParameterizedType(MutableList::class.java, BaseClass::class.java)
    private val adapter = moshi.adapter<MutableList<BaseClass>>(type)
    private var fileName: String? = null

    fun loadDB(name: String?) {
        fileName = name
        if (fileName != null && File(System.getProperty("user.dir") + File.separator + fileName).exists()) {
            adapter.fromJson(File(System.getProperty("user.dir") + File.separator + fileName).readText())
                ?.let { contacts.addAll(it) }
        } else {
            fileName = "Contacts.json"
        }
    }

    fun addContact() {
        print("Enter the type (person, organization):")
        when (readln().lowercase()) {
            "person" -> contacts.add(Person.add()).also { println("The record added.") }
            "organization" -> contacts.add(Organization.add()).also { println("The record added.") }
            else -> println("Invalid input")
        }
        File(System.getProperty("user.dir") + File.separator + fileName).writeText(adapter.toJson(contacts))
    }

    fun list(list: MutableList<BaseClass> = contacts) {
        for ((index, contact) in list.withIndex()) {
            println("${index + 1}. $contact")
        }
        print("\n[list] Enter action ([number], back):")
        val user = readln()
        when {
            user.toIntOrNull() != null && user.toInt() in 1..contacts.size -> {
                contacts[user.toInt() - 1].print()
                println()
                record(contacts[user.toInt() - 1])
            }

            user != "back" -> println("Invalid input")
        }
    }

    fun search() {
        do {
            print("Enter search query:")
            val query = readln()
            val resultSearch = mutableListOf<BaseClass>()
            for (element in contacts) {
                if (element.searchQuery(query)) resultSearch.add(element)
            }
            println("Found ${resultSearch.size} results:")
            for ((index, contact) in resultSearch.withIndex()) {
                println("${index + 1}. $contact")
            }
            print("[search] Enter action ([number], back, again):")
            val user = readln()
            when (user) {
                (1..resultSearch.size).toString() -> {
                    resultSearch[user.toInt() - 1].print()
                    record(resultSearch[user.toInt() - 1])
                }

                !in listOf("back", "again") -> println("Invalid input")
            }
        } while (user == "again")
    }

    private fun record(element: BaseClass) {
        print("[record] Enter action (edit, delete, menu):")
        when (readln()) {
            "edit" -> element.edit()
            "delete" -> contacts.remove(element).also { println("The record removed!") }
            !in listOf("edit", "delete", "menu") -> println("Invalid input")
        }
        File(System.getProperty("user.dir") + File.separator + fileName).writeText(adapter.toJson(contacts))
    }

    fun count() {
        println("The Phone Book has ${contacts.size} records.")
    }
}