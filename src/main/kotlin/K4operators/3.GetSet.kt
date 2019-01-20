package K4operators

class Matrix(val rows: Int, val cols: Int) {
    private val matrix: Array<Array<Int>> = Array(rows, { Array<Int>(cols, { 0 } )})

    // implements: matrix[x, y]
    operator fun get(x: Int, y: Int): Int = this.matrix[x].get(y)

    // implements: matrix[x, y] = value
    operator fun set(x: Int, y: Int, value: Int) = this.matrix[x].set(y, value)

    fun print() {
        matrix.forEach {
            it.forEach {
                print("$it ")
            }
            println()
        }
    }
}

fun main(args: Array<String>) {
    val matrix = Matrix(5,4)
    matrix.print()
    // 0 0 0 0
    // 0 0 0 0
    // 0 0 0 0
    // 0 0 0 0
    // 0 0 0 0
    matrix[2, 1] = 9
    println(matrix[2, 1]) // 9
    matrix.print()
    // 0 0 0 0
    // 0 0 0 0
    // 0 9 0 0
    // 0 0 0 0
    // 0 0 0 0
}

