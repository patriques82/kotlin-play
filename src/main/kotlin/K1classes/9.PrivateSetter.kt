package K1classes

class CountSet {
    var count: Int = 0
        private set
    var sum = 0

    fun addNumber(n: Int) {
        count++ // setting only allowed inside class
        sum += n
    }
}

fun main(args: Array<String>) {
    val cs = CountSet()
    cs.addNumber(4)
    cs.addNumber(3)
    println(cs.count) // 2
    println(cs.sum) // 7
    // cs.count = 2 not allowed
}