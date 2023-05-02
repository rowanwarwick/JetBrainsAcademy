import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class DealMaker {
    private var random = Random()
    var newTransactions = LinkedList<Transaction>()
    private val transactionID = AtomicInteger(1)
    private val allTransactions =  mutableMapOf<Int, Transaction>()
    var accounts = mutableMapOf("Adam" to 0, "Bob" to 0, "Rob" to 0, "Dan" to 0, "Greg" to 0, "Liz" to 0, "Sam" to 0)

    fun makingTransactions() {
        newTransactions = LinkedList<Transaction>()
        var transfers = random.nextInt(10)
        while (transfers-- > 0) {
            val payers = accounts.entries.filter { it.value > 0 }.map { it.key }.toList()
            if (payers.isNotEmpty()) {
                val payer = payers[random.nextInt(payers.size)]
                val payerBalance = accounts[payer]!!
                val coinsAmount = random.nextInt(payerBalance) + 1
                val receiver = accounts.keys.filter { it != payer }.toList()[random.nextInt(accounts.size - 1)]
                val receiverBalance = accounts[receiver]!!
                val transaction = Transaction(transactionID.getAndIncrement(), payer, receiver, coinsAmount)
                newTransactions.add(transaction)
                allTransactions[transaction.id] = transaction
                accounts[payer] = payerBalance - coinsAmount
                accounts[receiver] = receiverBalance + coinsAmount
            }
        }
    }
}