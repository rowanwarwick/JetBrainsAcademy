class CoffeeMachine() {
    var mlWater = 400
    var mlMilk = 540
    var gCoffee = 120
    var dCup = 9
    var dollar = 550
    fun start() {
        var action = ""
        do {
            println("Write action (buy, fill, take, remaining, exit):")
            action = this.menu()
            when (action) {
                "buy" -> buy()
                "fill" -> fill()
                "take" -> take()
                "remaining" -> remaining()
            }
        } while (action != "exit")
    }
    private fun menu():String = readln()
    private fun remaining() {
        println("""
        The coffee machine has:
        ${this.mlWater} ml of water
        ${this.mlMilk} ml of milk
        ${this.gCoffee} g of coffee beans
        ${this.dCup} disposable cups
        $${this.dollar} of money""".trimIndent())
    }
    private fun buy() {
        println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
        when (this.menu()) {
            "1" -> changeIngredient(250, 0, 16, 1, 4)
            "2" -> changeIngredient(350, 75, 20, 1, 7)
            "3" -> changeIngredient(200, 100, 12, 1, 6)
        }
    }
    private fun changeIngredient(water:Int, milk:Int, coffee:Int, cup:Int, money:Int) {
        if (this.mlWater < water || this.mlMilk < milk || this.gCoffee < coffee || this.dCup < cup) {
            if (this.mlWater < water)
                println("Sorry, not enough water!")
            if (this.mlMilk < milk)
                println("Sorry, not enough milk!")
            if (this.gCoffee < coffee)
                println("Sorry, not enough coffee!")
            if (this.dCup < cup)
                println("Sorry, not enough cup!")
        } else {
            this.mlWater -= water
            this.mlMilk -= milk
            this.gCoffee -= coffee
            this.dCup -= cup
            this.dollar += money
            println("I have enough resources, making you a coffee!")
        }
    }
    private fun fill() {
        println("Write how many ml of water you want to add: ")
        this.mlWater += readln().toInt()
        println("Write how many ml of milk you want to add: ")
        this.mlMilk += readln().toInt()
        println("Write how many grams of coffee beans you want to add: ")
        this.gCoffee += readln().toInt()
        println("Write how many disposable cups you want to add: ")
        this.dCup += readln().toInt()
    }
    private fun take() {
        println("I gave you $${this.dollar}")
        this.dollar = 0
    }
}

fun main() {
    val coffeMachine = CoffeeMachine()
    coffeMachine.start()
}