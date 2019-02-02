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

// Either source: https://damo.io

sealed class Either<T, U>
data class Success<T, U>(val value: T): Either<T, U>()
data class Error<T, U>(val value: U): Either<T, U>()

data class Member(val name: String)
data class ErrorMessage(val message: String)

fun errorFunction(): Either<Member, ErrorMessage> {
    return Error(ErrorMessage("Oops"))
}

fun main(args: Array<String>) {
    val drag = Drag(4, 4, 6, 7)
    val click = Click(5, 5)

    respond(drag) // Drag from (4,4) to (6,7)
    respond(click) // Click at (5,5)

    val result = errorFunction()
    when (result) {
        is Success -> println("Success we got the user ${result.value.name}")
        is Error -> println("We got a failure ${result.value.message}") // We got a failure Oops
    }
}