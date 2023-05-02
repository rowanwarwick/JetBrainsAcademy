import java.util.stream.IntStream

class CryptoMine {
    private val blocksToMine = 15L
    private val minersHired = 20
    private val blockAward = 100

    fun mining(blockchain: Blockchain, dealMaker: DealMaker) {
        IntStream.rangeClosed(1, minersHired)
            .mapToObj { Miner(it, "miner $it", dealMaker) }
            .limit(blocksToMine)
            .forEach { miner ->
                run {
                    dealMaker.makingTransactions()
                    println(miner.createNewBlock(blockchain, miner))
                    dealMaker.accounts.let {
                        val updatedAmount = it[miner.name]
                        if (updatedAmount != null) it[miner.name] = updatedAmount + blockAward
                        else it[miner.name] = blockAward
                    }
//                    println(dealMaker.accounts) // todo debugging
                }
            }
    }

    class Miner(val minerID: Int, val name: String, private val dealMaker: DealMaker) {
        fun createNewBlock(blockchain: Blockchain, miner: Miner): Block {
            val block = Block(blockchain, miner)
            block.content = dealMaker.newTransactions
            return if (blockchain.addBlockToChain(block)) block else throw Exception("Invalid block")
        }
    }
}