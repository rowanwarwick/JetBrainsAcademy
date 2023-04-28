import java.util.Scanner

//import java.util.*
//
//fun main() {
//    val scanner = Scanner(System.`in`)
//    var maxIncrease = 0
//    var joke = 0
//    var index = 1
//    val n = readln().toInt()
//    val k = readln().toInt()
//    while (index <= k) {
//        val time = scanner.nextInt()
//        val allTimeJoke = joke + index * time
//        val currentIncrease = allTimeJoke + (n - index) * k
//        if (currentIncrease > maxIncrease) {
//            maxIncrease = currentIncrease
//        }
//        joke = allTimeJoke
//        index++
//    }
//    println(maxIncrease)
//}

//fun main() {
//    val scanner = Scanner(System.`in`)
//    val size = readln().toInt()
//    val list1 = MutableList(size) { scanner.nextInt() }
//    val list2 = MutableList(size) { scanner.nextInt() }
//    var left = 0
//    var sum1 = list1[0]
//    var sum2 = list2[0]
//    var max = if (sum1 == sum2) 0 else -1
//    for (right in 1..list1.lastIndex) {
//        sum1 += list1[right]
//        sum2 += list2[right]
//        if (sum1 == sum2) {
//            if (max < right - left) max = right - left
//            sum1 -= list1[left]
//            sum2 -= list2[left]
//            left++
//        }
//    }
//    val right = list1.lastIndex
//    while (left <= list1.lastIndex) {
//        if (sum1 == sum2) {
//            if (max < right - left) max = right - left
//        }
//        sum1 -= list1[left]
//        sum2 -= list2[left]
//        left++
//    }
//    println(max)
//}


fun a(): String { val a = return ""}
fun main() {
    println(a())
}
