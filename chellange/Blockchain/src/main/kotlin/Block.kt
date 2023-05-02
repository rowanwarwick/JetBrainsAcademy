import java.util.*
import kotlin.system.measureTimeMillis

class Block(blockchain: Blockchain, miner: CryptoMine.Miner) {
    private var minerID = miner.minerID
    private val timeStamp = System.currentTimeMillis()
    private var miningDifficulty = blockchain.difficulty.get()
    var content = mutableListOf<Transaction>()
    val blockId = blockchain.blockID.getAndIncrement()
    var hashingTime = 0L
    var blockHash = generateBlockHash("0".repeat(miningDifficulty))
    private var difficultiesComparison = blockchain.compareAndUpdateDifficulty(this)
    var prevBlockHash = if (blockId == 1) "0" else blockchain.chain[blockId -1]?.blockHash
    private var magic = 0


    private fun collectAndOrganizeContent(content: List<Transaction>): String {
        return if (content.isEmpty()) "No transactions" else content.joinToString("\n") { it.toString() }
    }

    private fun generateBlockHash(zerosPrefix: String): String {
        hashingTime = measureTimeMillis {
            do {
                magic = Random().nextInt(100000000)
                blockHash = SecurityUtils.applySHA256("$blockId$minerID$timeStamp$magic$prevBlockHash${collectAndOrganizeContent(content)}")
            } while (!blockHash.startsWith(zerosPrefix))
        }
        return blockHash
    }

    override fun toString(): String {
        return "\nBlock:\nCreated by miner # $minerID\nminer$minerID gets 100 VC\n" +
                "Id: $blockId\nTimestamp: $timeStamp\nMagic number: $magic\n" +
                "Hash of the previous block:\n$prevBlockHash\n" +
                "Hash of the block:\n$blockHash\nBlock data:\n${collectAndOrganizeContent(content)}\n" +
                "Block was generating for $hashingTime seconds\n$difficultiesComparison"
    }
}