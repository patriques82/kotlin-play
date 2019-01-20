package K1classes

// Worse
class PrimaryConstructor3(_variable: Int) {
    val variable: Int
    init {
        variable = _variable
    }
}

// Better
class PrimaryConstructor2(_variable: Int) {
    val variable = _variable
}

// Best
open class PrimaryConstructor(val variable: Int)

class ExtendPrimaryConstructor(_variable2: Int): PrimaryConstructor(_variable2)

open class SecondaryConstructor {
    val half: Int
    constructor(variable: Int) {
        half = variable/2
    }
}

class ExtendSecondaryConstructor: SecondaryConstructor {
    val fourth: Int
    constructor(_variable: Int): super(_variable) {
        fourth = half/2
    }
}
fun main(args: Array<String>) {
    val ep = ExtendPrimaryConstructor(3)
    println(ep.variable) // 3

    val s1 = SecondaryConstructor(6)
    print(s1.half) // 3
    val s2 = ExtendSecondaryConstructor(8)
    print(s2.half) // 4
    print(s2.fourth) // 2
}

