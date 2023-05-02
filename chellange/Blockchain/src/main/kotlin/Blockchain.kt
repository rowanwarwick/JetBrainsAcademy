import java.util.concurrent.atomic.AtomicInteger

class Blockchain(private val dealMaker: DealMaker) {
    var chain = mutableMapOf<Int, Block>()
    var difficulty = AtomicInteger(0)
    var blockID = AtomicInteger(1)

    @Synchronized
    fun addBlockToChain(block: Block): Boolean {
        chain[block.blockId] = block
        if (contentVerified() && validBlock(block)) return true
        else chain.remove(block.blockId).also { return false }
    }

    fun compareAndUpdateDifficulty(block: Block): String {
        return when {
            (block.hashingTime > 2L) ->
                difficulty.set(difficulty.decrementAndGet().coerceAtLeast(0)).let { "N was decreased by 1" }
            (block.hashingTime < 1L) ->
                difficulty.set(difficulty.incrementAndGet()).let { "N was increased to ${difficulty.get()}" }
            else -> "N stays the same"
        }
    }

    private fun contentVerified(): Boolean {
        val transactionsIDsStream = chain.values.stream()
            .map { it.content }.flatMap { it.stream() }
            .map { it as Transaction }.map { it.id }.toList()
        return dealMaker.newTransactions.all {
            SecurityUtils.verifySignature(it.data.toByteArray(), it.signature, it.publicKey)
                    && transactionsIDsStream.sorted().toList() == transactionsIDsStream
        }
    }

    private fun validBlock(block: Block): Boolean {
        return when (block.blockId) {
            1 -> "0" == block.prevBlockHash
            else -> block.prevBlockHash == chain[block.blockId - 1]?.blockHash
        }
    }
}