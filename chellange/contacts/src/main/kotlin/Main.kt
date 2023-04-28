package contacts

fun main(args: Array<String>) {
    Contacts.loadDB(args.getOrNull(0))
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