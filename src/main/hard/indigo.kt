import java.lang.Exception
import java.util.Collections.shuffle

class Card (val rang:String, val suite:String) {
    override fun toString() = rang + suite
}

class Deck() {
    val rang = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
    val suite = arrayOf("♠", "♥", "♦", "♣")
    val point = arrayOf("A", "J", "Q", "K", "10")
    val cards = suite.flatMap{suit -> rang.map { rang -> Card(rang, suit) } }.toMutableList()
    init {shuffle(cards)}
    var cardOnTable = 4
    val tableCard = cards.take(cardOnTable).toMutableList().also { cards.removeAll(cards.take(cardOnTable).toMutableList()) }
    var topCard = tableCard[cardOnTable - 1]
    val cardOnPlayer = 6
    val cardPlayer = cards.take(cardOnPlayer).toMutableList().also { cards.removeAll(cards.take(cardOnPlayer).toMutableList()) }
    val cardComp = cards.take(cardOnPlayer).toMutableList().also { cards.removeAll(cards.take(cardOnPlayer).toMutableList()) }
    var outCard = cardOnTable
}

class Players() {
    val ourDeck = Deck()
    var score1 = 0
    var score2 = 0
    var card1 = 0
    var card2 = 0
    var lastWinner = -1
    fun gameMode(card:MutableList<Card>, numberPlayer:Int):Pair<Int, String> {
        var menu = ""
        var turn = numberPlayer
        if (card.size == 0) {
            card += ourDeck.cards.take(6)
            ourDeck.cards.removeAll(card)
        }
        println(if (ourDeck.cardOnTable != 0) "${ourDeck.cardOnTable} cards on the table, and the top card is ${ourDeck.topCard}" else "No cards on the table")
        if (numberPlayer == 0) println("Cards in hand: ${card.joinToString(separator = " ") { "${card.indexOf(it) + 1})$it" }}")
        do {
            var number = 1
            if (numberPlayer == 0) {
                println("Choose a card to play (1-${card.size}):")
                menu = readln().lowercase()
                number = try {
                    menu.toInt()
                } catch (e: Exception) {
                    0
                }
            } else {
                val candidate = card.filter{it.rang == ourDeck.topCard.rang || it.suite == ourDeck.topCard.suite}
                if (candidate.size == 1) {
                    number = card.indexOf(card.find { it == candidate[0] }) + 1
                } else if (ourDeck.cardOnTable == 0 || (ourDeck.cardOnTable != 0 && candidate.size == 0)) {
                    number = partCompLogic(card, card)
                } else if (candidate.size > 1) {
                    number = partCompLogic(candidate, card)
                }
            }
            if (number in 1..6) {
                ourDeck.cardOnTable++
                ourDeck.tableCard += card[number - 1]
                if (numberPlayer == 1) println("${card.joinToString(" ")}\nComputer plays ${card[number - 1]}")
                if (card[number - 1].rang == ourDeck.topCard.rang || card[number - 1].suite == ourDeck.topCard.suite) {
                    val count = ourDeck.tableCard.count { it.rang in ourDeck.point }
                    if (numberPlayer == 0) {
                        score1 += count
                        card1 += ourDeck.cardOnTable
                        lastWinner = 0
                    } else {
                        score2 += count
                        card2 += ourDeck.cardOnTable
                        lastWinner = 1
                    }
                    println("${if (numberPlayer == 0) "Player" else "Computer"} wins cards")
                    println("Score: Player $score1 - Computer $score2")
                    println("Cards: Player $card1 - Computer $card2")
                    ourDeck.cardOnTable = 0
                    ourDeck.tableCard.clear()
                    ourDeck.topCard = Card("", "")
                } else {
                    ourDeck.topCard = card[number - 1]
                }
                turn++
                ourDeck.outCard++
                card.remove(card[number - 1])
            }
        } while ((number > 6 || number < 1) && menu != "exit")
        if (menu == "exit") println("Game Over")
        return Pair(turn, menu)
    }

    private fun partCompLogic(card1:List<Card>, card2:List<Card>):Int {
        var number = 1
        if (card1.groupBy { it.suite }.filter{it.value.size > 1}.isNotEmpty()) {
            val randomCard = card1.groupBy { it.suite }.filter { it.value.size > 1 }.values.random().random()
            number = card2.indexOf(card2.find { it == randomCard }) + 1
        } else if (card1.groupBy { it.rang }.filter{it.value.size > 1}.isNotEmpty()) {
            val randomCard = card1.groupBy { it.rang }.filter { it.value.size > 1 }.values.random().random()
            number = card2.indexOf(card2.find { it == randomCard }) + 1
        } else {
            number = card2.indexOf(card2.find { it == card1.random() }) + 1
        }
        return number
    }
}

class Game(){
    var menu = ""
    var turn:Int = 0
    val players = Players()
    fun game() {
        println("Indigo Card Game\nPlay first?")
        do {
            menu = readln().lowercase()
            when (menu) {
                "yes" -> turn = 0
                "no" -> turn = 1
                "exit" -> menu = "exit"
                else -> println("Play first?")
            }
        } while (menu != "yes" && menu != "no" && menu != "exit")
        if (menu == "exit") println("Game Over")
        else {
            println("Initial cards on the table:" + players.ourDeck.tableCard.joinToString(" "))
            do {
                val (turnNext, menu) = players.gameMode(if (turn % 2 == 0) players.ourDeck.cardPlayer else players.ourDeck.cardComp, turn % 2)
                turn = turnNext
                if (players.ourDeck.outCard == 52) {
                    println(if (players.ourDeck.cardOnTable != 0) "${players.ourDeck.cardOnTable} cards on the table, and the top card is ${players.ourDeck.topCard}" else "No cards on the table")
                    if (players.ourDeck.tableCard.isNotEmpty()) {
                        if (players.lastWinner == 0 || (players.lastWinner == -1 && turn % 2 == 0)) {
                            players.score1 += players.ourDeck.tableCard.count { it.rang in players.ourDeck.point }
                            players.card1 += players.ourDeck.cardOnTable
                        } else {
                            players.score2 += players.ourDeck.tableCard.count { it.rang in players.ourDeck.point }
                            players.card2 += players.ourDeck.cardOnTable
                        }
                    }
                    if (players.card1 > players.card2 || (players.card1 == players.card2 && turn % 2 == 0)) players.score1 += 3
                    else players.score2 += 3
                    println("Score: Player ${players.score1} - Computer ${players.score2}")
                    println("Cards: Player ${players.card1} - Computer ${players.card2}")
                    println("Game Over")
                }
            } while (players.ourDeck.outCard != 52 && menu != "exit")
        }
    }
}

fun main() {
    Game().game()
}