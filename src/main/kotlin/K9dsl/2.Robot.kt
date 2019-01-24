package K9dsl

import kotlin.properties.Delegates

// source: https://en.wikipedia.org/wiki/Logo_(programming_language)

fun turtleWalk(x: Int, y: Int, body: Turtle.() -> Unit): Turtle = // body: Lambda with receiver
    Turtle(x, y).apply(body)

class Turtle(x: Int, y: Int) {
    val start = Cell(x, y)
    private var currentSteps = 0
    private var penIsDown = false
    private val visitedCells: MutableList<Cell> = mutableListOf(start)

    var cell by Delegates.observable(start) { _, oldCell, newCell ->
        if(penIsDown) visitedCells.addAll(newCell - oldCell)
        else visitedCells.add(newCell)
    }

    fun pendown() {
        penIsDown = true
    }

    fun penup() {
        penIsDown = false
    }

    fun move(nrOfSteps: Int): Turtle {
        currentSteps = nrOfSteps
        return this
    }

    infix fun steps(dir: DirectionWrapper) = // infix function: `x.steps(y)` can be rewritten to `x steps y`
        when (dir.value) {
            Direction.LEFT -> cell = Cell(cell.x - currentSteps, cell.y)
            Direction.RIGHT -> cell = Cell(cell.x + currentSteps, cell.y)
            Direction.DOWN -> cell = Cell(cell.x, cell.y + currentSteps)
            Direction.UP -> cell = Cell(cell.x, cell.y - currentSteps)
        }

    fun trace() = println(visitedCells.joinToString(separator = ", ") { it.toString() })

}

data class Cell(val x: Int, val y: Int) {
    operator fun minus(other: Cell): List<Cell> =
        when {
            x > other.x -> (other.x+1   ..   x).map { Cell(it, y) }
            x < other.x -> (other.x-1 downTo x).map { Cell(it, y) }
            y > other.y -> (other.y+1   ..   y).map { Cell(x, it) }
            y < other.y -> (other.y-1 downTo y).map { Cell(x, it) }
            else -> emptyList()
        }

    override fun toString(): String = "(${x},${y})"
}

enum class Direction { LEFT, RIGHT, DOWN, UP }

open class DirectionWrapper(val value: Direction)

object right : DirectionWrapper(Direction.RIGHT)
object down : DirectionWrapper(Direction.DOWN)
object left : DirectionWrapper(Direction.LEFT)
object up : DirectionWrapper(Direction.UP)


fun main(args: Array<String>) {
    val turtle = turtleWalk(x = 1, y = 1) {
        pendown()
        move(3) steps right
        penup()
        move(2) steps down
        pendown()
        move(3) steps left
        penup()
        move(1) steps up
    }
    turtle.trace() // (1,1), (2,1), (3,1), (4,1), (4,3), (3,3), (2,3), (1,3), (1,2)
    println("final cell ${turtle.cell}") // final cell (1,2)
}