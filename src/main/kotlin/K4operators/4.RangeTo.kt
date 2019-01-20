package K4operators

data class IP(val first: Int, val second: Int, val third: Int): Comparable<IP> {
    override fun compareTo(other: IP): Int =
        first.compareTo(other.first).coerceIn(-1, 1) * 100 +
                second.compareTo(other.second).coerceIn(-1, 1) * 10 +
                    third.compareTo(other.third)

    override fun toString(): String = "$first.$second.$third"
}

// implements: for (x in y..z)
operator fun ClosedRange<IP>.iterator(): Iterator<IP> =
    object : Iterator<IP> {
        var current: IP = start

        override fun hasNext(): Boolean =
            current < endInclusive

        override fun next(): IP =
            if (current.third < 127) {
                current.apply {
                    current = copy(third = third + 1)
                }
            } else {
                if (current.second < 127) {
                    current.apply {
                        current = copy(second = second + 1, third = 0)
                    }
                } else {
                    current.apply {
                        current = IP(first + 1, 0, 0)
                    }
                }
            }

    }

fun main(args: Array<String>) {
    val ip1 = IP(126, 127, 126)
    val ip2 = IP(127, 0, 2)

    // uses stdlib function: operator fun <T: Comparable<T>> T.rangeTo(that: T): ClosedRange<T>
    println(IP(127, 0, 1) in (ip1..ip2)) // true
    println(IP(126, 1, 1) in (ip1..ip2)) // false

    // uses defined function: operator fun ClosedRange<IP>.iterator(): Iterator<IP>
    for (ip in ip1..ip2) {
        println(ip)
    }
    // 126.127.126
    // 126.127.127
    // 127.0.0
    // 127.0.1

}