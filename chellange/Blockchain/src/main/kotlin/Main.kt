fun main() {
    val dealMaker = DealMaker()
    val blockchain = Blockchain(dealMaker)
    SecurityUtils.initSecurity()
    CryptoMine().mining(blockchain, dealMaker)
}