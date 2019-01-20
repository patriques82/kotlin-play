package K4operators

class Node<T>(val value: T)

class Graph<T>(val nodes: Map<Node<T>, List<Node<T>>>)

class Path<T>(vararg _nodes: Node<T>) {
    val nodes = _nodes.asList()
}

// implements: in !in
operator fun <T> Graph<T>.contains(path: Path<T>): Boolean {
    val size = path.nodes.size
    for (i in 0 until size - 1) {
        val current = path.nodes[i]
        val next = path.nodes[i + 1]
        if (next !in (nodes[current] ?: return false)) {
            return false
        }
    }
    return true
}

fun main(args: Array<String>) {
    val K = Node("K")
    val o = Node("o")
    val t = Node("t")
    val l = Node("l")
    val i = Node("i")
    val n = Node("n")

    val J = Node("J")
    val a = Node("a")
    val v = Node("v")

    val graph = Graph(mapOf(
        K to listOf(t, o, n),
        o to listOf(i, n, t),
        t to listOf(n, K, l),
        l to listOf(n, i),
        i to listOf(o, t, n),
        J to listOf(K, l),
        a to listOf(v, J),
        v to listOf(o)
    ))

    val kotlinPath = Path(K, o, t, l, i, n)
    val javaPath = Path(J, a, v, a)
    println(kotlinPath in graph) // true
    println(javaPath !in graph) // true
}