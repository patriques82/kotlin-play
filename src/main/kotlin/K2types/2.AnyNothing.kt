package K2types

fun <T> erroneouslyPrintHashCode(o: T) = println(o.hashCode()) // object might be null (Any?)
fun <T: Any> correctPrintHashCode1(o: T) = println(o.hashCode()) // object is never null (Any)
fun <T> correctPrintHashCode2(o: T) = println(o?.hashCode()) // safe call

// Nothing is an empty type with no values
// Nothing is a subtype of all types, just as all types are subtypes of Any?

fun fail(mess: String): Nothing {
    throw Exception(mess) // returns Nothing (just as non terminating functions)
}

fun main(args: Array<String>) {
    erroneouslyPrintHashCode(null) // 0
    correctPrintHashCode1("Test") // 1160460865
    val listOfNum: List<Int> = null ?: fail("No numbers list") // Nothing conforms to type
}