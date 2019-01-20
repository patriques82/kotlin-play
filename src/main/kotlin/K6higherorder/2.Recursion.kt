package K6higherorder

fun <T: Comparable<T>> List<T>.quickSort(): List<T> =
    if (isEmpty()) emptyList()
    else {
        val firstElem = get(0)
        val smallerSorted = filter { it < firstElem }.quickSort() // higher-order filter + recursive call
        val largerSorted = filter { it > firstElem }.quickSort()
        smallerSorted + listOf(firstElem) + largerSorted
    }

// tail recursive calls rewrites body to an resource effiecient loop, so that stack overflow can be avoided
tailrec fun <T> reverse(acc: MutableList<T>, list: List<T>): List<T> =
    if(list.isEmpty())
        acc
    else {
        acc.add(list[list.lastIndex])
        reverse(acc, list.subList(0, list.lastIndex)) // tail recursive call (must be the last thing the function does)
    }

fun main(args: Array<String>) {
    val sorted = listOf(7, 3, 9, 8, 2, 1).quickSort()

    println(sorted) // [1, 2, 3, 7, 8, 9]
    println(reverse(mutableListOf(), sorted)) // [9, 8, 7, 3, 2, 1]
}
