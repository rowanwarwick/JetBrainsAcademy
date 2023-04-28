package contacts

import java.time.LocalDateTime

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
            editTime = LocalDateTime.now().toString()
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