package contacts

import java.time.LocalDateTime

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
            editTime = LocalDateTime.now().toString()
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