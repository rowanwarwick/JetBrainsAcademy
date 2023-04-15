fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Error: Wrong number of arguments.")
    } else {
        val inputData = Words(args[1], args[0])
        if (inputData.check) {
            Game.randomWord = inputData.generateRandomWord()
            Game.dataWords = inputData.listAllWords
            Game.start()
        }
    }
}
