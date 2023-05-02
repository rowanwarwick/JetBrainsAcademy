class Transaction(val id: Int, private val sender: String, private val receiver: String, private val coins: Int) {
    val publicKey = SecurityUtils.publicKey
    val data: String = String.format("%d %s %s %d", id, sender, receiver, coins)
    val signature = SecurityUtils.signTransaction(data)

    override fun toString() = "%s sent %d VC to %s".format(sender, coins, receiver)
}