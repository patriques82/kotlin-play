package K3lambdas

import java.lang.StringBuilder

fun main(args: Array<String>) {
    val sb1 = StringBuilder()

    with(sb1) {
        append("H")
        append("E")
        append("L")
        append("L")
        append("O")
    }

    println(sb1.toString()) // HELLO

    val sb2 = StringBuilder().apply {
        append("W")
        append("O")
        append("R")
        append("L")
        append("D")
    }

    println(sb2.toString()) // WORLD
}