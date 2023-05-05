import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val input = if (args.contains("-in")) args.getOrNull(args.indexOf("-in") + 1) else null
    val output = if (args.contains("-out")) args.getOrNull(args.indexOf("-out") + 1) else null
    val newX = if (args.contains("-width")) args.getOrNull(args.indexOf("-width") + 1) else null
    val newY = if (args.contains("-height")) args.getOrNull(args.indexOf("-height") + 1) else null
    if (input != null && File(input).exists()) {
        val picture = Picture(ImageIO.read(File(input)), output)
        if (newX?.toIntOrNull() != null && newY?.toIntOrNull() != null) picture.resize(newX.toInt(), newY.toInt())
    } else {
        println("Invalid input file")
    }
}