import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.pow

class Picture(private var image: BufferedImage, private val output: String? = null) {
    private var maxValue = 0.0
    private var matrix = MutableList(image.height) { MutableList(image.width) { 0.0 } }

    fun resize(newX: Int, newY: Int) {
        val conditionX = image.width - newX
        val conditionY = image.height - newY
        do {
            val newImage = if (image.width != conditionX) BufferedImage(
                image.width - 1,
                image.height,
                BufferedImage.TYPE_INT_RGB
            ) else
                BufferedImage(image.width, image.height - 1, BufferedImage.TYPE_INT_RGB)
            if (image.width != conditionX) {
                val indexes = findSeam('v')
                for (y in 0 until newImage.height) {
                    var flagForDeletePix = 0
                    for (x in 0 until newImage.width) {
                        if (x == indexes[indexes.lastIndex - y]) flagForDeletePix = 1
                        newImage.setRGB(x, y, image.getRGB(x + flagForDeletePix, y))
                    }
                }
            } else {
                val indexes = findSeam('h')
                for (x in 0 until newImage.width) {
                    var flagForDeletePix = 0
                    for (y in 0 until newImage.height) {
                        if (y == indexes[indexes.lastIndex - x]) flagForDeletePix = 1
                        newImage.setRGB(x, y, image.getRGB(x, y + flagForDeletePix))
                    }
                }
            }
            image = newImage
        } while (!(image.width == conditionX && image.height == conditionY))
        ImageIO.write(image, "png", File(output ?: "output.png"))
    }

    private fun findSeam(option: Char): MutableList<Int> {
        val result = mutableListOf<Int>()
        createEnergyMatrix()
        val sumEnergyMatrix =
            if (option == 'v') matrix.toMutableList().map { it.toMutableList() } else rotateMatrix(matrix)
        val maxY = if (option == 'v') image.height else image.width
        val maxX = if (option == 'v') image.width else image.height
        for (y in 1 until maxY) {
            for (x in 0 until maxX) {
                val variants = mutableListOf(sumEnergyMatrix[y - 1][x])
                if (x != 0) variants.add(sumEnergyMatrix[y - 1][x - 1])
                if (x != maxX - 1) variants.add(sumEnergyMatrix[y - 1][x + 1])
                sumEnergyMatrix[y][x] += Collections.min(variants)
            }
        }
        var indexCellSeam = sumEnergyMatrix[maxY - 1].indexOf(Collections.min(sumEnergyMatrix[maxY - 1]))
        for (coordinate in maxY - 1 downTo 0) {
            if (coordinate != maxY - 1) {
                val variants = mutableListOf(
                    if (indexCellSeam != 0) sumEnergyMatrix[coordinate][indexCellSeam - 1] else sumEnergyMatrix[coordinate][indexCellSeam] + 1,
                    sumEnergyMatrix[coordinate][indexCellSeam],
                    if (indexCellSeam != maxX - 1) sumEnergyMatrix[coordinate][indexCellSeam + 1] else sumEnergyMatrix[coordinate][indexCellSeam] + 1
                )
                indexCellSeam += variants.indexOf(Collections.min(variants)) - 1
            }
            result.add(indexCellSeam)
        }
        return result
    }

    private fun <T> rotateMatrix(matrix: MutableList<MutableList<T>>): MutableList<MutableList<T>> {
        val result = MutableList(matrix[0].size) { MutableList(matrix.size) { null as T } }
        for (y in 0 until matrix.size) {
            for (x in 0 until matrix[y].size) {
                result[x][y] = matrix[y][x]
            }
        }
        return result
    }

    private fun createEnergyMatrix() {
        matrix = MutableList(image.height) { MutableList(image.width) { 0.0 } }
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val energy = energy(x, y)
                if (energy > maxValue) maxValue = energy
                matrix[y][x] = energy
            }
        }
        normalizeMatrix()
    }

    private fun normalizeMatrix() {
        for (y in matrix.indices) {
            for (x in matrix[y].indices) {
                matrix[y][x] = 255.0 * matrix[y][x] / maxValue
            }
        }
    }

    private fun energy(x: Int, y: Int): Double {
        val changeX = changeCoordinate(x, image.width - 1)
        val changeY = changeCoordinate(y, image.height - 1)
        val gradX = when {
            image.width > 2 -> gradient(Color(image.getRGB(changeX - 1, y)), Color(image.getRGB(changeX + 1, y)))
            image.width == 1 -> gradient(Color(image.getRGB(0, y)), Color(image.getRGB(0, y)))
            else -> gradient(Color(image.getRGB(0, y)), Color(image.getRGB(1, y)))
        }
        val gradY = when {
            image.height > 2 -> gradient(Color(image.getRGB(x, changeY - 1)), Color(image.getRGB(x, changeY + 1)))
            image.height == 1 -> gradient(Color(image.getRGB(x, 0)), Color(image.getRGB(x, 0)))
            else -> gradient(Color(image.getRGB(x, 0)), Color(image.getRGB(x, 1)))
        }
        return (gradX + gradY).pow(0.5)
    }

    private fun gradient(color1: Color, color2: Color): Double {
        val r = (color1.red - color2.red).toDouble().pow(2)
        val g = (color1.green - color2.green).toDouble().pow(2)
        val b = (color1.blue - color2.blue).toDouble().pow(2)
        return r + g + b
    }

    private fun changeCoordinate(c: Int, max: Int): Int {
        return when (c) {
            0 -> 1
            max -> max - 1
            else -> c
        }
    }

    @Suppress("Unused")
    fun negative() {
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val color = Color(image.getRGB(x, y))
                val colorNew = Color(255 - color.red, 255 - color.green, 255 - color.blue)
                image.setRGB(x, y, colorNew.rgb)
            }
        }
        ImageIO.write(image, "png", File(output ?: "output.png"))
    }

    @Suppress("Unused")
    fun printEnergyMatrix() {
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val colorNew = Color(matrix[y][x].toInt(), matrix[y][x].toInt(), matrix[y][x].toInt())
                image.setRGB(x, y, colorNew.rgb)
            }
        }
        ImageIO.write(image, "png", File(output ?: "output.png"))
    }
}