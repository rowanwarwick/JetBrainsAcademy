import java.util.*
import kotlin.random.Random

class Field(private val size: Int) {

    constructor(data: String) : this(calculateSize(data)) {
        labyrinth = data.split("\n").map { line -> line.chunked(2).toMutableList() }.toMutableList()
    }
    private val start: Pair<Int, Int> by lazy { Pair((1 until size).first { labyrinth[it][0] == "  " }, 0) }
    private val end: Pair<Int, Int> by lazy { Pair((1 until size).first { labyrinth[it][size - 1] == "  " }, size - 1) }

    private var labyrinth = MutableList(size) { MutableList(size) { "██" } }

    private fun searchVariants(x: Int, y: Int, condition: String): MutableList<Pair<Int, Int>> {
        val option = if (condition == "██") 0 else 1
        val result = mutableListOf<Pair<Int, Int>>()
        if (x + 2 in 1 until size - 1 && this.labyrinth[x + 2][y] == condition) result.add(Pair(x + 2 - option, y))
        if (x - 2 in 1 until size - 1 && this.labyrinth[x - 2][y] == condition) result.add(Pair(x - 2 + option, y))
        if (y + 2 in 1 until size - 1 && this.labyrinth[x][y + 2] == condition) result.add(Pair(x, y + 2 - option))
        if (y - 2 in 1 until size - 1 && this.labyrinth[x][y - 2] == condition) result.add(Pair(x, y - 2 + option))
        return result
    }

    private fun borderlineColumn(y: Int) {
        val position = if (y in 0..1) 1 else -1
        val check = (1..size - 2).all { this.labyrinth[it][y] == "██" }
        if (check) {
            var first = true
            val range = List(size - 2) { it + 1 }.shuffled()
            for (xList in range) {
                if (this.labyrinth[xList][y + position] == "  " && this.labyrinth[xList - 1][y] != "  " && this.labyrinth[xList + 1][y] != "  ") {
                    if (first) {
                        this.labyrinth[xList][y] = "  "
                        first = false
                    } else if (y != 0 && y != size - 1) {
                        if (Random.nextBoolean()) this.labyrinth[xList][y] = "  "
                    }
                }
            }
        }
    }

    private fun borderlineRow(x: Int) {
        val position = if (x == 1) 1 else -1
        val check = (1..size - 2).all { this.labyrinth[x][it] == "██" }
        if (check) {
            var first = true
            val range = List(size - 2) { it + 1 }.shuffled()
            for (yList in range) {
                if (this.labyrinth[x + position][yList] == "  " && this.labyrinth[x][yList - 1] != "  " && this.labyrinth[x][yList + 1] != "  ") {
                    if (first) {
                        this.labyrinth[x][yList] = "  "
                        first = false
                    } else {
                        if (Random.nextBoolean()) this.labyrinth[x][yList] = "  "
                    }
                }
            }
        }
    }

    private fun workWithBorderline() {
        borderlineColumn(1)
        borderlineColumn(size - 2)
        borderlineColumn(0)
        borderlineColumn(size - 1)
        borderlineRow(1)
        borderlineRow(size - 2)
    }

    fun generateLab() {
        val random = Random.Default
        val firstCellH = random.nextInt(1, size - 1)
        val firstCellW = random.nextInt(1, size - 1)
        this.labyrinth[firstCellH][firstCellW] = "  "
        val variants = searchVariants(firstCellH, firstCellW, "██")
        do {
            val randomFrontier = variants.removeAt(random.nextInt(variants.size))
            this.labyrinth[randomFrontier.first][randomFrontier.second] = "  "
            val cellBetweenEmptyAndFrontier = searchVariants(randomFrontier.first, randomFrontier.second, "  ")
            val cell = cellBetweenEmptyAndFrontier.elementAt(random.nextInt(cellBetweenEmptyAndFrontier.size))
            this.labyrinth[cell.first][cell.second] = "  "
            val newFrontier = searchVariants(randomFrontier.first, randomFrontier.second, "██")
            variants.addAll(newFrontier)
        } while (variants.isNotEmpty())
        workWithBorderline()
    }

    override fun toString(): String {
        val string = StringBuilder()
        for (row in labyrinth) {
            for (column in row) {
                string.append(column)
            }
            string.append("\n")
        }
        return string.toString()
    }

    companion object {
        fun calculateSize(data: String) = data.split("\n").filter { it.isNotEmpty() }.size
    }

    fun findPath(): List<Pair<Int, Int>> {
        val visited = Array(labyrinth.size) { BooleanArray(labyrinth[0].size) {false} }
        val queue = LinkedList<Pair<Int, Int>>()
        val prev = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        visited[start.first][start.second] = true
        queue.offer(start)
        while (queue.isNotEmpty()) {
            val curr = queue.poll()
            if (curr == end) {
                return buildPath(curr, prev)
            }
            val neighbors = getNeighbors(curr)
            for (next in neighbors) {
                if (!visited[next.first][next.second]) {
                    visited[next.first][next.second] = true
                    queue.offer(next)
                    prev[next] = curr
                }
            }
        }
        return emptyList()
    }

    private fun getNeighbors(curr: Pair<Int, Int>): List<Pair<Int, Int>> {
        val (x, y) = curr
        val neighbors = mutableListOf<Pair<Int, Int>>()
        if (x > 0 && labyrinth[x - 1][y] == "  ") {
            neighbors.add(x - 1 to y)
        }
        if (x < labyrinth.size - 1 && labyrinth[x + 1][y] == "  ") {
            neighbors.add(x + 1 to y)
        }
        if (y > 0 && labyrinth[x][y - 1] == "  ") {
            neighbors.add(x to y - 1)
        }
        if (y < labyrinth[0].size - 1 && labyrinth[x][y + 1] == "  ") {
            neighbors.add(x to y + 1)
        }
        return neighbors
    }

    private fun buildPath(curr: Pair<Int, Int>, prev: MutableMap<Pair<Int, Int>, Pair<Int, Int>>): List<Pair<Int, Int>> {
        val path = mutableListOf<Pair<Int, Int>>()
        var current = curr
        while (current != start) {
            path.add(current)
            current = prev[current]!!
        }
        path.add(start)
        path.forEach { labyrinth[it.first][it.second] = "//" }
        println(this)
        return path
    }
}