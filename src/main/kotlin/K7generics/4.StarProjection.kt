package K7generics

// Star projections are used when you dont know/care about the generic type parameter
fun Pair<*, *>.flip(): Pair<*, *> = second to first

fun main(args: Array<String>) {
    val pair = 2 to "First"
    val (e1, e2) = pair.flip()
    println(e1) // "First"
    println(e2) // 2
}