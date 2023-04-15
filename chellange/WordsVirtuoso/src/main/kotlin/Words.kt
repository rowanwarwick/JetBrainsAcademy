import java.io.File
import java.io.FileReader
import kotlin.random.Random

class Words(private val fileCandidate: String, private val fileAll: String) {

    private val listCandidates = mutableListOf<String>()
    val listAllWords = mutableListOf<String>()
    val check: Boolean by lazy {
        fileCanOpen(fileAll) && fileCanOpen(fileCandidate, true) &&
                numberIncorrectWords() && numberIncorrectWords(true) && isIncludeAllCandidate()
    }

    private fun fileCanOpen(name: String, isCandidate: Boolean = false): Boolean {
        val result = File(name).exists()
        if (!result) println("Error: The ${if (isCandidate) "candidate " else ""}words file $name doesn't exist.")
        else {
            FileReader(name).use {
                if (isCandidate) listCandidates.addAll(it.readLines().map { word -> word.lowercase() })
                else listAllWords.addAll(it.readLines().map { word -> word.lowercase() })
            }
        }
        return result
    }

    private fun isIncludeAllCandidate(): Boolean {
        val result = listCandidates.filter { !listAllWords.contains(it) }.size
        if (result != 0) println("Error: $result candidate words are not included in the $fileAll file.")
        return result == 0
    }

    private fun conditionRightWord(word: String): Boolean {
        word.apply { return !(length != 5 || contains("[^a-zA-Z]".toRegex()) || toSet().size != length) }
    }

    private fun numberIncorrectWords(isCandidate: Boolean = false): Boolean {
        val result = (if (isCandidate) listCandidates else listAllWords).filter { !conditionRightWord(it) }.size
        if (result != 0) println("Error: $result invalid words were found in the ${if (!isCandidate) fileAll else fileCandidate} file.")
        return result == 0
    }

    fun generateRandomWord() = listCandidates[Random.Default.nextInt(listCandidates.size)]
}