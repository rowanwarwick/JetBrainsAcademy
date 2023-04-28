package contacts

import java.time.LocalDateTime
import kotlin.reflect.full.declaredMemberProperties

abstract class BaseClass(phones: String) {
    open val createdTime: LocalDateTime = LocalDateTime.now()
    open var editTime: LocalDateTime? = null

    open var phone = validNumber(phones)
        set(value) {
            field = validNumber(value)
        }

    private fun validNumber(sample: String) =
        if (sample.matches(Regex("\\+?(\\(?[a-zA-Z0-9]\\)?)+([- ]?([a-zA-Z0-9]){2,})?([- ]?([a-zA-Z0-9]){2,})*")) ||
            sample.matches(Regex("\\+?([a-zA-Z0-9])+([- ]?(\\(?[a-zA-Z0-9]\\)?){2,})?([- ]?([a-zA-Z0-9]){2,})*"))
        ) {
            sample
        } else {
            println("Wrong number format!")
            ""
        }

    open fun searchQuery(query: String):Boolean {
        val params = this::class.declaredMemberProperties
        for (param in params) {
            if (param.call(this).toString().contains(query, true)) return true
        }
        return false
    }

    abstract fun print()
    abstract fun edit()
}

class Person(
    var name: String,
    var surname: String,
    var birth: String,
    var gender: String,
    override var phone: String
) : BaseClass(phone) {

    override fun toString() = "$name $surname"

    override fun edit() {
        print("Select a field (name, surname, birth, gender, number):")
        val field = readln()
        when (field) {
            "name" -> name = readln()
            "surname" -> surname = readln()
            "number" -> phone = readln()
            "birth" -> birth = readln()
            "gender" -> gender = readln()
        }
        if (field in listOf("name", "surname", "number", "birth", "gender")) {
            editTime = LocalDateTime.now()
            println("The record updated!")
        } else println("Invalid input")
    }

    override fun print() {
        println(
            """Name: $name
Surname: $surname
Birth date: ${birth.ifEmpty { "[no data]" }}
Gender: ${gender.ifEmpty { "[no data]" }}
Number: $phone
Time created: $createdTime
Time last edit: $editTime
"""
        )
    }

    companion object {
        fun add(): Person {
            print("Enter the name of the person:")
            val name = readln()
            print("Enter the surname of the person:")
            val surname = readln()
            print("Enter the birth date:")
            val birthDay = readln()
            if (birthDay.isEmpty()) println("Bad birth date!")
            print("Enter the gender (M, F):")
            var gender = readln().uppercase()
            if (gender !in listOf("M", "F")) gender = ""
            if (gender.isEmpty()) println("Bad gender!")
            print("Enter the number:")
            val phone = readln()
            return Person(name, surname, birthDay, gender, phone)
        }
    }
}

class Organization(var name: String, var address: String, override var phone: String) : BaseClass(phone) {

    override fun toString() = name

    override fun edit() {
        print("Select a field (address, number):")
        val field = readln()
        when (field) {
            "address" -> address = readln()
            "number" -> phone = readln()
        }
        if (field in listOf("address", "number")) {
            editTime = LocalDateTime.now()
            println("The record updated!")
        } else println("Invalid input")
    }

    override fun print() {
        println(
            """Organization name: $name
Address: $address
Number: $phone
Time created: $createdTime
Time last edit: $editTime
"""
        )
    }

    companion object {
        fun add(): Organization {
            print("Enter the organization name:")
            val name = readln()
            print("Enter the address:")
            val address = readln()
            print("Enter the number:")
            val phone = readln()
            return Organization(name, address, phone)
        }
    }
}

object Contacts {
    private val contacts = mutableListOf<BaseClass>()

    fun addContact() {
        print("Enter the type (person, organization):")
        when (readln().lowercase()) {
            "person" -> contacts.add(Person.add()).also { println("The record added.") }
            "organization" -> contacts.add(Organization.add()).also { println("The record added.") }
            else -> println("Invalid input")
        }
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
    }

    fun count() {
        println("The Phone Book has ${contacts.size} records.")
    }
}

fun main() {
    do {
        print("[menu] Enter action (add, list, search, count, exit):")
        val user = readln()
        when (user) {
            "add" -> Contacts.addContact()
            "list" -> Contacts.list()
            "search" -> Contacts.search()
            "count" -> Contacts.count()
        }
        println()
    } while (user != "exit")
}