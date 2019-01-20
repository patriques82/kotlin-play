package K6higherorder

// Source: https://www.baeldung.com/kotlin-inline-functions

// Instead of creating an instance of a function type, which lambdas are converted to, we can write inline functions.
// Inline functions when called:
// - substitutes call with function body
// - inlines functions passed to it (in this case 'block')
inline fun <T> Collection<T>.each(block: (T) -> Unit) {
    for (e in this) block(e)
}

// Noinline:
// If you dont want a lambda passed to an inline function to be inlined drive 'noinline'
// inline fun foo(inlined: () -> Unit, noinline notInlined: () -> Unit) { ... }

fun main(args: Array<String>) {
    val numbers = listOf(1, 2, 3, 4, 5)
    numbers.each { println(it) } // this call translates to:
    // for (number in numbers)
    //    println(number)


    // Restrictions:
    // we can inline functions with lambda parameters only if
    // - the lambda is either called directly, or
    // - passed to another inline function
}