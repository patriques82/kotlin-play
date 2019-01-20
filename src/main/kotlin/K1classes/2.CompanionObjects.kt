package K1classes

class Screamer private constructor(_mess: String) {
    val message: String = "${_mess.toUpperCase()}!"

    companion object { // allows for static methods
        fun create(_mess: String)= Screamer(_mess)
    }

    fun scream() = println(message)
}

fun main(args: Array<String>) {
    val screamer = Screamer.create("hello")
    screamer.scream() // HELLO!
}