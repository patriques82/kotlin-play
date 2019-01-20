package K1classes

sealed class ScreenAction
data class Click(val x: Int, val y: Int): ScreenAction()
data class Drag(val fromX: Int, val fromY: Int, val toX: Int, val toY: Int): ScreenAction()

fun respond(screenAction: ScreenAction): Unit {
    when(screenAction) {
        is Click -> println("Click at (${screenAction.x},${screenAction.y})") // Smart cast
        is Drag -> println("Drag from (${screenAction.fromX},${screenAction.fromY}) to (${screenAction.toX},${screenAction.toY})")
    }
}

fun main(args: Array<String>) {
    val drag = Drag(4, 4, 6, 7)
    val click = Click(5, 5)

    respond(drag) // Drag from (4,4) to (6,7)
    respond(click) // Click at (5,5)
}