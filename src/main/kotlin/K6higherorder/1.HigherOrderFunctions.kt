package K6higherorder

typealias Function<A, B> = (A) -> B
typealias BiFunction<A, B, C> = (A, B) -> C

// Higher order functions either takes a function as parameter, returns function, or both
fun main(args: Array<String>) {

    fun applyTwice(f: Function<Int, Int>): Function<Int, Int> = { f(f(it)) }
    val square = { n: Int -> n * 2 }
    val cube = applyTwice { square(it) }

    println(square(3)) // 6
    println(cube(3)) // 12

    fun multiplyString(s: String, n: Int): String = if (n <= 1) s else "$s " + multiplyString(s, n-1)
    fun <A, B, C> flip(f: BiFunction<A, B, C>): BiFunction<B, A, C> = { b, a -> f(a,b) }
    val flippedMultiply = flip(::multiplyString)

    println(multiplyString("He", 4)) // He He He He
    println(flippedMultiply(4, "Ho")) // Ho Ho Ho Ho

    fun <A, B, C> compose(f: Function<A, B>, g: Function<B, C>): Function<A, C> = { g(f(it)) }
    val findInitials: (String) -> String =
        compose(
            { it.split(" ", limit = 2)}, // split name into first and family name
            { list: List<String> -> String(charArrayOf(list[0][0], list[1][0])) } // first char of first and family name
        )

    println(findInitials("Patrik Nygren")) // PN
}