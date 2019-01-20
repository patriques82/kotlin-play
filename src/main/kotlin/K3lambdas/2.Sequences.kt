package K3lambdas

fun main(args: Array<String>) {
    val isEven = { n: Int -> print("isEven($n) "); n % 2 == 0 }
    val double = { n: Int -> print("double($n) "); n * n }

    listOf(0,1,2,3,4,5,6).asSequence()
        .filter(isEven)
        .map(double)
        .toList() // isEven(0) double(0) isEven(1) isEven(2) double(2) isEven(3) isEven(4) double(4) isEven(5) isEven(6) double(6)

    listOf(0,1,2,3,4,5,6).asSequence()
        .filter(isEven)
        .map(double) // nothing, since sequences are lazy

    val firstEvenGreaterThan100 = generateSequence(0) { it + 1 }
        .filter(isEven)
        .firstOrNull { it > 100 }
    println(firstEvenGreaterThan100) // 102

}