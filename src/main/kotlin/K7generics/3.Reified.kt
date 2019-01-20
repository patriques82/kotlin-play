package K7generics

// fun <T> isType(value: Any) = value is T // => (compile error) 'cannot check for instance of erased type'

// only inline functions can be reified
inline fun <reified T> isType(value: Any): Boolean = value is T

inline fun <reified T> List<Any>.filterByType(): List<T> {
    val filtered = mutableListOf<T>()
    for (elem in this) {
        if (elem is T) {
            filtered.add(elem)
        }
    }
    return filtered
}

fun main(args: Array<String>) {
    println(isType<String>("test")) // true
    println(isType<Int>(true)) // false

    val list = listOf(1, "t", false, "e", true, "s", 3.9, "t")
    println(list.filterByType<String>().joinToString(separator = "")) // test
    println(list.filterByType<Boolean>().joinToString()) // false, true
}